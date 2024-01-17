package ru.itis.master.party.dormdeals.models.jpa;


import jakarta.persistence.*;
import lombok.*;
import ru.itis.master.party.dormdeals.models.AbstractEntity;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "carts")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @Builder.Default
    @ToString.Exclude
    private List<CartProduct> productsInCart = new ArrayList<>();


    public void addProductToCart(CartProduct cartProduct) {
        cartProduct.setCart(this);
        productsInCart.add(cartProduct);
    }
}
