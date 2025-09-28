package com.codular.auth.filter;

import com.codular.auth.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final AntPathMatcher PM = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (header != null) {
                header = header.trim();
                if (header.regionMatches(true, 0, "Bearer ", 0, 7) && header.length() > 7) {
                    String token = header.substring(7).trim();

                    try {
                        // access token 검증
                        Jws<Claims> jws = jwtUtil.parseAccess(token);
                        Claims claims = jws.getBody();

                        // 최소 정보 추출
                        Long userId = jwtUtil.getUserId(claims);
                        String role = jwtUtil.getRole(claims);

                        // 인증 토큰 구성
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(userId, null,
                                        List.of(new SimpleGrantedAuthority("ROLE_" + role)));
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // 컨텍스트에 저장
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } catch (Exception e) {
                        SecurityContextHolder.clearContext();
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    // 특정 경로는 필터링하지 않도록 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return PM.match("/api/v1/auth/**", uri) // 회원가입/로그인/재발급
                || PM.match("/api/v1/test/**", uri) // 테스트용 공개 엔드포인트
                || PM.match("/h2-console/**", uri) // 개발 편의
                || PM.match("/swagger-ui/**", uri) // Swagger UI
                || PM.match("/v3/api-docs/**", uri); // OpenAPI
    }

}