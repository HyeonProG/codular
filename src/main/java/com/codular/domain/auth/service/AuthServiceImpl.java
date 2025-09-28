package com.codular.domain.auth.service;

import com.codular.auth.util.JwtUtil;
import com.codular.common.exception.BaseException;
import com.codular.common.response.BaseResponseStatus;
import com.codular.common.util.HashUtil;
import com.codular.domain.auth.dto.request.UserSignInRequestDto;
import com.codular.domain.auth.dto.response.UserSignInResponseDto;
import com.codular.domain.auth.repository.RefreshTokenRepository;
import com.codular.domain.user.User;
import com.codular.domain.user.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    /*
     * 로그인
     */
    @Override
    public UserSignInResponseDto signIn(UserSignInRequestDto userSignInRequestDto) {
        User user = userMapper.findByEmail(userSignInRequestDto.getEmail())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_MATCH_EMAIL_OR_PASSWORD));

        if (!passwordEncoder.matches(userSignInRequestDto.getPassword(), user.getPassword())) {
            throw new BaseException(BaseResponseStatus.NO_MATCH_EMAIL_OR_PASSWORD);
        }

        String accessToken = jwtUtil.issueAccess(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.issueRefresh(user.getId(), user.getEmail(), user.getRole().name());

        long ttlSeconds = Duration.ofDays(jwtUtil.getRefreshExpDays()).toSeconds();
        refreshTokenRepository.save(user.getId(), refreshToken, ttlSeconds);

        return UserSignInResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    @Override
    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BaseException(BaseResponseStatus.NO_HEADER);
        }
        String accessToken = authHeader.substring(7);

        // Access 토큰 검증 및 userId 추출
        Jws<Claims> jws = jwtUtil.parseAccess(accessToken);
        Claims claims = jws.getBody();
        Long userId = jwtUtil.getUserId(claims);

        refreshTokenRepository.delete(userId);
    }

    @Override
    public UserSignInResponseDto reissue(Long userId, String refreshToken) {
        // 토큰 만료 검증
        Jws<Claims> jws = jwtUtil.parseRefresh(refreshToken);
        Claims claims = jws.getBody();

        // 토큰 subject 일치 검증
        Long subUserId = jwtUtil.getUserId(claims);
        if (!userId.equals(subUserId)) {
            throw new BaseException(BaseResponseStatus.NO_MATCH_TOKEN);
        }

        // 저장 해시와 제공 토큰 해시 비교
        String savedHash = refreshTokenRepository.find(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_REFRESH_TOKEN));
        String providedHash = HashUtil.sha256(refreshToken);
        if (!savedHash.equals(providedHash)) {
            throw new BaseException(BaseResponseStatus.UNAUTHORIZED_REFRESH_TOKEN);
        }

        // 최신 사용자 정보 조회(email 변경 반영용)
        User user = userMapper.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER));

        // 새 Access 발급
        String newAccess = jwtUtil.issueAccess(user.getId(), user.getEmail(), user.getRole().name());

        // refresh 갱신 + redis 갱신
        String newRefresh = jwtUtil.issueRefresh(user.getId(), user.getEmail(), user.getRole().name());
        long ttlSeconds = Duration.ofDays(jwtUtil.getRefreshExpDays()).toSeconds();
        refreshTokenRepository.save(user.getId(), newRefresh, ttlSeconds);

        return UserSignInResponseDto.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .build();
    }
}
