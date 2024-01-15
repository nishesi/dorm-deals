package ru.itis.master.party.dormdeals.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "catalogue")
public class Catalogue {

    @Id
    @Column(name = "catalogue_id")
    private Long id;

    private String name;

    @Column(name = "parent_id")
    private Long parentId;

    private String url;

    private String image;
}
