package ru.itis.master.party.dormdeals.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.regex.qual.Regex;

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
    private String name;

    private String description;
    private String category;
    private Integer price;

    //TODO: реализовать проверку на ноль после заказа, если ноль то переводить состояние в "NOT_AVAILABLE"
    private Integer countInStorage;
    private UUID uuidOfPhotos;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Enumerated(value = EnumType.STRING)
    private State state;
}

