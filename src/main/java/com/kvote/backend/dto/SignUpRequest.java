package com.kvote.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "회원가입 요청 DTO")
public class SignUpRequest {

    @Schema(description = "카카오 이메일", example = "test@kakao.com")
    private String kakaoEmail;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "전공명", example = "컴퓨터공학과")
    private String collegeMajorName;

    @Schema(description = "학교 이메일", example = "student@kyonggi.ac.kr")
    private String studentEmail;

    @Schema(description = "클레이튼 지갑 주소", example = "0x1234abcd5678")
    private String walletAddress;

    @Schema(description = "KAS Key ID", example = "kas-key-id")
    private String keyId;

    @Schema(description = "KRN", example = "krn:1001:abcd")
    private String krn;
}
