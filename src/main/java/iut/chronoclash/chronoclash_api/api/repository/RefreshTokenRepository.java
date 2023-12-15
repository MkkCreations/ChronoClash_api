package iut.chronoclash.chronoclash_api.api.repository;

import iut.chronoclash.chronoclash_api.api.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByOwner_Id(String id);

    default void deleteByOwner(RefreshToken refreshToken) {
        deleteByOwner_Id(refreshToken.getOwner().getId());
    }
}