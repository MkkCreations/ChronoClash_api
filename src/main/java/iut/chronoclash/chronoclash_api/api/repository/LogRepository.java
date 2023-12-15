package iut.chronoclash.chronoclash_api.api.repository;

import iut.chronoclash.chronoclash_api.api.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {
    Optional<Log> findByName(String name);
}