package com.kvote.backend.fcm;

import com.kvote.backend.auth.utils.UserDetailsImpl;
import com.kvote.backend.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/register")
    public ResponseEntity<Void> saveFcmToken(@RequestBody FcmTokenRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        fcmService.saveToken(user, request.getToken());
        return ResponseEntity.ok().build();
    }
}
