package ru.itis.master.party.dormdeals.security.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Jwt {
    private String token;
    private Duration expiration;
}
