package com.codular.domain.user.controller;

import com.codular.common.response.BaseResponseEntity;
import com.codular.common.response.BaseResponseStatus;
import com.codular.domain.user.dto.request.UserSignUpRequestDto;
import com.codular.domain.user.dto.response.UserSignUpResponseDto;
import com.codular.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 API", description = "이메일, 비밀번호, 닉네임을 받아 회원가입을 진행합니다.", tags = {"User-Service"})
    @PostMapping("/sign-up")
    public BaseResponseEntity<UserSignUpResponseDto> signUp(@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        userService.signUp(userSignUpRequestDto);
        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS);
    }

}
