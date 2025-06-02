package com.kvote.backend.controller;

import com.kvote.backend.dto.KakaoLoginResponse;
import com.kvote.backend.dto.SignUpRequest;
import com.kvote.backend.dto.TokenDto;
import com.kvote.backend.dto.TokenResponseDto;
import com.kvote.backend.service.KakaoAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Kakao Auth", description = "카카오 로그인 및 회원가입 API")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @Operation(summary = "카카오 로그인 시도", description = "인가 코드를 받아 로그인 시도 후 가입 여부 응답")
    @GetMapping("/kakao")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(@RequestParam("code") String code) {
        return ResponseEntity.ok(kakaoAuthService.handleKakaoLogin(code));
    }

    @Operation(summary = "회원가입", description = "카카오 이메일 + 사용자 정보 입력 후 최종 회원가입")
    @PostMapping("/kakao/signup")
    public ResponseEntity<TokenDto> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(kakaoAuthService.completeSignup(request));
    }

    @Operation(summary = "JWT 재발급", description = "Refresh Token을 이용해 Access Token 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestParam("refreshToken") String refreshToken) {
        return ResponseEntity.ok(kakaoAuthService.refreshToken(refreshToken));
    }
}
