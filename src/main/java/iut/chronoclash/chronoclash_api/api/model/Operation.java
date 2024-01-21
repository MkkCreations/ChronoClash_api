package iut.chronoclash.chronoclash_api.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "operations")
@Getter
@Setter
public class Operation {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String type;
    private String description;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "log_id")
    @JsonBackReference
    private Log log;

    public Operation(String id, String type, String description, Date date, Log log) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.date = date;
        this.log = log;
    }

    public Operation() {

    }

    @Override
    public String toString() {
        return "Operation{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date.toString() + '\'' +
                ", log=" + log +
                '}';
    }
}
