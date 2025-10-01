package com.codular.domain.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "auth.cookie")
public class AuthCookieProperties {

    private String accessName = "ACCESS_TOKEN";
    private String refreshName = "REFRESH_TOKEN";
    private boolean secure = false; // 운영 시 true, 개발 시 false
    private String sameSite = "Lax";
    private String domain; // 추후 도메인 적용
    private String path = "/";

}
