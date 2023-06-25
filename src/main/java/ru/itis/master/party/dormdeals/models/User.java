package ru.itis.master.party.dormdeals.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "char(60)", nullable = false)
    private String hashPassword;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Column(length = 15)
    private String telephone;

    @Column(columnDefinition = "char(64)")
    private String hashForConfirm;

    private String resource;

    @Enumerated(EnumType.STRING)
    private State state;

    @Enumerated(EnumType.STRING)
    private List<Authority> authorities;

    @ManyToMany
    @JoinTable(name = "favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @JsonIgnore
    @ToString.Exclude
    private List<Product> favorites = new ArrayList<>();

    public enum State {
        NOT_CONFIRMED,
        ACTIVE,
        BANNED,
        DELETED
    }
}
