package com.kvote.backend.auth.jwt;

import com.kvote.backend.dto.TokenDto;
import io.jsonwebtoken.*;                // JJWT 라이브러리의 핵심 클래스들
import io.jsonwebtoken.security.Keys;   // HMAC 키 생성 유틸
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.net.Authenticator;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    //userrole 클레임 키
    public static final String ROLES_CLAIM_KEY = "roles";

    private static final String BEARER_TYPE = "Bearer";

    // 비밀키 문자열 (실제 배포 시에는 절대로 코드에 하드코딩하지 말고 환경변수로 관리)
    private static final String SECRET = "super-secret-key-super-secret-key";

    // 액세스토큰 만료 기간: 15분 (밀리초 단위)
    private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000;

    // 리프레시토큰 만료 기간: 7일
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000;

    // HMAC-SHA256 알고리즘용 키 객체 생성
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public TokenDto generateTokenDto(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();

        //AccessToken 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_VALIDITY);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())         // payload "sub": "email or kakaoId"
                .claim(ROLES_CLAIM_KEY, authorities)          // payload "roles: "common"
                .setExpiration(accessTokenExpiresIn)          // payload "exp": 151621022 (ex)
                .signWith(key, SignatureAlgorithm.HS512)      // header  "alg": "HS512"
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_VALIDITY))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken){

        //토큰 복호화
        Claims claims = parseClaims(accessToken);

        if(claims.get(ROLES_CLAIM_KEY) == null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다");
        }

        //클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(ROLES_CLAIM_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }



    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();    // 유효한 경우: 정상적으로 claims 반환
        } catch (ExpiredJwtException e) {
            return e.getClaims();  // 만료된 경우: 예외 안 터트리고 claims만 꺼내기
        }
    }







}