package com.kvote.backend.service;

import com.kvote.backend.auth.jwt.JwtProvider;
import com.kvote.backend.domain.User;
import com.kvote.backend.domain.UserRole;
import com.kvote.backend.dto.TokenDto;
import com.kvote.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    /**
     * “자동 회원가입(Auto Sign-Up)” 로직을 적용한 메서드.
     * 이미 가입된 사용자가 있으면 해당 User를 그대로 사용하고,
     * 없으면 새 User를 자동으로 생성한 뒤 JWT 를 발급해준다.
     */
    @Transactional
    public TokenDto loginOrSignUpWithKakao(String authorizationCode) {
        // 1) 카카오로부터 AccessToken 발급받기
        String kakaoAccessToken = getKakaoAccessToken(authorizationCode);

        // 2) AccessToken 으로 카카오 사용자 정보 조회 → 이메일 추출
        String kakaoEmail = getKakaoEmail(kakaoAccessToken);

        // 3) DB 조회. 없으면 자동 가입
        User user = userRepository.findByKakaoEmail(kakaoEmail)
                .orElseGet(() -> {
                    // 신규 회원이니까 자동 가입 로직 수행
                    User newUser = User.builder()
                            // 자동 가입 시, 카카오 프로필에 없는 필드는 placeholder로 두거나
                            // 이후에 별도 프로필 수정 API를 통해 채워줘도 된다.
                            .name("카카오회원")                    // 예시: 카카오 닉네임을 쓰고 싶으면 getKakaoNickName(...) 같은 추가 메서드 구현 필요
                            .collegeMajorName("미정")             // 자동 가입 상태이므로 기본값
                            .kakaoEmail(kakaoEmail)
                            .role(UserRole.ROLE_USER)                  // 기본 권한 USER 로 지정
                            .studentVerified(false)
                            .studentEmail("none@kyonggi.ac.kr")   // placeholder
                            .walletAddress("0x0000")             // placeholder
                            .keyId("not_set")
                            .krn("not_set")
                            .build();
                    return userRepository.save(newUser);
                });

        // 4) 가입(또는 기존 조회)된 User 를 바탕으로 JWT 생성
        return jwtProvider.generateTokenDto(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        user.getKakaoEmail(),
                        null,
                        Collections.singleton(() -> user.getRole().name())
                )
        );
    }

    /**
     * 카카오 AccessToken 발급 메서드 (기존 로직 그대로)
     */
    private String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String body = "grant_type=authorization_code"
                + "&client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectUri
                + "&code=" + code;

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("카카오 토큰 발급 실패: HTTP " + response.getStatusCode());
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new IllegalStateException("카카오 토큰 응답 파싱 실패", e);
        }
    }

    /**
     * 카카오 사용자 정보 조회 메서드 (기존 로직 그대로)
     */
    private String getKakaoEmail(String kakaoAccessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("카카오 사용자 정보 조회 실패: HTTP " + response.getStatusCode());
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode kakaoAccount = jsonNode.get("kakao_account");
            if (kakaoAccount == null || kakaoAccount.get("email") == null) {
                throw new IllegalStateException("카카오 계정에 이메일 정보가 없습니다.");
            }
            return kakaoAccount.get("email").asText();
        } catch (Exception e) {
            throw new IllegalStateException("카카오 사용자 정보 파싱 실패", e);
        }
    }
}
