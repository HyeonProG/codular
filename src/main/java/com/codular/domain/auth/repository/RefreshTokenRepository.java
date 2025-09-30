package com.codular.domain.auth.repository;

import java.util.Optional;

public interface RefreshTokenRepository {

    // 해시 저장 + TTL
    void save(Long userId, String refreshToken, long ttlSeconds);

    // 저장된 해시 반환
    Optional<String> find(Long userId);

    // 로그아웃
    void delete(Long userId);

}
