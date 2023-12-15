package iut.chronoclash.chronoclash_api.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDTO {
    @NotBlank
    @Size(min = 3, max = 30)
    private String username;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 60)
    @Email
    private String email;
    @NotBlank
    @Size(min = 6)
    private String password;
    private String image;
}