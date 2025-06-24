package com.kvote.backend.dto;

import com.kvote.backend.domain.UserRole;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private Long id; // 사용자 ID
    private String name; // 사용자 이름
    private String collegeMajorName; // 대학 전공 이름
    private String kakaoEmail; // 카카오 이메일
    private String refreshToken; // JWT Refresh Token (optional)
    private UserRole role; // 사용자 역할 (예: "ROLE_USER", "ROLE_ADMIN")
    private boolean studentVerified; // 학생 인증 여부
    private String studentEmail; // 학생 인증용 이메일 (kyonggi.ac.kr 도메인)
    private String walletAddress; // 지갑 주소 (optional)
}
