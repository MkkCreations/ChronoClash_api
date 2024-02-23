package iut.chronoclash.chronoclash_api.api.repository;

import iut.chronoclash.chronoclash_api.api.model.Friend;
import iut.chronoclash.chronoclash_api.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, String> {
    List<Friend> findFriendsByOwner(User user);
    Friend findFriendsByOwnerAndFriend(User owner, User friend);
    List<Friend> findFriendsByFriendAndAcceptedAndBlocked(User user, boolean accepted, boolean blocked);
    boolean existsByOwnerAndFriend(User user, User friend);
    void deleteByFriendId(String id);
    void deleteAllByOwner(User user);
}
