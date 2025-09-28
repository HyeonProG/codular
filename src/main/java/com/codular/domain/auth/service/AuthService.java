package com.codular.domain.auth.service;

import com.codular.domain.auth.dto.request.UserSignInRequestDto;
import com.codular.domain.auth.dto.response.UserSignInResponseDto;

public interface AuthService {

    // 로그인
    UserSignInResponseDto signIn(UserSignInRequestDto userSignInRequestDto);

    // 로그아웃
    void logout(String authHeader);

    // 토큰 재발급
    UserSignInResponseDto reissue(Long userId, String refreshToken);

}
