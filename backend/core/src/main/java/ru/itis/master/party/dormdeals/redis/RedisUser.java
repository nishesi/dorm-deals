package ru.itis.master.party.dormdeals.redis;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
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
