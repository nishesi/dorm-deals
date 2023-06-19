package ru.itis.master.party.dormdeals.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<CartProduct> productsInCart = new ArrayList<>();


    public void addProductToCart(CartProduct cartProduct) {
        cartProduct.setCart(this);
        productsInCart.add(cartProduct);
    }
//    public void retainAll(Collection<Long> ids) {
//        productsInCart.removeIf(cartProduct -> !ids.contains(cartProduct.getProduct().getId()));
//    }

}
