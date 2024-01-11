package iut.chronoclash.chronoclash_api.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameDTO {
    private String owner;
    private String enemy;
    private boolean win;
    private int xp;
}
