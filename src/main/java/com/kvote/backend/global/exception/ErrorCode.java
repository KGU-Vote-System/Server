package com.kvote.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // election errors
    ELECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "선거를 찾을 수 없습니다."),
    ELECTION_CREATION_FAILED(HttpStatus.BAD_REQUEST, "선거 생성에 실패했습니다."),
    ELECTION_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "선거가 활성화되지 않았습니다."),

    // vote errors
    VOTE_ALREADY_CAST(HttpStatus.CONFLICT,  "이미 해당 선거에 투표했습니다."),

    // default errors
    ENUM_CONVERSION_ERROR(HttpStatus.BAD_REQUEST, "Enum 변환 오류입니다."),

    // user errors
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    ;


    private final HttpStatus status;
    private final String message;

    // 메시지를 기반으로 ErrorCode를 찾는 정적 메서드
    public static ErrorCode fromMessage(String message) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getMessage().equals(message)) {
                return errorCode;
            }
        }
        throw CheckmateException.from(ErrorCode.ENUM_CONVERSION_ERROR);
    }
}