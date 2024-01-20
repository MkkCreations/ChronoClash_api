package iut.chronoclash.chronoclash_api.api.rest;

import iut.chronoclash.chronoclash_api.api.dto.*;
import iut.chronoclash.chronoclash_api.api.jwt.JwtHelper;
import iut.chronoclash.chronoclash_api.api.model.Operation;
import iut.chronoclash.chronoclash_api.api.model.RefreshToken;
import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.repository.RefreshTokenRepository;
import iut.chronoclash.chronoclash_api.api.repository.UserRepository;
import iut.chronoclash.chronoclash_api.api.service.LogService;
import iut.chronoclash.chronoclash_api.api.service.RefreshTokenService;
import iut.chronoclash.chronoclash_api.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    RefreshTokenService refreshTokenService;
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

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> login(@RequestBody LoginDTO dto, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        RefreshToken refreshToken = new RefreshToken();

        refreshTokenService.create(user, request.getRemoteAddr(), request.getHeader("User-Agent"));

        String accessToken = jwtHelper.generateAccessToken(user);
        String refreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

        // Creation of the Log
        Operation operation = new Operation();
        operation.setType("Login");
        operation.setDate(new Date().toString());
        operation.setDescription(String.format("User %s logged in", user.getUsername()));
        logService.createLog("Auth", operation, user);

        return ResponseEntity.ok(new TokenDTO(user, accessToken, refreshTokenString));
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDTO dto, HttpServletRequest request) {
        // Check if username or email is taken, and if password is valid
        if (userService.isUsernameTaken(dto.getUsername())) {
            return ResponseEntity.badRequest().body("username already exists");
        } else if (userService.isEmailTaken(dto.getEmail())) {
            return ResponseEntity.badRequest().body("email already exists");
        } else if (!userService.validatePassword(dto.getPassword())) {
            return ResponseEntity.badRequest().body("passwords must be at least 8 characters long");
        }

        // Creation of the User
        User user = new User(dto.getName(), dto.getUsername(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()), dto.getImage(), "USER", null, null, null);
        userService.create(user);

        // Creation of the Log
        Operation operation = new Operation();
        operation.setType("Signup");
        operation.setDate(new Date().toString());
        operation.setDescription(String.format("User %s signed up", user.getUsername()));
        logService.createLog("Auth", operation, user);

        return getResponseEntity(user, request);
    }

    private ResponseEntity<?> getResponseEntity(User user, HttpServletRequest request) {
        RefreshToken refreshToken = refreshTokenService.create(user, request.getRemoteAddr(), request.getHeader("User-Agent"));

        String accessToken = jwtHelper.generateAccessToken(user);
        String refreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

        return ResponseEntity.ok(new TokenDTO(user, accessToken, refreshTokenString));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenDTO refreshToken) {
        String refreshTokenString = refreshToken.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenService.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db
            User user = userService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            refreshTokenService.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));

            // Creation of the Log
            Operation operation = new Operation();
            operation.setType("Logout");
            operation.setDate(new Date().toString());
            operation.setDescription(String.format("User %s logged out", user.getUsername()));
            logService.createLog("Auth", operation, user);

            return ResponseEntity.ok().build();
        }
        throw new BadCredentialsException(invalidToken);
    }

    @PostMapping("/logout-all")
    @Transactional
    public ResponseEntity<?> logoutAll(@AuthenticationPrincipal User user) {
        // valid and exists in db
        refreshTokenService.deleteByOwner(user);

        // Creation of the Log
        Operation operation = new Operation();
        operation.setType("Logout");
        operation.setDate(new Date().toString());
        operation.setDescription(String.format("User %s logged out from all devices", user.getUsername()));
        logService.createLog("Auth", operation, user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/access-token")
    public ResponseEntity<?> accessToken(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenService.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db
            User user = userService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            String accessToken = jwtHelper.generateAccessToken(user);
            return ResponseEntity.ok(new TokenDTO(user, accessToken, refreshTokenString));
        }
        throw new BadCredentialsException(invalidToken);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDTO dto, HttpServletRequest request) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenService.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db
            refreshTokenService.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));

            User user = userService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));

            // Creation of the Log
            Operation operation = new Operation();
            operation.setType("Login");
            operation.setDate(new Date().toString());
            operation.setDescription(String.format("User %s logged", user.getUsername()));
            logService.createLog("Auth", operation, user);

            return getResponseEntity(user, request);
        }
        throw new BadCredentialsException(invalidToken);
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal User user, @RequestBody ChangePassDTO dto, @PathVariable String id ) {
        if (!user.getId().equals(id)) {
            return ResponseEntity.badRequest().body("invalid user");
        } else if (!passwordEncoder.matches(dto.getActualPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("invalid password");
        } else if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("passwords do not match");
        } else {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            userRepository.save(user);

            // Creation of the Log
            Operation operation = new Operation();
            operation.setType("Update");
            operation.setDate(new Date().toString());
            operation.setDescription(String.format("User %s changed password", user.getUsername()));
            logService.createLog("User", operation, user);

            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().build();
    }
}