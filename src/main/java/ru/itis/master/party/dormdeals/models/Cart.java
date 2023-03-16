package ru.itis.master.party.dormdeals.models;


import jakarta.persistence.*;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO: навесить нужны аннотации, чтобы создать связи
    private Long userId;
    private Long productId;
    private Long count;
    private Long sumPricesOfProducts;
}
