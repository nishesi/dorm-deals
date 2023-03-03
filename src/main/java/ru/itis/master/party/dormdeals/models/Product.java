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
        DELETED
    };
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer price;
    private Integer count_in_storage;
    private UUID uuid_photos;
//    @Id
//    private Long id_shop;
    @Enumerated(value = EnumType.STRING)
    private State state;
}

