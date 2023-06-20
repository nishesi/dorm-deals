package ru.itis.master.party.dormdeals.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProduct {

    public enum State {
        ACTIVE,
        INACTIVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer count;

    @Enumerated(EnumType.STRING)
    private State state;
}
