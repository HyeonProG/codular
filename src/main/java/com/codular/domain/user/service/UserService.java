package com.codular.domain.user.service;

import com.codular.domain.user.dto.request.UpdateNicknameRequestDto;
import com.codular.domain.user.dto.request.UpdatePasswordRequestDto;
import com.codular.domain.user.dto.response.UserMeResponseDto;
import com.codular.domain.user.dto.response.UserMyPageResponseDto;

public interface UserService {

    // 헤더 유저 조회
    UserMeResponseDto getMe(Long userId);

    // 마이페이지 유저 조회
    UserMyPageResponseDto getUserMyPage(Long userId);

    // 닉네임 업데이트
    void updateNickname(Long userId, UpdateNicknameRequestDto updateNicknameRequestDto);

    // 비밀번호 업데이트
    void updatePassword(Long userId, UpdatePasswordRequestDto updatePasswordRequestDto);

}
