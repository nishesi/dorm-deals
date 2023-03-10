package ru.itis.master.party.dormdeals.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
public class User {
    public enum State {
        NOT_CONFIRMED,
        ACTIVE,
        DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String telephone;
    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToMany(targetEntity = Authority.class, fetch = FetchType.EAGER)
    private Collection<Authority> authorities;
}
