package iut.chronoclash.chronoclash_api.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "games")
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User owner;

    @Column
    private String enemy;

    @Column
    private int win;

    @Column
    private Date date;

    public Game() {
    }

    public Game(String id, User owner, String enemy, int win, Date date) {
        this.id = id;
        this.owner = owner;
        this.enemy = enemy;
        this.win = win;
        this.date = date;
    }

    public int isWin() {
        return win;
    }

    @Override
    public String toString() {
        return "Games{" +
                "id='" + id + '\'' +
                ", owner=" + owner +
                ", enemy=" + enemy +
                ", win=" + win +
                '}';
    }

}
