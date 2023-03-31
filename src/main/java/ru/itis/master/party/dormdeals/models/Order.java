package ru.itis.master.party.dormdeals.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Entity
public class Order {
    public enum State {
        IN_PROCESSING,
        CONFIRMED,
        IN_DELIVERY,
        DELIVERED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @DateTimeFormat(pattern = "dd-MM-yyyy hh-mm-ss XX")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime orderTime;

    @Column
    private String userComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    private float price;

    @Enumerated(value = EnumType.STRING)
    private State state;
}
