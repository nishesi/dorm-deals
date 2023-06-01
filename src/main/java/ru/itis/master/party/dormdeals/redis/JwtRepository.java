package ru.itis.master.party.dormdeals.redis;

import ru.itis.master.party.dormdeals.security.service.Jwt;

import java.util.List;

public interface JwtRepository {
    boolean existsInBlackList(String jwt);

    void saveAllToBlackList(List<Jwt> refreshTokens);
}
