package ru.itis.master.party.dormdeals.repositories;

public interface JwtService {
    void addTokensToUser(String userId, String accessToken, String refreshToken);

    boolean isBlockedAccessToken(String jwt);

    boolean isBlockedRefreshToken(String token);

    void blockAllTokensForUser(String userId);
}
