package com.codular.domain.auth.service;

import com.codular.domain.auth.dto.request.UserSignInRequestDto;
import com.codular.domain.auth.dto.request.UserSignUpRequestDto;
import com.codular.domain.auth.dto.response.UserSignInResponseDto;

public interface AuthService {

    /*
     * 회원가입
     */
    void signUp(UserSignUpRequestDto userSignUpRequestDto);

    /*
     * 로그인
     * access/refreshToken을 http-only 쿠키로 받음
     */
    UserSignInResponseDto signIn(UserSignInRequestDto userSignInRequestDto);

    /*
     * 토큰 재발급
     * 쿠키 검증 후 access/refreshToken 갱신
     */
    UserSignInResponseDto reissue(String refreshToken);

    /*
     * 로그아웃
     * 쿠키 무효화
     */
    void logout(Long userId);

}
