package com.codular.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public record BaseResponseEntity<T>(@Schema(hidden = true) HttpStatusCode httpStatus, Boolean isSuccess, String message, int code, T result) {

    /*
      필요값 : Http상태코드, 성공여부, 메시지, 에러코드, 결과값
      1. Return 객체가 필요한 경우 -> 성공
      2. Return 객체가 필요 없는 경우 -> 성공
      3. 요청에 실패한 경우
     */

    /**
     * 1. Return 객체가 필요한 경우 -> 성공
     */
    public BaseResponseEntity(T result) {
        this(HttpStatus.OK, true, "SUCCESS", 200, result);
    }

    /**
     * 2. Return 객체가 필요 없는 경우 -> 성공
     */
    public BaseResponseEntity() {
        this(HttpStatus.OK, true, "SUCCESS", 200, null);
    }

    /**
     * 3. 요청에 실패한 경우
     */
    public BaseResponseEntity(BaseResponseStatus status) {
        this(status.getHttpStatusCode(), status.isSuccess(), status.getMessage(), status.getCode(), null);
    }

    public BaseResponseEntity(BaseResponseStatus status, String message) {
        this(status.getHttpStatusCode(), status.isSuccess(), message, status.getCode(), null);
    }

    /**
     * Return 객체가 필요하고 커스텀 상태값이 필요한 경우 -> 성공
     */
    public BaseResponseEntity(BaseResponseStatus base, T result) {
        this(base.getHttpStatusCode(),
                base.isSuccess(),
                base.getMessage(),
                base.getCode(),
                result
        );
    }
}

