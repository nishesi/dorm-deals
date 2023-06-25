package ru.itis.master.party.dormdeals.models.order;

import jakarta.persistence.*;
import lombok.*;
import ru.itis.master.party.dormdeals.models.User;

import java.time.ZonedDateTime;

@Entity
@Table(name = "orders_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @Access(AccessType.PROPERTY)
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private ZonedDateTime addedDate;
}
