package ru.itis.master.party.dormdeals.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Access(AccessType.PROPERTY)
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @Access(AccessType.PROPERTY)
    @EqualsAndHashCode.Exclude
    private Product product;

    @Column(length = 500, nullable = false)
    private String message;
    @Column(columnDefinition = "int check (score >= 1 and score <= 5)")
    private int score;
}
