package com.kvote.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Operation(summary = "카카오톡 로그인 구현API")
    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return ResponseEntity.ok("안녕하세요! " + oAuth2User.getAttribute("properties") + "님");
    }
}
