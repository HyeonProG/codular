package com.codular.domain.auth.service;

import com.codular.common.exception.BaseException;
import com.codular.common.response.BaseResponseStatus;
import com.codular.domain.auth.repository.EmailSender;
import com.codular.domain.auth.repository.PasswordResetTokenRepository;
import com.codular.domain.auth.util.MailSenderUtil;
import com.codular.domain.user.User;
import com.codular.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final MailSenderUtil mailSenderUtil;
    private final PasswordResetRateLimiter passwordResetRateLimiter;

    private static final Duration TOKEN_TTL = Duration.ofMinutes(5);

    @Override
    public void requestResetPassword(String email, String resetLinkBase) {
        if (!passwordResetRateLimiter.allow(email)) {
            return;
        }

        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isEmpty()) return;

        User user = optUser.get();

        // 토큰 생성 및 저장 (email -> token)
        String token = UUID.randomUUID().toString();
        tokenRepository.save(email, token, TOKEN_TTL.getSeconds());

        // 링크에 email & token 포함
        String link = resetLinkBase
                + "?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)
                + "&token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        String body = mailSenderUtil.buildBody(user.getNickname(), link);
        emailSender.send(email, "[Codular] 비밀번호 재설정 안내", body);
    }

    @Override
    public void confirmResetPassword(String email, String token, String newPassword) {
        // 이메일로 저장된 토큰 조회 및 검증
        String saved = tokenRepository.find(email)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.BAD_REQUEST, "유효하지 않거나 만료된 토큰입니다."));
        if (!saved.equals(token)) {
            throw new BaseException(BaseResponseStatus.NO_MATCH_TOKEN, "토큰이 일치하지 않습니다.");
        }

        // 사용자 조회 및 비밀번호 업데이트
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER));

        user.changePassword(passwordEncoder.encode(newPassword));
        userRepository.updatePassword(user.getId(), user.getPassword());

        // 토큰 제거(일회성)
        tokenRepository.delete(email);
    }
}
