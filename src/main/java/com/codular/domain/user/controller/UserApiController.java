package com.codular.domain.user.controller;

import com.codular.common.exception.BaseException;
import com.codular.common.response.BaseResponseEntity;
import com.codular.common.response.BaseResponseStatus;
import com.codular.domain.user.dto.response.UserMeResponseDto;
import com.codular.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserApiController {

    private final UserService userService;

    @Operation(summary = "사용자 닉네임 조회 API", description = "헤더에 필요한 사용자 닉네임 조회", tags = {"User-Service"})
    @GetMapping("/me")
    public BaseResponseEntity<UserMeResponseDto> getMe(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return new BaseResponseEntity<>(BaseResponseStatus.UNAUTHORIZED);
        }
        Long userId = (Long) authentication.getPrincipal();

        return new BaseResponseEntity<>(userService.getMe(userId));
    }

}
