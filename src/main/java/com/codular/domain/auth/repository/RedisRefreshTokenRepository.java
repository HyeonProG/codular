package com.codular.domain.auth.repository;

import com.codular.common.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepository implements RefreshTokenRepository {

    private final StringRedisTemplate redis;

    private String key(Long userId) {
        return "rt:" + userId;
    }

    @Override
    public void save(Long userId, String token, long ttlSeconds) {
        String hash = HashUtil.sha256(token);
        redis.opsForValue().set(key(userId), hash, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public Optional<String> find(Long userId) {
        return Optional.ofNullable(redis.opsForValue().get(key(userId)));
    }

    @Override
    public void delete(Long userId) {
        redis.delete(key(userId));
    }

}
