package ru.itis.master.party.dormdeals.models.jpa.order;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itis.master.party.dormdeals.models.AbstractEntity;
import ru.itis.master.party.dormdeals.models.jpa.Shop;
import ru.itis.master.party.dormdeals.models.jpa.User;

import java.time.ZonedDateTime;
import java.util.List;

@NamedEntityGraph(name = "order-shop",
        attributeNodes = {
                @NamedAttributeNode(value = "shop")
        }
)
@NamedEntityGraph(name = "order-customer-shop",
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
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @Access(AccessType.PROPERTY)
    @ToString.Exclude
    private User customer;

    @DateTimeFormat(pattern = "dd-MM-yyyy hh-mm-ss XX")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime addedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @ToString.Exclude
    private Shop shop;

    @Column
    private float price;

    @Enumerated(value = EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<OrderProduct> products;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL})
    @ToString.Exclude
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
