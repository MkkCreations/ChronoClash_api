package iut.chronoclash.chronoclash_api.api.dto;

import iut.chronoclash.chronoclash_api.api.model.Level;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class FriendNotificationDTO {
    private String id;
    private String username;
    private String name;
    private byte[] image;
    private Level level;
    private Date date;
}
