package com.kvote.backend.controller;

import com.kvote.backend.auth.utils.UserDetailsImpl;
import com.kvote.backend.service.JwtBlacklistedService;
import com.kvote.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final JwtBlacklistedService jwtBlacklistedService;
    private final UserService userService;

    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃",
            description = "현재 로그인된 사용자의 accessToken을 블랙리스트에 등록하고, refreshToken을 DB에서 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 완료"),
            @ApiResponse(responseCode = "400", description = "Authorization 헤더 누락 또는 형식 오류")
    })
    public ResponseEntity<String> logout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(
                    name = "Authorization",
                    description = "JWT accessToken (Bearer {토큰})",
                    required = true,
                    example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            )
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("잘못된 토큰 형식입니다.");
        }

        String token = authHeader.substring(7);
        String email = userDetails.getUsername();

        jwtBlacklistedService.addToBlacklist(token);
        userService.removeRefreshToken(email);

        return ResponseEntity.ok("로그아웃 완료");
    }
}
