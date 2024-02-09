package iut.chronoclash.chronoclash_api.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "logs")
@Getter
@Setter
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private String id;

    @Column
    private String name;

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Nullable
    @Fetch(FetchMode.JOIN)
    private List<Operation> operations;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User owner;

    public Log(String id, String name, @Nullable List<Operation> operations, User owner) {
        this.id = id;
        this.name = name;
        this.operations = operations;
        this.owner = owner;
    }

    public Log() {}
}