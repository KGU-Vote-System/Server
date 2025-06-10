package com.kvote.backend.global.response;

import com.kvote.backend.global.exception.CheckmateException;
import com.kvote.backend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    REQUEST_OK(HttpStatus.OK, "올바른 요청입니다.");

    private final HttpStatus status;
    private final String message;

    // 메시지를 기반으로 ErrorCode를 찾는 정적 메서드
    public static SuccessCode fromMessage(String message) {
        for (SuccessCode successCode : SuccessCode.values()) {
            if (successCode.getMessage().equals(message)) {
                return successCode;
            }
        }
        throw CheckmateException.from(ErrorCode.ENUM_CONVERSION_ERROR);
    }
}