package iut.chronoclash.chronoclash_api.api.service;

import iut.chronoclash.chronoclash_api.api.dto.FriendNotificationDTO;
import iut.chronoclash.chronoclash_api.api.model.Friend;
import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.repository.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FriendService {
    @Autowired
    FriendRepository friendRepository;
    @Autowired
    UserService userService;

    public List<Friend> getAllByUser(User user) {
        return friendRepository.findFriendsByOwner(user).stream().filter(f -> !f.isBlocked() && f.isAccepted()).toList();
    }

    public Friend getById(String id) {
        return friendRepository.findById(id).orElseThrow();
    }

    public Friend addFriend(String userId, String friendId) {
        if (friendRepository.existsByOwnerAndFriend(userService.findById(userId), userService.findById(friendId))) {
            throw new IllegalArgumentException("Friend request already exists");
        }
        if (userId.equals(friendId)) throw new IllegalArgumentException("You can't add yourself as a friend");
        // "You are already friends"
        if (friendRepository.findFriendsByOwner(userService.findById(userId)).stream().anyMatch(f -> f.getFriend().getId().equals(userService.findById(friendId)))) throw new IllegalArgumentException("You are already friends");
        // "You are blocked by this user"
        if (friendRepository.findFriendsByOwner(userService.findById(userId)).stream().anyMatch(f -> f.getFriend().getId().equals(userService.findById(friendId)) && f.isBlocked())) throw new IllegalArgumentException("You are blocked by this user");
        // "You already sent a friend request"
        if (friendRepository.findFriendsByOwner(userService.findById(userId)).stream().anyMatch(f -> f.getFriend().getId().equals(userService.findById(friendId)) && !f.isAccepted())) throw new IllegalArgumentException("You already sent a friend request");

        User user = userService.findById(userId);
        User friend = userService.findById(friendId);
        Friend newFriend = new Friend(user, friend, false, false, new Date());
        return friendRepository.save(newFriend);
    }

    public List<FriendNotificationDTO> getNotifications(User user) {
        return friendRepository.findFriendsByFriendAndAcceptedAndBlocked(user, false, false)
                .stream().map(f -> new FriendNotificationDTO(
                        f.getId(),
                        f.getOwner().getUsername(),
                        f.getOwner().getName(),
                        f.getOwner().getImage(),
                        f.getOwner().getLevel(),
                        f.getDate()))
                .toList();
    }

    public void acceptFriend(String requestId) {
        Friend friend = friendRepository.findById(requestId).orElseThrow();
        friend.setAccepted(true);
        if (friendRepository.existsByOwnerAndFriend(friend.getFriend(), friend.getOwner())) {
            throw new IllegalArgumentException("This friend request has already been accepted");
        }
        Friend newFriend = addFriend(friend.getFriend().getId(), friend.getOwner().getId());
        newFriend.setAccepted(true);
        friendRepository.save(newFriend);
        friendRepository.save(friend);
    }

    public void blockFriend(String id) {
        Friend friend = friendRepository.findById(id).orElseThrow();
        friend.setBlocked(true);
        friendRepository.save(friend);
    }

    public void unblockFriend(String id) {
        Friend friend = friendRepository.findById(id).orElseThrow();
        friend.setBlocked(false);
        friendRepository.save(friend);
    }

    public void deleteFriend(String id) {
        Friend friend = friendRepository.findById(id).orElseThrow();
        friendRepository.deleteByFriendId(friend.getId());
        friendRepository.deleteById(id);
    }

    public void deleteByUser(String userId) {
        friendRepository.deleteAllByOwner(userService.findById(userId));
    }
}
