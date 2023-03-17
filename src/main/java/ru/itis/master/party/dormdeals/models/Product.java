package ru.itis.master.party.dormdeals.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    public enum State {
        ACTIVE,
        NOT_AVAILABLE,
        DELETED
    }

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

    //TODO: реализовать проверку на ноль после заказа, если ноль то переводить состояние в "NOT_AVAILABLE"
    @Column(columnDefinition = "smallint check (count_in_storage >= 0)", nullable = false)
    private short countInStorage;
    private UUID uuidOfPhotos;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Enumerated(value = EnumType.STRING)
    private State state;
}

