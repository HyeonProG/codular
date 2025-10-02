package com.codular.domain.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisPasswordResetTokenRepository implements PasswordResetTokenRepository {

    private final StringRedisTemplate stringRedisTemplate;

    private String key(String email) {
        return "auth:pwreset:" + email;
    }

    @Override
    public void save(String email, String token, long ttlSeconds) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key(email), token, Duration.ofSeconds(ttlSeconds));
    }

    @Override
    public Optional<String> find(String email) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(key(email)));
    }

    @Override
    public void delete(String email) {
        stringRedisTemplate.delete(key(email));
    }
}
