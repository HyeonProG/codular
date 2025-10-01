package com.codular.domain.auth.controller;

import com.codular.common.exception.BaseException;
import com.codular.common.response.BaseResponseEntity;
import com.codular.common.response.BaseResponseStatus;
import com.codular.domain.auth.dto.request.UserSignInRequestDto;
import com.codular.domain.auth.dto.request.UserSignUpRequestDto;
import com.codular.domain.auth.dto.response.UserSignInResponseDto;
import com.codular.domain.auth.service.AuthService;
import com.codular.domain.auth.util.AuthCookieManager;
import com.codular.domain.auth.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController {

    private final AuthService authService;
    private final AuthCookieManager authCookieManager;
    private final JwtUtil jwtUtil;

    @Operation(summary = "회원가입 API", description = "이메일/닉네임/비밀번호로 회원가입 성공 시 로그인 페이지로 이동", tags = {"Auth-Service"})
    @PostMapping("/sign-up")
    public BaseResponseEntity<?> signUp(@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        authService.signUp(userSignUpRequestDto);
        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS, Map.of("redirectUri", "/auth/sign-in"));
    }

    @Operation(summary = "로그인 API", description = "이메일, 비밀번호를 입력받아 AccessToken, RefreshToken 발급", tags = {"Auth-Service"})
    @PostMapping("/sign-in")
    public BaseResponseEntity<?> signIn(@Valid @RequestBody UserSignInRequestDto userSignInRequestDto, HttpServletResponse response) {
        UserSignInResponseDto result = authService.signIn(userSignInRequestDto);
        authCookieManager.addAccessToken(response, result.getAccessToken(), Duration.ofMinutes(jwtUtil.getAccessExpMin()));
        authCookieManager.addRefreshToken(response, result.getRefreshToken(), Duration.ofDays(jwtUtil.getRefreshExpDays()));

        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS, Map.of("redirectUri", "/"));
    }

    @Operation(summary = "토큰 재발급", description = "만료된 AccessToken을 RefreshToken으로 발급", tags = {"Auth-Service"})
    @PostMapping("/reissue")
    public BaseResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = authCookieManager.extractRefreshToken(request)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_REFRESH_TOKEN));

        UserSignInResponseDto result = authService.reissue(refresh);

        authCookieManager.addAccessToken(response, result.getAccessToken(), Duration.ofMinutes(jwtUtil.getAccessExpMin()));
        authCookieManager.addRefreshToken(response, result.getRefreshToken(), Duration.ofDays(jwtUtil.getRefreshExpDays()));

        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS);
    }


    @Operation(summary = "로그아웃 API", description = "로그아웃", tags = {"Auth-Service"})
    @PostMapping("/logout")
    public BaseResponseEntity<?> logout(Authentication authentication, HttpServletResponse response) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return new BaseResponseEntity<>(BaseResponseStatus.UNAUTHORIZED);
        }

        Long userId = (Long) authentication.getPrincipal();
        authService.logout(userId);
        authCookieManager.clear(response);

        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS);
    }

}
