package iut.chronoclash.chronoclash_api.api.repository;

import iut.chronoclash.chronoclash_api.api.model.RefreshToken;
import iut.chronoclash.chronoclash_api.api.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
     void deleteAllByOwner(User owner);
     Optional<List<RefreshToken>> findAllByOwner(User owner);
     Optional<RefreshToken> findById(String id);
}