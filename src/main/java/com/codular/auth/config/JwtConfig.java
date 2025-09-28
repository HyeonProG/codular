package com.codular.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {
    /*
    JwtProperties 바인딩을 활성화 하기 위한 클래스
    스위치 역할
    추후 (OAuth2.0, redis 등) 다른 설정 클래스가 추가될 수 있음
     */
}
