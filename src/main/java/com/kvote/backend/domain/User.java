package com.kvote.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 실제 이름 */
    @NotBlank
    @Column(nullable = false)
    private String name;

    /*그 뭐시기 그 대학 전공 이름 -> 부전공도 있어야 하나 ? // 그리고 이걸 개별 String으로 받을지 아니면 좀 노가다로 경기대학교 학과를 전체 List업해서 넣을지 고민중*/
    @NotBlank
    @Column(nullable = false)
    private String collegeMajorName;

    /** 카카오 로그인용 이메일 */
    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String kakaoEmail;

    /** JWT Refresh Token (optional) */
    @Column(length = 512)
    private String refreshToken;

    /** 사용자 역할 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /** 학생 인증 여부 */
    @Column(nullable = false)
    private boolean studentVerified;

    /**
     * 학생 인증용 이메일
     * - 반드시 kyonggi.ac.kr 도메인
     */
    @NotBlank
    @Email
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@kyonggi\\.ac\\.kr$",
            message = "Kyonggi University 이메일(@kyonggi.ac.kr)만 허용됩니다."
    )
    @Column(nullable = false, unique = true)
    private String studentEmail;

    /** Klaytn 지갑 주소 */
    @NotBlank
    @Column(nullable = false, unique = true)
    private String walletAddress;

    /**
     * Klaytn KAS Key ID
     * (KAS 지갑 관리용 key identifier)
     */
    @Column(nullable = false)
    private String keyId;

    /**
     * Klaytn Resource Name (KRN)
     * (KAS 리소스 식별자)
     */
    @Column(nullable = false, unique = true)
    private String krn;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Column(nullable = false, unique = true)
    private String fcmToken;

    public void updateFcmToken(String newToken) {
        this.fcmToken = newToken;
    }

}
