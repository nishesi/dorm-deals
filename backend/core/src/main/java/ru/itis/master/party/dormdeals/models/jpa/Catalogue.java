package ru.itis.master.party.dormdeals.models.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import ru.itis.master.party.dormdeals.models.AbstractEntity;

@Entity
@Table(name = "catalogue")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Catalogue extends AbstractEntity {

    @Id
    @Column(name = "catalogue_id")
    private Long id;

    private String name;

    @Column(name = "parent_id")
    private Long parentId;

    private String url;

    private String image;
}
