package com.kvote.backend.global.exception;

import lombok.Getter;

@Getter
public class CheckmateException extends RuntimeException {
    private final ErrorCode errorCode;

    private CheckmateException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 메시지 전달
        this.errorCode = errorCode;
    }

    private CheckmateException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public static CheckmateException from(ErrorCode errorCode) {
        return new CheckmateException(errorCode);
    }

    public static CheckmateException from(ErrorCode errorCode, String customMessage) {
        return new CheckmateException(errorCode, customMessage);
    }
}

