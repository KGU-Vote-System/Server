package com.kvote.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // JPA 엔티티로 지정
@Getter // Getter만 사용, Setter는 최소화
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자는 JPA 전용으로 제한
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    @Column(unique = true) // 지갑 주소는 유일해야 함
    private String walletAddress;

    private String refreshToken; // refreshToken 저장용

    // 생성자: 필수값만 초기화
    public User(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    // refreshToken 갱신을 위한 메서드
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
/*
AccessToken = 로그인 후 API 요청할 때 사용하는 짧은 유효기간을 가진 토큰임
refreshToken = accessToken 만료 됐을때 새로 발급 받아야함

AccessToken < refreshToken ( 기간으로 따졌을 때 )  ㅇㅇ

accessToken은 유효시간 짧음 -> 서버에 저장 안 해도 됨 JWT니깐 유저정보 자체를 담고있어서 검증만 하면 됨
refreshToken => 탈취 위험이 큼 -> 로그아웃 하면 폐기 처분 ? 같은 블랙리스트 처리 해줘야함
Redis나 DB에 저장해서 관리 요망

흐음 -> 쉽게 말하면
accessToken은 사용자가 들고 댕기고 refreshToken은 서버가 검증용으로 저장함->그래서 User클래스에 refreshToken만 있는겁니당
 */