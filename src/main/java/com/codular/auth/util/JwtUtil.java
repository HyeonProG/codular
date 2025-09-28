package com.codular.auth.util;

import com.codular.auth.config.JwtProperties;
import com.codular.common.exception.BaseException;
import com.codular.common.response.BaseResponseStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {

    private final String issuer;
    private final long skewSec;

    private final Key accessKey;
    private final long accessExpMin;

    private final Key refreshKey;
    private final long refreshExpDays;

    public JwtUtil(JwtProperties props) {
        this.issuer = props.getIssuer();
        this.skewSec = props.getClockSkewSec();

        this.accessKey = Keys.hmacShaKeyFor(props.getAccess().getSecret().getBytes(StandardCharsets.UTF_8));
        this.accessExpMin = props.getAccess().getExpMin();

        this.refreshKey = Keys.hmacShaKeyFor(props.getRefresh().getSecret().getBytes(StandardCharsets.UTF_8));
        this.refreshExpDays = props.getRefresh().getExpDays();
    }

    // Access Token 발급
    public String issueAccess(Long userId, String email, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setIssuer(issuer) // 토큰 발급자
                .setSubject(String.valueOf(userId)) // 사용자 식별
                .claim("email", email)
                .claim("role", role) // 권한
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(accessExpMin, ChronoUnit.MINUTES)))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 발급
    public String issueRefresh(Long userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setIssuer(issuer) // 토큰 발급자
                .setSubject(String.valueOf(userId)) // 사용자 식별
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(refreshExpDays, ChronoUnit.DAYS)))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // access token 검증
    public Jws<Claims> parseAccess(String token) {
        return parse(token, accessKey);
    }

    // refresh token 검증
    public Jws<Claims> parseRefresh(String token) {
        return parse(token, refreshKey);
    }

    // 파싱 로직
    private Jws<Claims> parse(String token, Key key) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .setAllowedClockSkewSeconds(skewSec)
                    .requireIssuer(issuer)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new BaseException(BaseResponseStatus.EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            throw new BaseException(BaseResponseStatus.BAD_JWT);
        } catch (SignatureException e) {
            throw new BaseException(BaseResponseStatus.FAILED_AUTHENTICATION_JWT_TOKEN);
        }
    }

    /*
    헬퍼 메서드
     */
    public Long getUserId(Claims claims) {
        return Long.valueOf(claims.getSubject());
    }

    public String getEmail(Claims claims) {
        return claims.get("email", String.class);
    }

    public String getRole(Claims claims) {
        return claims.get("role", String.class);
    }

}
