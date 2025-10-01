package com.codular.domain.auth.util;

import com.codular.domain.auth.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties props;

    private SecretKey accessKey;
    private SecretKey refreshKey;

    @PostConstruct
    void init() {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.getAccess().getSecret()));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.getRefresh().getSecret()));
    }

    // accessToken 발급
    public String issueAccessToken(Long userId, String role) {
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofMinutes(props.getAccess().getExpMin()));

        return Jwts.builder()
                .setIssuer(props.getIssuer())
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String issueRefreshToken(Long userId, String role) {
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofDays(props.getRefresh().getExpDays()));

        return Jwts.builder()
                .setIssuer(props.getIssuer())
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 파싱/검증
    public Jws<Claims> parseAccess(String token) {
        return Jwts.parserBuilder()
                .requireIssuer(props.getIssuer())
                .setAllowedClockSkewSeconds(props.getClockSkewSec())
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token);
    }

    public Jws<Claims> parseRefresh(String token) {
        return Jwts.parserBuilder()
                .requireIssuer(props.getIssuer())
                .setAllowedClockSkewSeconds(props.getClockSkewSec())
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token);
    }

    // 유틸
    public Long getUserId(Claims claims) {
        String sub = claims.getSubject();
        return (sub == null) ? null : Long.parseLong(sub);
    }

    public String getRole(Claims claims) {
        return claims.get("role", String.class);
    }

    public long getAccessExpMin() { return props.getAccess().getExpMin(); }
    public long getRefreshExpDays() { return props.getRefresh().getExpDays(); }

}