package com.kvote.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class KakaoErrorController {
    @GetMapping
    public ResponseEntity<String> oauthFailure() {
        return ResponseEntity.status(401).body("실패 -> 나중에 다시 시작 바람");
    }
}
