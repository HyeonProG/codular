package com.codular.domain.user.service;

import com.codular.domain.user.dto.response.UserMeResponseDto;

public interface UserService {

    // 헤더 유저 닉네임 조회
    UserMeResponseDto getMe(Long userId);

}
