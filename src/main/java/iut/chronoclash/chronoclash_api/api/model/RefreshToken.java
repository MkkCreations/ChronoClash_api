package iut.chronoclash.chronoclash_api.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    @NotNull
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User owner;

    @Column
    private String requestIp;

    @Column
    private String requestUserAgent;

    @Column
    private Date date;

    public void setId(String id) {
        if (id != null)
            this.id = id;
    }
}