package com.kvote.backend.controller;

import com.kvote.backend.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private int number; // 이메일 인증 숫자를 저장하는 변수

    // 인증 이메일 전송
    @PostMapping("/mailSend")
    public ResponseEntity<?> mailSend(String mail) {
        HashMap<String, Object> map = new HashMap<>();

        // 학교 이메일 형식 확인
        if (!mail.endsWith("@kyonggi.ac.kr")) {
            map.put("success", Boolean.FALSE);
            map.put("error", "학교 이메일(@kyonggi.ac.kr)만 사용 가능합니다.");
            return ResponseEntity.badRequest().body(map);
        }

        try {
            number = mailService.sendMail(mail);
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return ResponseEntity.ok(map);
    }

    // 인증번호 일치여부 확인
    @GetMapping("/mailCheck")
    public ResponseEntity<?> mailCheck(@RequestParam String userNumber) {

        boolean isMatch = userNumber.equals(String.valueOf(number));

        return ResponseEntity.ok(isMatch);
    }
}
