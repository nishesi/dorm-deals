package ru.itis.master.party.dormdeals.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(unique = true, nullable = false)
    private String email;
    @Column(columnDefinition = "char(65)", nullable = false)
    private String hashPassword;
    @Column(length = 50, nullable = false)
    private String firstName;
    @Column(length = 50)
    private String lastName;
    @Column(length = 15)
    private String telephone;
    private String dormitory;
    @Column(columnDefinition = "char(64)")
    private String hashForConfirm;
    @Enumerated(EnumType.STRING)
    private State state;
    @Enumerated(EnumType.STRING)
    private Role role;
}
