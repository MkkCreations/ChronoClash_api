package iut.chronoclash.chronoclash_api.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "friends")
@Getter
@Setter
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User owner;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    @JsonIgnoreProperties({"password", "role", "logs", "friends"})
    private User friend;

    @Column
    private boolean accepted;

    @Column
    private boolean blocked;

    @Column
    private Date date;

    public Friend(User user, User friend, boolean accepted, boolean blocked, Date date) {
        this.owner = user;
        this.friend = friend;
        this.accepted = accepted;
        this.blocked = blocked;
        this.date = date;
    }

    public Friend() {}
}
