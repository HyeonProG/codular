package com.codular.domain.user.service;

import com.codular.common.exception.BaseException;
import com.codular.common.response.BaseResponseStatus;
import com.codular.domain.user.User;
import com.codular.domain.user.dto.request.UpdateNicknameRequestDto;
import com.codular.domain.user.dto.request.UpdatePasswordRequestDto;
import com.codular.domain.user.dto.response.UserMeResponseDto;
import com.codular.domain.user.dto.response.UserMyPageResponseDto;
import com.codular.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserMeResponseDto getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER));
        return new UserMeResponseDto(user.getNickname(), user.getProfileImageUrl() != null ? user.getProfileImageUrl() : "/images/default-profile.jpeg");
    }

    @Override
    public UserMyPageResponseDto getUserMyPage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER));

        LocalDateTime createdAt = user.getCreatedAt();
        String createdAtString = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return UserMyPageResponseDto.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .createdAt(createdAtString)
                .build();
    }

    @Override
    @Transactional
    public void updateNickname(Long userId, UpdateNicknameRequestDto updateNicknameRequestDto) {
        String newNickname = updateNicknameRequestDto.getNickname();

        if (userRepository.existsByNickname(newNickname)) {
            throw new BaseException(BaseResponseStatus.ALREADY_EXIST_NICKNAME);
        }

        userRepository.updateNickname(userId, newNickname);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequestDto updatePasswordRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER));

        if (!passwordEncoder.matches(updatePasswordRequestDto.getCurrentPassword(), user.getPassword())) {
            throw new BaseException(BaseResponseStatus.INVALID_PASSWORD);
        }

        if (!updatePasswordRequestDto.getNewPassword().equals(updatePasswordRequestDto.getConfirmPassword())) {
            throw new BaseException(BaseResponseStatus.INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(updatePasswordRequestDto.getNewPassword()));
        userRepository.updatePassword(userId, updatePasswordRequestDto.getNewPassword());
    }
}
