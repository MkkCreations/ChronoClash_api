package iut.chronoclash.chronoclash_api.api.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class UsersDTO implements Serializable {
    private String id;
    private String username;
    private String name;
    private String email;
    @Size(max = 1000000)
    private byte[] image;
    private int level;
}
