package com.kvote.backend.service;

import com.kvote.backend.dto.TokenDto;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvote.backend.auth.jwt.JwtProvider;
import com.kvote.backend.domain.User;
import com.kvote.backend.domain.UserRole;
import com.kvote.backend.dto.KakaoLoginResponse;
import com.kvote.backend.dto.SignUpRequest;
import com.kvote.backend.dto.TokenResponseDto;
import com.kvote.backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Tag(name = "KakaoAuthService", description = "카카오 인증과 JWT 발급/회원가입 관련 서비스")
public class KakaoAuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Transactional
    public KakaoLoginResponse handleKakaoLogin(String code) {
        String accessToken = getAccessTokenFromKakao(code);
        String kakaoEmail = getKakaoEmail(accessToken);

        return userRepository.findByKakaoEmail(kakaoEmail)
                .map(user -> {
                    TokenDto token = generateTokenFor(user);
                    user.setRefreshToken(token.getRefreshToken());
                    return new KakaoLoginResponse(true, kakaoEmail, token);
                })
                .orElseGet(() -> new KakaoLoginResponse(false, kakaoEmail, null));
    }

    @Transactional
    public TokenDto completeSignup(SignUpRequest req) {
        if (userRepository.findByKakaoEmail(req.getKakaoEmail()).isPresent()) {
            throw new IllegalStateException("이미 가입된 사용자입니다.");
        }

        User user = User.builder()
                .name(req.getName())
                .collegeMajorName(req.getCollegeMajorName())
                .kakaoEmail(req.getKakaoEmail())
                .role(UserRole.ROLE_USER)
                .studentVerified(true)
                .studentEmail(req.getStudentEmail())
                .walletAddress(req.getWalletAddress())
                .keyId(req.getKeyId())
                .krn(req.getKrn())
                .build();

        TokenDto token = generateTokenFor(user);
        user.setRefreshToken(token.getRefreshToken());
        userRepository.save(user);

        return token;
    }

    public TokenDto refreshToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

        TokenDto token = generateTokenFor(user);
        user.setRefreshToken(token.getRefreshToken());
        userRepository.save(user);

        return token;
    }

    @Hidden
    private String getAccessTokenFromKakao(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=authorization_code"
                + "&client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectUri
                + "&code=" + code;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("access_token").asText();
        } catch (Exception e) {
            throw new IllegalStateException("카카오 토큰 파싱 실패", e);
        }
    }

    @Hidden
    private String getKakaoEmail(String token) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("kakao_account").get("email").asText();
        } catch (Exception e) {
            throw new IllegalStateException("카카오 사용자 정보 파싱 실패", e);
        }
    }


    private TokenDto generateTokenFor(User user) {
        return jwtProvider.generateTokenDto(
                new UsernamePasswordAuthenticationToken(
                        user.getKakaoEmail(),
                        null,
                        Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
                )
        );
    }
}
