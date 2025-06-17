package com.kvote.backend.fcm;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.kvote.backend.domain.User;
import com.kvote.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FcmService {

    private final UserRepository userRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Transactional
    public void saveToken(User user, String token){
        user.updateFcmToken(token);
        userRepository.save(user);

    }

    // 공지 알림 전송
    public void sendNotificationToAllUsers(String title, String body) {
        List<User> usersWithToken = userRepository.findAllByFcmTokenIsNotNull();

        for (User user : usersWithToken) {
            sendMessage(user.getFcmToken(), title, body);
        }
    }

    private void sendMessage(String token, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            // 로그 처리
            System.out.println("FCM 전송 실패: " + e.getMessage());
        }
    }

}