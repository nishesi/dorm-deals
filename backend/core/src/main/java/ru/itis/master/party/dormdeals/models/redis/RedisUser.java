package ru.itis.master.party.dormdeals.models.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("user")
public class RedisUser {
    @Id
    private String id;

    private List<String> refreshTokens;

    private List<String> accessTokens;
}
