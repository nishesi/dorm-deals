package ru.itis.master.party.dormdeals.services;

public interface EmailService {
    void confirmAccount(String email, String code);
}
