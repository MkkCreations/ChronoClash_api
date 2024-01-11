package iut.chronoclash.chronoclash_api.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "levels")
@Getter
@Setter
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private String id;

    @Column
    private int level;

    @Column
    private int xp;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User owner;

    public Level() {
    }

    public Level(String id, int level, int xp, User owner) {
        this.id = id;
        this.level = level;
        this.xp = xp;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Level{" +
                "id='" + id + '\'' +
                ", level=" + level +
                ", xp=" + xp +
                ", owner=" + owner +
                '}';
    }
}
