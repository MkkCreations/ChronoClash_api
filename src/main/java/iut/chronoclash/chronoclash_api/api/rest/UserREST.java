package iut.chronoclash.chronoclash_api.api.rest;

import iut.chronoclash.chronoclash_api.api.dto.GameDTO;
import iut.chronoclash.chronoclash_api.api.dto.TokenDTO;
import iut.chronoclash.chronoclash_api.api.model.Game;
import iut.chronoclash.chronoclash_api.api.model.Operation;
import iut.chronoclash.chronoclash_api.api.model.RefreshToken;
import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.service.GameService;
import iut.chronoclash.chronoclash_api.api.service.LogService;
import iut.chronoclash.chronoclash_api.api.service.RefreshTokenService;
import iut.chronoclash.chronoclash_api.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserREST {
    @Autowired
    UserService userService;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    GameService gameService;
    @Autowired
    LogService logService;

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PostMapping("/game")
    @Transactional
    public ResponseEntity<?> createGame(@AuthenticationPrincipal User user, @RequestBody GameDTO dto) {
        gameService.createGame(dto);
        userService.updateLevel(user, dto.getXp());
        Operation operation = new Operation();
        operation.setType(dto.isWin() ? "Win" : "Lose");
        operation.setDescription(
                "You " + (dto.isWin() ? "won" : "lost") + " against " + dto.getEnemy() + " and earned " + dto.getXp() + " xp"
        );
        operation.setDate(new Date().toString());
        logService.createLog("Game", operation, user);
        return ResponseEntity.ok(new TokenDTO(user, null, null));
    }

    @GetMapping("/connections")
    public ResponseEntity<?> getConnections(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(refreshTokenService.getByOwner(user));
    }
}
