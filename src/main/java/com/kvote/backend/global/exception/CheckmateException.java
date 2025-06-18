package com.kvote.backend.global.exception;

import lombok.Getter;

@Getter
public class CheckmateException extends RuntimeException{
    private final ErrorCode errorCode;
    private String message;

    private CheckmateException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private CheckmateException(ErrorCode errorCode ,String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public static CheckmateException from(ErrorCode errorCode) {
        return new CheckmateException(errorCode);
    }

    public static CheckmateException from(ErrorCode errorCode, String message) {
        return new CheckmateException(errorCode,message);
    }
}