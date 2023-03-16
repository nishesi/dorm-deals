package ru.itis.master.party.dormdeals.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Favourites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //TODO: навесить нужны аннотации, чтобы создать связи
    private Long productId;
    private Long userId;

}
