package com.codular.domain.user.service;

import com.codular.domain.user.dto.request.UserSignUpRequestDto;
import com.codular.domain.user.dto.response.UserSignUpResponseDto;

public interface UserService {

    // 회원가입
    UserSignUpResponseDto signUp(UserSignUpRequestDto userSignUpRequestDto);

}
