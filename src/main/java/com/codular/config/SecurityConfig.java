package com.codular.config;

import com.codular.domain.auth.filter.JwtAuthenticationFilter;
import com.codular.common.exception.BaseExceptionHandlerFilter;
import com.codular.common.security.JsonAccessDeniedHandler;
import com.codular.common.security.JsonAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final BaseExceptionHandlerFilter baseExceptionHandlerFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JsonAuthEntryPoint authenticationEntryPoint;
    private final JsonAccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 세션 미사용(JWT)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // CSRF 활성화
                .csrf(AbstractHttpConfigurer::disable)
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .ignoringRequestMatchers(
//                                "/api/v1/auth/sign-in",
//                                "/api/v1/auth/sign-up",
//                                "/api/v1/auth/refresh",
//                                "/api/v1/auth/logout",
//                                "/api/v1/auth/password/forgot",
//                                "/api/v1/auth/password/reset"))
                // cors
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/", "/favicon.ico", "/css/**", "/js/**", "/images/**"
                        , "/swagger-ui/**", "/v3/api-docs/**", "/error", "/error/**"
                        , "/auth/**", "/api/v1/auth/**").permitAll()
                        // 운영단계에서는 .authenticated()
                        .anyRequest().permitAll()
                )
                // 예외처리
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                // 기본 폼/로그인 disabled
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // 필터 순서(전역 예외 -> jwt)
                .addFilterBefore(baseExceptionHandlerFilter, AuthorizationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
