package com.kvote.backend.controller;

import com.kvote.backend.domain.User;
import com.kvote.backend.domain.UserRole;
import com.kvote.backend.dto.SignUpRequest;
import com.kvote.backend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 2) 회원가입 Controller
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignUpController {
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequest req) {
        // 이미 같은 kakaoEmail 로 가입된 사용자가 있으면 예외 처리
        if (userRepository.findByKakaoEmail(req.getKakaoEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("이미 가입된 사용자입니다.");
        }

        User newUser = User.builder()
                .name(req.getName())
                .collegeMajorName(req.getCollegeMajorName())
                .kakaoEmail(req.getKakaoEmail())
                .role(UserRole.ROLE_USER)           // 기본 ROLE 설정
                .studentVerified(false)        // 인증 로직 별도 구현 필요
                .studentEmail(req.getStudentEmail())
                .walletAddress(req.getWalletAddress())
                .keyId(req.getKeyId())
                .krn(req.getKrn())
                .build();

        userRepository.save(newUser);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}

