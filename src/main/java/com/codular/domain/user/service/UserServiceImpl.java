package com.codular.domain.user.service;

import com.codular.common.exception.BaseException;
import com.codular.common.response.BaseResponseStatus;
import com.codular.domain.user.User;
import com.codular.domain.user.UserRole;
import com.codular.domain.user.dto.request.UserSignUpRequestDto;
import com.codular.domain.user.dto.response.UserSignUpResponseDto;
import com.codular.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /*
     * 회원가입
     */
    @Override
    @Transactional
    public UserSignUpResponseDto signUp(UserSignUpRequestDto userSignUpRequestDto) {
        if (userMapper.findByEmail(userSignUpRequestDto.getEmail()).isPresent()) {
            throw new BaseException(BaseResponseStatus.ALREADY_EXIST_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(userSignUpRequestDto.getPassword());

        User user = User.builder()
                .email(userSignUpRequestDto.getEmail())
                .password(encodedPassword)
                .nickname(userSignUpRequestDto.getNickname())
                .role(UserRole.USER)
                .build();

        userMapper.signUp(user);
        return UserSignUpResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

}
