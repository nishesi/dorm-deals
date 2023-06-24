package ru.itis.master.party.dormdeals.models.order;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.models.User;

import java.time.ZonedDateTime;
import java.util.List;


@NamedEntityGraph(name = "order-user-shop",
        attributeNodes = {
                @NamedAttributeNode(value = "customer"),
                @NamedAttributeNode(value = "shop")
        }
)
@NamedEntityGraph(
        name = "order-product",
        attributeNodes = {
                @NamedAttributeNode(value = "products", subgraph = "orderProduct-product")
        },
        subgraphs = {
                @NamedSubgraph(name = "orderProduct-product", attributeNodes = {
                        @NamedAttributeNode("product")
                })
        }
)
@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    @DateTimeFormat(pattern = "dd-MM-yyyy hh-mm-ss XX")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime addedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Column
    private float price;

    @Enumerated(value = EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL})
    private List<OrderProduct> products;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL})
    private List<OrderMessage> messages;

    @RequiredArgsConstructor
    public enum State {
        CANCELLED(-1),
        IN_PROCESSING(1),
        CONFIRMED(2),
        DELIVERED(3);

        private final int index;

        public int index() {
            return index;
        }
    }
}
