package ru.itis.master.party.dormdeals.models.jpa.order;

import jakarta.persistence.*;
import lombok.*;
import ru.itis.master.party.dormdeals.models.AbstractEntity;
import ru.itis.master.party.dormdeals.models.jpa.Product;

@Entity
@Table(name = "orders_products")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private Product product;

    @JoinColumn(nullable = false)
    private float price;

    @JoinColumn(nullable = false)
    private int count;
}
