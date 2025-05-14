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
