package com.kvote.backend.controller;

import com.kvote.backend.auth.jwt.JwtProvider;
import com.kvote.backend.dto.TokenDto;
import com.kvote.backend.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    /**
     * 1) 프론트에서 카카오 로그인 후 얻은 authorization code 를 파라미터로 받아서,
     *    내부적으로 카카오 OAuth -> 로컬 사용자 조회/예외 처리 -> JWT 발급 흐름을 수행한다.
     *
     * 예) GET /auth/kakao?code={인증코드}
     */
    @GetMapping("/kakao")
    public ResponseEntity<TokenDto> kakaoLogin(@RequestParam("code") String authorizationCode) {
        TokenDto tokenDto = kakaoAuthService.loginOrSignUpWithKakao(authorizationCode);
        return ResponseEntity.ok(tokenDto);
    }
}

