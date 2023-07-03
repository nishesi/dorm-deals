package ru.itis.master.party.dormdeals.security.service;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Jwt {
    private String token;
    private Duration expiration;
}
