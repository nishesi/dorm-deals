package ru.itis.master.party.dormdeals.models.jpa;


import jakarta.persistence.*;
import lombok.*;
import ru.itis.master.party.dormdeals.models.AbstractEntity;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Review extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Access(AccessType.PROPERTY)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @Access(AccessType.PROPERTY)
    @ToString.Exclude
    private Product product;

    @Column(length = 500, nullable = false)
    private String message;

    @Column(columnDefinition = "int check (score >= 1 and score <= 5)")
    private int score;
}
