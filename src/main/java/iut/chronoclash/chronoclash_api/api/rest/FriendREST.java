package iut.chronoclash.chronoclash_api.api.rest;

import iut.chronoclash.chronoclash_api.api.dto.AddFriendDTO;
import iut.chronoclash.chronoclash_api.api.model.Friend;
import iut.chronoclash.chronoclash_api.api.model.Log;
import iut.chronoclash.chronoclash_api.api.model.Operation;
import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.service.FriendService;
import iut.chronoclash.chronoclash_api.api.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/friend")
public class FriendREST {
    @Autowired
    FriendService friendService;
    @Autowired
    LogService logService;

    /**
     * Get all friends
     * @param user the user
     * @return a response entity
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendService.getAllByUser(user));
    }

    /**
     * Add a friend
     * @param user the user
     * @param dto the add friend dto
     * @return a response entity
     */
    @PostMapping ("/add")
    @Transactional
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @RequestBody AddFriendDTO dto) {
        try {
            Friend friend = friendService.addFriend(user.getId(), dto.getId());

            // Creation of the Log
            Operation operation = new Operation();
            operation.setType("add");
            operation.setDate(new Date());
            operation.setDescription(String.format("You added user %s as a friend", friend.getFriend().getUsername()));
            logService.createLog("Friend", operation, user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Friend request sent");
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("notifications", friendService.getNotifications(user)));
    }

    /**
     * Accept a friend request
     * @param id the friend id
     * @return a response entity
     */
    @GetMapping("/accept/{id}")
    public ResponseEntity<?> accept(@AuthenticationPrincipal User user, @PathVariable String id) {
        try {
            friendService.acceptFriend(id);

            // Creation of the Log
            Operation operation = new Operation();
            operation.setType("accept");
            operation.setDate(new Date());
            operation.setDescription(String.format("You accepted a friend request from user %s", friendService.getById(id).getOwner().getUsername()));
            logService.createLog("Friend", operation, user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Friend request accepted");
    }

    /**
     * Delete a friend
     * @param id the friend id
     * @return a response entity
     */
    @GetMapping("/block")
    public ResponseEntity<?> block(String id) {
        friendService.blockFriend(id);
        return ResponseEntity.ok("Friend blocked");
    }

    /**
     * Unblock a friend
     * @param id the friend id
     * @return a response entity
     */
    @GetMapping("/unblock")
    public ResponseEntity<?> unblock(String id) {
        friendService.unblockFriend(id);
        return ResponseEntity.ok("Friend unblocked");
    }
}
