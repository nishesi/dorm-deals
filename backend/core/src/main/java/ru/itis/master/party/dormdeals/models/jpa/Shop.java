package ru.itis.master.party.dormdeals.models.jpa;

import jakarta.persistence.*;
import lombok.*;
import ru.itis.master.party.dormdeals.models.AbstractEntity;

import java.util.List;

@Entity
@Table(name = "shops")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Shop extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id")
    private Long id;

    @Column(length = 100, unique = true, nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToMany
    @ToString.Exclude
    private List<Dormitory> dormitories;

    @Column(columnDefinition = "numeric(2, 1)", nullable = false)
    private float rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @Access(AccessType.PROPERTY)
    @ToString.Exclude
    private User owner;
}
