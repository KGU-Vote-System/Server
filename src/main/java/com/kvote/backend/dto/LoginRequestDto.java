package com.kvote.backend.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String walletAddress; //지갑주소
    private String signature; //주소로 서명하는 메시지
}
