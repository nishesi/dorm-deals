package ru.itis.master.party.dormdeals.models.order;

import jakarta.persistence.*;
import lombok.*;
import ru.itis.master.party.dormdeals.models.Product;

@Entity
@Table(name = "orders_products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Product product;

    @JoinColumn(nullable = false)
    private float price;

    @JoinColumn(nullable = false)
    private Integer count;
}
