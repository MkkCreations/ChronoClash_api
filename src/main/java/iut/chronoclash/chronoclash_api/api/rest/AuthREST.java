package iut.chronoclash.chronoclash_api.api.rest;

import iut.chronoclash.chronoclash_api.api.dto.*;
import iut.chronoclash.chronoclash_api.api.jwt.JwtHelper;
import iut.chronoclash.chronoclash_api.api.model.Operation;
import iut.chronoclash.chronoclash_api.api.model.RefreshToken;
import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.repository.RefreshTokenRepository;
import iut.chronoclash.chronoclash_api.api.repository.UserRepository;
import iut.chronoclash.chronoclash_api.api.service.LogService;
import iut.chronoclash.chronoclash_api.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthREST {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    LogService logService;
    String invalidToken = "invalid token";

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(user);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(user);
        String refreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

        Operation operation = new Operation();
        operation.setType("login");
        operation.setDate(new Date().toString());
        operation.setDescription(String.format("User %s logged in", user.getUsername()));

        logService.createLog("User", operation, user);

        return ResponseEntity.ok(new TokenDTO(user, accessToken, refreshTokenString));
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDTO dto) {
        User user = new User(dto.getName(), dto.getUsername(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()), dto.getImage(), "USER", null);
        userRepository.save(user);

        return getResponseEntity(user);
    }

    private ResponseEntity<?> getResponseEntity(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(user);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(user);
        String refreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

        return ResponseEntity.ok(new TokenDTO(user, accessToken, refreshTokenString));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db
            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException(invalidToken);
    }

    @PostMapping("/logout-all")
    public ResponseEntity<?> logoutAll(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            refreshTokenRepository.deleteByOwner_Id(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException(invalidToken);
    }

    @PostMapping("/access-token")
    public ResponseEntity<?> accessToken(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            User user = userService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            String accessToken = jwtHelper.generateAccessToken(user);

            return ResponseEntity.ok(new TokenDTO(user, accessToken, refreshTokenString));
        }

        throw new BadCredentialsException(invalidToken);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));

            User user = userService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));

            Operation operation = new Operation();
            operation.setType("login");
            operation.setDate(new Date().toString());
            operation.setDescription(String.format("User %s logged in", user.getUsername()));

            logService.createLog("User", operation, user);
            return getResponseEntity(user);
        }

        throw new BadCredentialsException(invalidToken);
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal User user, @RequestBody ChangePassDTO dto, @PathVariable String id ) {
        if (!user.getId().equals(id)) {
            return ResponseEntity.badRequest().body("invalid user");
        }
        if (!passwordEncoder.matches(dto.getActualPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("invalid password");
        } else if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("passwords do not match");
        } else {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            userRepository.save(user);

            Operation operation = new Operation();
            operation.setType("update");
            operation.setDate(new Date().toString());
            operation.setDescription(String.format("User %s changed password", user.getUsername()));

            logService.createLog("User", operation, user);

            return ResponseEntity.ok().build();
        }
    }
}