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
    ELECTION_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "선거가 비활성화 상태입니다."),
    ELECTION_ACTIVE(HttpStatus.BAD_REQUEST, "선거가 활성화 상태입니다."),
    ELECTION_DELETION_FAILED(HttpStatus.BAD_REQUEST, "선거 삭제에 실패했습니다."),
    ELECTION_NOT_ACTIVE_OR_DELETED(HttpStatus.BAD_REQUEST, "선거가 활성화되지 않았거나 삭제되었습니다."),
    ELECTION_END_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "선거 종료에 실패했습니다."),
    ELECTION_DOT_NOT_UPDATE(HttpStatus.BAD_REQUEST, "선거가 수정 불가상태입니다."),
    ELECTION_ALREADY_STARTED(HttpStatus.UNAUTHORIZED, "이미 선거가 시작되었습니다."),
    ELECTION_END_TIME_INVALID(HttpStatus.BAD_REQUEST, "종료 시간이 올바르지 않습니다."),

    // vote errors
    VOTE_ALREADY_CAST(HttpStatus.CONFLICT,  "이미 해당 선거에 투표했습니다."),
    VOTE_FAILD(HttpStatus.BAD_REQUEST, "투표에 실패했습니다."),
    VOTE_AUDIT_LOG_CREATION_FAILED(HttpStatus.BAD_GATEWAY, "투표 감사 로그 생성에 실패했습니다."),
    VOTE_AUDIT_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "투표 감사 로그를 찾을 수 없습니다."),
    VOTE_NOT_ELIGIBLE(HttpStatus.UNAUTHORIZED, "투표 자격이 없습니다."),
    VOTE_ELIGIBILITY_CHECK_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "투표 자격 확인에 실패했습니다."),

    // default errors
    ENUM_CONVERSION_ERROR(HttpStatus.BAD_REQUEST, "Enum 변환 오류입니다."),

    // user errors
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    // nominee errors
    NOMINEE_NOT_FOUND(HttpStatus.NOT_FOUND, "후보자를 찾을 수 없습니다."),
    MAIN_NOMINEE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 정후보가 존재합니다."),
    SUB_NOMINEE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 부후보가 존재합니다."),

    // candidate errors,
    CANDIDATE_NOT_FOUND(HttpStatus.NOT_FOUND, "후보자를 찾을 수 없습니다."),
    CANDIDATE_ADD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "후보자 추가에 실패했습니다."),
    // pledge errors
    PLEDGE_NOT_FOUND(HttpStatus.NOT_FOUND, "공약을 찾을 수 없습니다."),

    // tx errors
    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "트랜잭션을 찾을 수 없습니다."),

    // success codes,
    REQUEST_OK(HttpStatus.OK, "요청이 성공했습니다."),;


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