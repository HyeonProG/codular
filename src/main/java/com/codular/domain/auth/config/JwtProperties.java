package com.codular.domain.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtProperties {

    // 토큰 발급자
    private String issuer;

    // 서버간 시간 오차
    private long clockSkewSec;

    // access, refresh 토큰 설정
    private final Token access = new Token();
    private final Token refresh = new Token();

    @Getter
    @Setter
    public static class Token {
        private String secret;
        private long expMin;
        private long expDays;
    }

}
