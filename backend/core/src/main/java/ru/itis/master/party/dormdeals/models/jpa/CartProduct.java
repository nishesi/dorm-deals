package ru.itis.master.party.dormdeals.models.jpa;


import jakarta.persistence.*;
import lombok.*;
import ru.itis.master.party.dormdeals.models.AbstractEntity;

@Entity
@Table(name = "carts_products")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @ToString.Exclude
    private Cart cart;

    @OneToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    private Product product;

    private Integer count;

    @Enumerated(EnumType.STRING)
    private State state;

    public enum State {
        ACTIVE,
        INACTIVE
    }
}
