package iut.chronoclash.chronoclash_api.api.rest;

import iut.chronoclash.chronoclash_api.api.dto.ErrorResponseDTO;
import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/network")
public class ConnectionREST {
    @Autowired
    RefreshTokenService refreshTokenService;

    @GetMapping("/connections")
    public ResponseEntity<?> getConnections(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("connections", refreshTokenService.getByOwner(user)));
    }

    @DeleteMapping("/disconnect/{id}")
    public ResponseEntity<?> disconnect(@AuthenticationPrincipal User user, @PathVariable String id) {
        try {
            refreshTokenService.deleteById(id);
            return getConnections(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
        }
    }
}
