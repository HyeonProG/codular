package com.codular.common.exception;

import com.codular.common.response.BaseResponseStatus;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final BaseResponseStatus status;

    public BaseException(BaseResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }

    public BaseException(BaseResponseStatus status, String message) {
        super(message);
        this.status = status;
    }

    public BaseException(BaseResponseStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
