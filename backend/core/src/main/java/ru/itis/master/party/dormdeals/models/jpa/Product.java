package ru.itis.master.party.dormdeals.models.jpa;

import jakarta.persistence.*;
import lombok.*;
import ru.itis.master.party.dormdeals.models.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    private String category;

    private String brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Catalogue catalogue;

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
    private ArrayList<String> resources = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @Access(AccessType.PROPERTY)
    @ToString.Exclude
    private Shop shop;

    @ManyToMany
    @JoinTable(name = "favorites",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();

    public enum State {
        ACTIVE,
        HIDDEN,
        DELETED
    }
}

