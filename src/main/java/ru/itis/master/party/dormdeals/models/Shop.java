package ru.itis.master.party.dormdeals.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, unique = true, nullable = false)
    private String name;
    @Column(length = 1000)
    private String description;
    @ManyToMany
    private List<Dormitory> dormitories;
    @Column(columnDefinition = "numeric(2, 1)")
    private double rating;
    private String resource;
    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
