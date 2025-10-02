package com.codular.domain.user.controller;

import com.codular.common.exception.BaseException;
import com.codular.common.response.BaseResponseEntity;
import com.codular.common.response.BaseResponseStatus;
import com.codular.domain.user.dto.request.UpdateNicknameRequestDto;
import com.codular.domain.user.dto.request.UpdatePasswordRequestDto;
import com.codular.domain.user.dto.response.UserMeResponseDto;
import com.codular.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "사용자 닉네임 변경 API", description = "요청받은 새 닉네임 검증 후 업데이트", tags = {"User-Service"})
    @PatchMapping("/nickname")
    public BaseResponseEntity<?> updateNickname(Authentication authentication, @Valid @RequestBody UpdateNicknameRequestDto updateNicknameRequestDto) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return new BaseResponseEntity<>(BaseResponseStatus.UNAUTHORIZED);
        }
        Long userId = (Long) authentication.getPrincipal();
        userService.updateNickname(userId, updateNicknameRequestDto);

        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "사용자 비밀번호 변경 API", description = "사용자의 기존 비밀번호 검증, 새로운 비밀번호 검증 후 비밀번호 변경", tags = {"User-Service"})
    @PatchMapping("/password")
    public BaseResponseEntity<?> updatePassword(Authentication authentication, @Valid @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return new BaseResponseEntity<>(BaseResponseStatus.UNAUTHORIZED);
        }
        Long userId = (Long) authentication.getPrincipal();
        userService.updatePassword(userId, updatePasswordRequestDto);

        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS);
    }

}
