package ru.itis.master.party.dormdeals.models;


import jakarta.persistence.*;

@Entity
public class Favourites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //TODO: навесить нужны аннотации, чтобы создать связи
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;
    private Integer count;

}
