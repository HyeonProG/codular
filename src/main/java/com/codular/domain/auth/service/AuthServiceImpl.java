package com.codular.domain.auth.service;

import com.codular.common.exception.BaseException;
import com.codular.common.response.BaseResponseStatus;
import com.codular.domain.auth.dto.request.UserSignInRequestDto;
import com.codular.domain.auth.dto.response.UserSignInResponseDto;
import com.codular.domain.auth.repository.RefreshTokenRepository;
import com.codular.domain.auth.util.JwtUtil;
import com.codular.domain.user.User;
import com.codular.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserSignInResponseDto signIn(UserSignInRequestDto userSignInRequestDto) {
        User user = userRepository.findByEmail(userSignInRequestDto.getEmail())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_MATCH_EMAIL_OR_PASSWORD));

        if (!passwordEncoder.matches(userSignInRequestDto.getPassword(), user.getPassword())) {
            throw new BaseException(BaseResponseStatus.NO_MATCH_EMAIL_OR_PASSWORD);
        }

        // 토큰 발급
        String accessToken = jwtUtil.issueAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtUtil.issueRefreshToken(user.getId(), user.getRole().name());

        // redis 저장
        refreshTokenRepository.save(user.getId(), refreshToken, Duration.ofDays(jwtUtil.getRefreshExpDays()).getSeconds());

        return new UserSignInResponseDto(user.getId(), accessToken, refreshToken);
    }

    @Override
    public UserSignInResponseDto reissue(String refreshToken) {
        Jws<Claims> jws = jwtUtil.parseRefresh(refreshToken);
        Long userId = jwtUtil.getUserId(jws.getBody());

        // redis에 저장된 토큰과 일치 여부 확인
        String stored = refreshTokenRepository.find(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.UNAUTHORIZED_REFRESH_TOKEN));
        if (!stored.equals(refreshToken)) {
            throw new BaseException(BaseResponseStatus.NO_MATCH_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER));

        // accessToken 재발급
        String newAccess = jwtUtil.issueAccessToken(user.getId(), user.getRole().name());
        String newRefresh = jwtUtil.issueRefreshToken(user.getId(), user.getRole().name());

        refreshTokenRepository.save(user.getId(), newRefresh, Duration.ofDays(jwtUtil.getRefreshExpDays()).getSeconds());

        return new UserSignInResponseDto(user.getId(), newAccess, newRefresh);
    }

    @Override
    public void logout(Long userId) {
        refreshTokenRepository.delete(userId);
    }
}
