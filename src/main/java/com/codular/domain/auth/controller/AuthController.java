package com.codular.domain.auth.controller;

import com.codular.common.exception.BaseException;
import com.codular.common.response.BaseResponseEntity;
import com.codular.common.response.BaseResponseStatus;
import com.codular.domain.auth.dto.request.TokenReissueRequestDto;
import com.codular.domain.auth.dto.request.UserSignInRequestDto;
import com.codular.domain.auth.dto.response.UserSignInResponseDto;
import com.codular.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인 API", description = "이메일, 비밀번호를 입력받아 AccessToken, RefreshToken 발급", tags = {"Auth-Service"})
    @PostMapping("/sign-in")
    public BaseResponseEntity<UserSignInResponseDto> signIn(@Valid @RequestBody UserSignInRequestDto userSignInRequestDto) {
        return new BaseResponseEntity<>(authService.signIn(userSignInRequestDto));
    }

    @Operation(summary = "로그아웃 API", description = "userId를 받아 로그아웃", tags = {"Auth-Service"})
    @PostMapping("/logout/{userId}")
    public BaseResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION)String authHeader) {
        authService.logout(authHeader);
        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "토큰 재발급", description = "만료된 AccessToken을 RefreshToken으로 발급", tags = {"Auth-Service"})
    @PostMapping("/refresh")
    public BaseResponseEntity<UserSignInResponseDto> reissue(@Valid @RequestBody TokenReissueRequestDto tokenReissueRequestDto) {
        return new BaseResponseEntity<>(authService.reissue(tokenReissueRequestDto.getUserId(), tokenReissueRequestDto.getRefreshToken()));
    }


}
