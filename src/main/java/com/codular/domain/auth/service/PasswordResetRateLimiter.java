package com.codular.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PasswordResetRateLimiter {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX_COOL = "pwreset:cool:";
    private static final String KEY_PREFIX_COUNT = "pwreset:count:";

    private static final long COOLDOWN_SECONDS = 60;
    private static final int DAILY_LIMIT = 5;

    public boolean allow(String email) {
        String coolKey = KEY_PREFIX_COOL + email;
        if (stringRedisTemplate.hasKey(coolKey)) {
            return false;
        }

        String countKey = KEY_PREFIX_COUNT + email + ":" + LocalDate.now();
        Long count = stringRedisTemplate.opsForValue().increment(countKey);
        if (count != null && count == 1L) {
            stringRedisTemplate.expire(countKey, Duration.ofDays(1));
        }
        if (count != null && count > DAILY_LIMIT) {
            return false;
        }

        stringRedisTemplate.opsForValue().set(coolKey, "1", Duration.ofSeconds(COOLDOWN_SECONDS));
        return true;
    }

}
