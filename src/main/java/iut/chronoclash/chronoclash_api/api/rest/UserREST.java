package iut.chronoclash.chronoclash_api.api.rest;

import iut.chronoclash.chronoclash_api.api.dto.ErrorResponseDTO;
import iut.chronoclash.chronoclash_api.api.dto.GameDTO;
import iut.chronoclash.chronoclash_api.api.dto.TokenDTO;
import iut.chronoclash.chronoclash_api.api.dto.UsersDTO;
import iut.chronoclash.chronoclash_api.api.jwt.JwtHelper;
import iut.chronoclash.chronoclash_api.api.model.Game;
import iut.chronoclash.chronoclash_api.api.model.Operation;
import iut.chronoclash.chronoclash_api.api.model.RefreshToken;
import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @Autowired
    FriendService friendService;

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @RequestBody User newUser) {
        if (newUser.getImage() != null && newUser.getImage().length >= 1000000) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO("Image size too large"));
        }
        return ResponseEntity.ok(userService.update(user, newUser));
    }

    @PostMapping("/game")
    @Transactional
    public ResponseEntity<?> createGame(@AuthenticationPrincipal User user, @RequestBody GameDTO dto) {
        gameService.createGame(dto);
        User updatedUser =  userService.updateLevel(user, dto.getXp());
        Operation operation = new Operation();
        operation.setType(dto.isWin() ? "Win" : "Lose");
        operation.setDescription(
                "You " + (dto.isWin() ? "won" : "lost") + " against " + dto.getEnemy() + " and earned " + dto.getXp() + " xp"
        );
        operation.setDate(new Date());
        logService.createLog("Game", operation, updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@AuthenticationPrincipal User user) {
        List<UsersDTO> users = userService.findAll().stream()
                .filter(u ->
                        !u.getId().equals(user.getId()) && friendService.getAllByUser(user).stream().noneMatch(f -> f.getFriend().getId().equals(u.getId())))
                .toList();

        return ResponseEntity.ok(Map.of("users", users));
    }
}
