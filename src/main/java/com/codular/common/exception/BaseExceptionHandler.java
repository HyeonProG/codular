package com.codular.common.exception;

import com.codular.common.response.BaseResponseEntity;
import com.codular.common.response.BaseResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    // 공통 유틸
    private <T> ResponseEntity<BaseResponseEntity<T>> resp(BaseResponseEntity<T> body) {
        // BaseResponseEntity.record 에 들어있는 httpStatus() 그대로 사용
        return new ResponseEntity<>(body, body.httpStatus());
    }

    // 커스텀 예외
    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<BaseResponseEntity<Void>> handleBase(BaseException e) {
        BaseResponseStatus s = e.getStatus();
        // BaseException 에 커스텀 메시지가 있으면 사용, 없으면 상태 기본 메시지
        String msg = (e.getMessage() != null && !e.getMessage().isBlank())
                ? e.getMessage() : s.getMessage();

        log.error("BaseException -> {} ({})", s.name(), msg, e);
        return resp(new BaseResponseEntity<>(s, msg));
    }

    // 인증/검증 파싱
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<BaseResponseEntity<Void>> handleBadCredentials(BadCredentialsException e) {
        log.warn("BadCredentialsException: ", e);
        return resp(new BaseResponseEntity<>(BaseResponseStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<BaseResponseEntity<Void>> handleValidation(MethodArgumentNotValidException e) {
        BindingResult br = e.getBindingResult();
        FieldError fe = br.getFieldError();
        String msg = (fe != null) ? (fe.getField() + " : " + fe.getDefaultMessage())
                : BaseResponseStatus.BAD_REQUEST.getMessage();
        log.warn("Validation failed: {}", msg);
        return resp(new BaseResponseEntity<>(BaseResponseStatus.BAD_REQUEST, msg));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<BaseResponseEntity<Void>> handleJsonParse(HttpMessageNotReadableException e) {
        log.warn("JsonParseException: {}", e.getMessage());
        return resp(new BaseResponseEntity<>(BaseResponseStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다."));
    }

    // MyBatis, DB
    @ExceptionHandler(DuplicateKeyException.class)
    protected ResponseEntity<BaseResponseEntity<Void>> handleDuplicate(DuplicateKeyException e) {
        log.warn("DuplicateKeyException: ", e);
        return resp(new BaseResponseEntity<>(BaseResponseStatus.CONFLICT));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<BaseResponseEntity<Void>> handleIntegrity(DataIntegrityViolationException e) {
        log.warn("DataIntegrityViolationException: ", e);
        return resp(new BaseResponseEntity<>(BaseResponseStatus.BAD_REQUEST, "무결성 제약 조건 위반"));
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    protected ResponseEntity<BaseResponseEntity<Void>> handleSql(BadSqlGrammarException e) {
        log.error("BadSqlGrammarException: ", e);
        return resp(new BaseResponseEntity<>(BaseResponseStatus.INTERNAL_SERVER_ERROR, "SQL 구문 오류"));
    }

    @ExceptionHandler({MyBatisSystemException.class, DataAccessException.class})
    protected ResponseEntity<BaseResponseEntity<Void>> handleDataAccess(Exception e) {
        log.error("DataAccessException(MyBatis): ", e);
        return resp(new BaseResponseEntity<>(BaseResponseStatus.INTERNAL_SERVER_ERROR, "데이터 접근 중 오류"));
    }

    // fallback
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<BaseResponseEntity<Void>> handleUnknown(Exception e, HttpServletRequest req) {
        log.error("Unhandled Exception: {}", req.getRequestURI(), e);
        return resp(new BaseResponseEntity<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
    }
}