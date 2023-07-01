package ru.itis.master.party.dormdeals.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    private String category;

    @Column(columnDefinition = "numeric(7, 2)", nullable = false)
    private float price;

    @Column(columnDefinition = "smallint check (count_in_storage >= 0)", nullable = false)
    private int countInStorage;

    @Enumerated(value = EnumType.STRING)
    private State state;
    @Column(columnDefinition = "float check (rating >= 0 and rating <= 5)")
    private float rating;

    @Basic
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private ArrayList<String> resources = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @Access(AccessType.PROPERTY)
    @EqualsAndHashCode.Exclude
    private Shop shop;

    @ManyToMany
    @JoinTable(name = "favorites",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Review> reviews = new ArrayList<>();

    public enum State {
        ACTIVE,
        HIDDEN,
        DELETED
    }
}

