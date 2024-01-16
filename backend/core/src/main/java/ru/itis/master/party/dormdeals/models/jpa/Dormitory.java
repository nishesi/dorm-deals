package ru.itis.master.party.dormdeals.models.jpa;

import jakarta.persistence.*;
import lombok.*;
import ru.itis.master.party.dormdeals.models.AbstractEntity;

@Entity
@Table(name = "dormitories")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Dormitory extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String address;
}
