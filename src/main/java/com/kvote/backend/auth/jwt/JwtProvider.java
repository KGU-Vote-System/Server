package com.kvote.backend.auth.jwt;

import io.jsonwebtoken.*;                // JJWT 라이브러리의 핵심 클래스들
import io.jsonwebtoken.security.Keys;   // HMAC 키 생성 유틸
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    // 비밀키 문자열 (실제 배포 시에는 절대로 코드에 하드코딩하지 말고 환경변수로 관리)
    private final String SECRET = "super-secret-key-super-secret-key";

    // 액세스토큰 만료 기간: 15분 (밀리초 단위)
    private final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000;

    // 리프레시토큰 만료 기간: 7일
    private final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000;

    // HMAC-SHA256 알고리즘용 키 객체 생성
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * 액세스 토큰 생성
     * @param subject 토큰의 주체(예: 사용자 ID, username 등)
     * @return 서명된 JWT 문자열
     */
    public String createAccessToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)                                   // JWT의 sub 클레임에 subject 설정
                .setExpiration(new Date(System.currentTimeMillis()     // 만료시간 설정 (현재시각 + 유효기간)
                        + ACCESS_TOKEN_VALIDITY))
                .signWith(key, SignatureAlgorithm.HS256)               // HMAC-SHA256 으로 서명
                .compact();                                            // 최종 JWT 문자열로 압축·생성
    }

    /**
     * 리프레시 토큰 생성
     */
    public String createRefreshToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)                                   // sub에 subject 설정
                .setExpiration(new Date(System.currentTimeMillis()
                        + REFRESH_TOKEN_VALIDITY))                    // 리프레시 유효기간 적용
                .signWith(key, SignatureAlgorithm.HS256)               // 동일한 비밀키로 서명
                .compact();
    }

    /**
     * 토큰 검증
     * @param token 전달받은 JWT
     * @return 유효하면 true, 아니면 RuntimeException 던짐
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()         // 파서 빌더 획득
                    .setSigningKey(key) // 검증에 사용할 비밀키 세팅
                    .build()            // 파서 인스턴스 생성
                    .parseClaimsJws(token);  // 파싱 및 서명검증 실행
            return true;                   // 예외 없으면 유효
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("토큰이 만료 되었습니당");   // 기한 만료
        } catch (JwtException e) {
            throw new RuntimeException("기타 서명 오류");    // 기타 서명 오류 등
        }
    }

    /**
     * 토큰에서 subject(claim) 추출
     */
    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()                                          // JWT 페이로드(Claims) 획득
                .getSubject();                                      // sub 값 리턴
    }

    /**
     * 토큰에서 만료일시 추출
     */
    public Date getExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();                                   // exp 값 리턴
    }
}

/*
setAllowedClockSkewSeconds(long seconds)
클라이언트/서버 간 시각 차이를 보정할 때 만료 검증에 허용할 여유 시간을 지정합니다.
requireSubject(String sub)
파싱 시 sub 클레임이 이 값과 일치하지 않으면 예외를 던집니다.
 */