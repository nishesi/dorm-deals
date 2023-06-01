package ru.itis.master.party.dormdeals.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import ru.itis.master.party.dormdeals.security.service.Jwt;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JwtRepositoryImpl implements JwtRepository {
    private final RedisTemplate<String, String> template;

    @Override
    public boolean existsInBlackList(String jwt) {
        Boolean exists = template.hasKey(jwt);
        return exists != null && exists;
    }

    @Override
    public void saveAllToBlackList(List<Jwt> tokens) {
        ValueOperations<String, String> valueOps = template.opsForValue();
        tokens.forEach(token -> valueOps.set(token.getToken(), "", token.getExpiration()));
    }
}
