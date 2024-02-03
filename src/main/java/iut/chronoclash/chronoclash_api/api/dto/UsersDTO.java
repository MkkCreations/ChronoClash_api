package iut.chronoclash.chronoclash_api.api.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersDTO {
    private String id;
    private String username;
    private String name;
    private String email;
    @Size(max = 1000000)
    private byte[] image;
    private int level;

    public UsersDTO() {}

    public UsersDTO(String id, String username, String name, String email, byte[] image, int level) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.image = image;
        this.level = level;
    }
}
