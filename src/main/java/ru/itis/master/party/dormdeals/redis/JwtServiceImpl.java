package ru.itis.master.party.dormdeals.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.repositories.JwtRepository;
import ru.itis.master.party.dormdeals.services.JwtService;
import ru.itis.master.party.dormdeals.security.service.JwtUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtUtil jwtUtil;
    private final JwtRepository jwtRepository;
    private final RedisUserRepository redisUserRepository;

    @Override
    public void addTokensToUser(String userId, String accessToken, String refreshToken) {
        Optional<RedisUser> redisUserOptional = redisUserRepository.findById(userId);

        redisUserOptional.ifPresentOrElse(
                redisUser -> {
                    if (redisUser.getRefreshTokens() != null)
                        redisUser.getRefreshTokens().add(refreshToken);
                    else redisUser.setRefreshTokens(List.of(refreshToken));

                    if (redisUser.getAccessTokens() != null)
                        redisUser.getAccessTokens().add(accessToken);
                    else redisUser.setAccessTokens(List.of(accessToken));

                    redisUserRepository.save(redisUser);
                },
                () -> {
                    RedisUser redisUser = new RedisUser(
                            userId,
                            List.of(accessToken),
                            List.of(refreshToken)
                    );
                    redisUserRepository.save(redisUser);
                });
    }

    @Override
    public boolean isBlockedAccessToken(String jwt) {
        return jwtRepository.existsInBlackList(jwt);
    }

    @Override
    public boolean isBlockedRefreshToken(String token) {
        return jwtRepository.existsInBlackList(token);
    }

    @Override
    public void blockAllTokensForUser(String userId) {
        Optional<RedisUser> redisUserOptional = redisUserRepository.findById(userId);

        redisUserOptional.ifPresent(redisUserRepository::delete);

        redisUserOptional.ifPresent(redisUser -> {
            if (redisUser.getRefreshTokens() != null)
                jwtRepository.saveAllToBlackList(redisUser.getRefreshTokens().stream()
                        .map(jwtUtil::from)
                        .toList());

            if (redisUser.getAccessTokens() != null)
                jwtRepository.saveAllToBlackList(redisUser.getAccessTokens().stream()
                        .map(jwtUtil::from)
                        .toList());
        });
    }
}
