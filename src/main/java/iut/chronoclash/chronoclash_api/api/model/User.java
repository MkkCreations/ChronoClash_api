package iut.chronoclash.chronoclash_api.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    String id;

    @NonNull
    @Column
    private String name;

    @NonNull
    @Column(unique = true)
    private String username;

    @NonNull
    @Column(unique = true)
    private String email;

    @JsonIgnore
    @NonNull
    @Column
    private String password;

    @Nullable
    @Column(columnDefinition="BLOB")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;

    @Nullable
    @Column
    private String role;

    @Nullable
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<RefreshToken> refreshToken;

    @Nullable
    @JsonManagedReference
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private List<Log> logs;

    @JsonManagedReference
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private Level level;

    @Nullable
    @JsonManagedReference
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private List<Game> games;

    @Nullable
    @JsonManagedReference
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private List<Friend> friends = new ArrayList<>();

    public User() {}

    public User(String name, String username, String email, String password, byte[] image, String role, List<Log> logs, Level level, List<Game> games, List<Friend> friends) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
        this.role = role;
        this.logs = logs;
        this.level = level;
        this.games = games;
        this.friends = friends;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return "ADMIN".equals(role) ? Collections.singletonList((GrantedAuthority) () -> "ROLE_ADMIN") : Collections.singletonList((GrantedAuthority) () -> "ROLE_USER");
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}