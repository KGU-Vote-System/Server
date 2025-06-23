package com.kvote.backend.controller;

import com.kvote.backend.dto.*;
import com.kvote.backend.service.KakaoAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Kakao Auth", description = "카카오 로그인 및 회원가입 API")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @Operation(summary = "카카오 로그인 시도", description = "인가 코드를 받아 로그인 시도 후 가입 여부 응답")
    @PostMapping("/kakao")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(@RequestBody CodeRequestDto codeDto) {
//        return ResponseEntity.ok(kakaoAuthService.handleKakaoLogin(codeDto.getCode()));
            KakaoLoginResponse response = kakaoAuthService.handleKakaoLogin(codeDto.getCode());
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 🔍 code를 브라우저로 받기 위한 디버그 엔드포인트
    @GetMapping("/kakao/test")
    public ResponseEntity<String> testCode(@RequestParam("code") String code) {
        return ResponseEntity.ok("받은 인가 코드: " + code);
    }

//    @Operation(summary = "카카오 리디렉트 처리용 GET", description = "카카오가 리디렉트해주는 code를 GET으로 받아 로그인 처리")
//    @GetMapping("/kakao")
//    public ResponseEntity<KakaoLoginResponse> kakaoRedirectLogin(@RequestParam("code") String code) {
//        return ResponseEntity.ok(kakaoAuthService.handleKakaoLogin(code));
//    }

    @Operation(summary = "회원가입", description = "카카오 이메일 + 사용자 정보 입력 후 최종 회원가입 -> ★★★★★★★★이거 쓰면 됨")
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
