package ru.itis.master.party.dormdeals.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "shops")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, unique = true, nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToMany
    @EqualsAndHashCode.Exclude
    private List<Dormitory> dormitories;

    @Column(columnDefinition = "numeric(2, 1)", nullable = false)
    private double rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @Access(AccessType.PROPERTY)
    @EqualsAndHashCode.Exclude
    private User owner;
}
