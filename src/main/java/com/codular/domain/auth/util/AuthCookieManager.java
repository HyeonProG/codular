package com.codular.domain.auth.util;

import com.codular.domain.auth.config.AuthCookieProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthCookieManager {

    private final AuthCookieProperties properties;

    // AccessToken 저장
    public void addAccessToken(HttpServletResponse response, String token, Duration maxAge) {
        addCookie(response, properties.getAccessName(), token, maxAge);
    }

    // RefreshToken 저장
    public void addRefreshToken(HttpServletResponse response, String token, Duration maxAge) {
        addCookie(response, properties.getRefreshName(), token, maxAge);
    }

    // AccessToken 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return extractCookie(request, properties.getAccessName());
    }

    // RefreshToken 추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return extractCookie(request, properties.getRefreshName());
    }

    // 쿠키 제거 (로그아웃용)
    public void clear(HttpServletResponse response) {
        expireCookie(response, properties.getAccessName());
        expireCookie(response, properties.getRefreshName());
    }

    // 내부 util 메서드
    private void addCookie(HttpServletResponse response, String name, String value, Duration maxAge) {
        if (!StringUtils.hasText(value)) return;

        ResponseCookie.ResponseCookieBuilder builder = baseBuilder(name, value)
                .maxAge(maxAge);

        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }

    private void expireCookie(HttpServletResponse response, String name) {
        ResponseCookie.ResponseCookieBuilder builder = baseBuilder(name, "")
                .maxAge(Duration.ZERO);

        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }

    private ResponseCookie.ResponseCookieBuilder baseBuilder(String name, String value) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(properties.isSecure())
                .path(properties.getPath())
                .sameSite(properties.getSameSite());

        if (StringUtils.hasText(properties.getDomain())) {
            builder.domain(properties.getDomain());
        }
        return builder;
    }

    private Optional<String> extractCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();

        return Arrays.stream(cookies)
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .filter(StringUtils::hasText)
                .findFirst();
    }

}
