package com.codular.common.exception;

import com.codular.common.response.BaseResponseEntity;
import com.codular.common.response.BaseResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class BaseExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public BaseExceptionHandlerFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (BaseException e) {
            log.error("BaseException -> {} ({})", e.getStatus().name(),
                    (e.getMessage() != null ? e.getMessage() : e.getStatus().getMessage()), e);

            if (isApiRequest(request)) {
                writeError(response, new BaseResponseEntity<>(e.getStatus(),
                        (e.getMessage() != null ? e.getMessage() : e.getStatus().getMessage())));
            } else {
                // 뷰 요청은 ControllerAdvice로 처리시키기 위해 재던지기
                throw e;
            }

        } catch (AuthenticationException e) {
            log.error("AuthenticationException -> {}", e.getMessage(), e);

            if (isApiRequest(request)) {
                writeError(response, new BaseResponseEntity<>(BaseResponseStatus.UNAUTHORIZED,
                        "인증이 필요합니다."));
            } else {
                throw e;
            }

        } catch (Exception e) {
            log.error("Unhandled Exception in filter -> {}", request.getRequestURI(), e);

            if (isApiRequest(request)) {
                writeError(response, new BaseResponseEntity<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
            } else {
                throw e;
            }
        }
    }

    private boolean isApiRequest(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String accept = req.getHeader("Accept");
        String xhr = req.getHeader("X-Requested-With");
        return (uri != null && uri.startsWith("/api/"))
                || (accept != null && accept.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xhr);
    }

    private void writeError(HttpServletResponse response, BaseResponseEntity<?> body) throws IOException {
        if (response.isCommitted()) return;

        response.setStatus(body.httpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 다운로드/파일 응답과 충돌 방지 헤더
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store");

        objectMapper.writeValue(response.getWriter(), body);
    }
}