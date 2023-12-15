package iut.chronoclash.chronoclash_api.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}