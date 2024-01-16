package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import ru.itis.master.party.dormdeals.models.redis.RedisUser;

public interface UserRedisRepository extends KeyValueRepository<RedisUser, String> {

}
