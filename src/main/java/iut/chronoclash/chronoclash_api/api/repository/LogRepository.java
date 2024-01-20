package iut.chronoclash.chronoclash_api.api.repository;

import iut.chronoclash.chronoclash_api.api.model.Log;
import iut.chronoclash.chronoclash_api.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {
    List<Log> findByOwner(User owner);
    Optional<Log> findByOwnerAndName(User owner, String name);
}