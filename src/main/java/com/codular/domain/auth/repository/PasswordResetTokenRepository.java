package com.codular.domain.auth.repository;

import java.util.Optional;

public interface PasswordResetTokenRepository {

    // 비밀번호 재설정 토큰 저장
    void save(String email, String token, long ttlSeconds);

    // 이메일로 토큰 조회
    Optional<String> find(String email);

    // 토큰 삭제
    void delete(String email);

}
