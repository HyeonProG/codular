package com.codular.domain.user.service;

import com.codular.common.exception.BaseException;
import com.codular.common.response.BaseResponseStatus;
import com.codular.domain.user.User;
import com.codular.domain.user.dto.response.UserMeResponseDto;
import com.codular.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserMeResponseDto getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER));
        return new UserMeResponseDto(user.getNickname(), user.getProfileUrl() != null ? user.getProfileUrl() : "/images/default-profile.jpeg");
    }
}
