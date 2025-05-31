package com.kvote.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignUpRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String collegeMajorName;

    @NotBlank @Email
    private String kakaoEmail;        // 카카오 로그인 후 얻은 이메일

    @NotBlank @Email
    private String studentEmail;      // 학교 이메일

    @NotBlank
    private String walletAddress;

    @NotBlank
    private String keyId;

    @NotBlank
    private String krn;
}

