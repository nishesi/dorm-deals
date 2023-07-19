package ru.itis.master.party.dormdeals.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carts_products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @EqualsAndHashCode.Exclude
    private Cart cart;

    @OneToOne
    @JoinColumn(name = "product_id")
    @EqualsAndHashCode.Exclude
    private Product product;

    private Integer count;

    @Enumerated(EnumType.STRING)
    private State state;

    public enum State {
        ACTIVE,
        INACTIVE
    }
}
