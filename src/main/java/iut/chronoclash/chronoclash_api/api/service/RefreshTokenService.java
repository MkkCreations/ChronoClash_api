package iut.chronoclash.chronoclash_api.api.service;

import iut.chronoclash.chronoclash_api.api.model.RefreshToken;
import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public void deleteById(String id) {
        refreshTokenRepository.deleteById(id);
    }

    public void deleteByOwner(User owner) {
        refreshTokenRepository.deleteAllByOwner(owner);
    }

    public RefreshToken create(User owner, String requestIp, String requestUserAgent) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(owner);
        refreshToken.setRequestIp(requestIp);
        refreshToken.setRequestUserAgent(requestUserAgent);
        refreshToken.setDate(new Date());
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken getById(String id) {
        return refreshTokenRepository.findById(id).orElse(null);
    }

    public List<RefreshToken> getByOwner(User owner) {
        return refreshTokenRepository.findAllByOwner(owner).orElse(null);
    }

    public boolean existsById(String id) {
        return refreshTokenRepository.existsById(id);
    }

}
