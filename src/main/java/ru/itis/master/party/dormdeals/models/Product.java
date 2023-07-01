package ru.itis.master.party.dormdeals.models;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @Access(AccessType.PROPERTY)
    @EqualsAndHashCode.Exclude
    private Shop shop;

    @Basic
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private ArrayList<String> resources = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "favorites",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> users = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private State state;

    public enum State {
        ACTIVE,
        NOT_AVAILABLE,
        DELETED
    }

//    @Converter
//    public class StringListConverter implements AttributeConverter<List<String>, String> {
//        private static final String SPLIT_CHAR = ";";
//
//        @Override
//        public String convertToDatabaseColumn(List<String> stringList) {
//            return stringList != null ? String.join(SPLIT_CHAR, stringList) : "";
//        }
//
//        @Override
//        public List<String> convertToEntityAttribute(String string) {
//            return string != null
//                    ? new ArrayList<>(List.of(string.split(SPLIT_CHAR)))
//                    : new ArrayList<>();
//        }
//    }
}

