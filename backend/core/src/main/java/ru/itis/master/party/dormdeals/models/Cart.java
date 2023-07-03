package ru.itis.master.party.dormdeals.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "carts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private User user;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @EqualsAndHashCode.Exclude
    private List<CartProduct> productsInCart = new ArrayList<>();


    public void addProductToCart(CartProduct cartProduct) {
        cartProduct.setCart(this);
        productsInCart.add(cartProduct);
    }

}
