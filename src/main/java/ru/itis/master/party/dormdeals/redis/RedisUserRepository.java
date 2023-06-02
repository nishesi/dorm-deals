package ru.itis.master.party.dormdeals.redis;

import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface RedisUserRepository extends KeyValueRepository<RedisUser, String> {

}
