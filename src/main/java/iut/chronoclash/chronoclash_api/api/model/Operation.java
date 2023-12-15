package iut.chronoclash.chronoclash_api.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String type;
    private String description;
    private String date;
    @ManyToOne
    @JoinColumn(name = "log_id")
    @JsonBackReference
    private Log log;

    public Operation(String id, String type, String description, String date, Log log) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.date = date;
        this.log = log;
    }

    public Operation() {

    }

    public String getId() {
        return id;
    }

    public String getType() { return type; }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public Log getLog() {
        return log;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public void setType(String type) { this.type = type; }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", log=" + log +
                '}';
    }
}
