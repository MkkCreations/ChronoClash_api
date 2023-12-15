package iut.chronoclash.chronoclash_api.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    String id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User owner;

    public void setId(String id) {
        if (id != null)
            this.id = id;
    }
}