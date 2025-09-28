package com.codular.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

    // 2xx
    SUCCESS(HttpStatus.OK, true, 200, "요청에 성공하였습니다."),
    CREATED(HttpStatus.CREATED, true, 201, "요청에 성공하여 리소스가 생성되었습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT, true, 204, "요청에 성공하였으나 응답할 데이터가 없습니다."),

    // 4xx
    BAD_REQUEST(HttpStatus.BAD_REQUEST, false, 400,  "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, false, 401,  "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, false, 403,  "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, false, 404,  "요청한 리소스를 찾을 수 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, false, 409, "이미 존재하는 데이터입니다."),

    // 5xx
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 500,  "서버 내부 오류입니다."),

    // Custom Errors

    // jwt
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, false, 401, "만료된 JWT 토큰입니다."),
    BAD_JWT(HttpStatus.BAD_REQUEST, false, 400, "잘못된 JWT 토큰입니다."),
    FAILED_AUTHENTICATION_JWT_TOKEN(HttpStatus.UNAUTHORIZED, false, 401, "JWT 토큰 서명 검증에 실패하였습니다."),

    // user
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, false, 409, "이미 존재하는 이메일입니다.");


    private final HttpStatusCode httpStatusCode;
    private final boolean isSuccess;
    private final int code;
    private final String message;

}
