package ru.itis.master.party.dormdeals.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(name = "product-shop",
        attributeNodes = @NamedAttributeNode(value = "shop", subgraph = "shop-subgraph"),
        subgraphs = {
                @NamedSubgraph(name = "shop-subgraph", attributeNodes = {})
        }
)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private short countInStorage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ElementCollection(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<String> resources = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "favorites",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    private List<User> users = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private State state;

    public enum State {
        ACTIVE,
        NOT_AVAILABLE,
        DELETED
    }
}

