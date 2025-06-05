package com.kvote.backend.fcm;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.private-key}")
    private String privateKey;

    @Value("${firebase.private-key-id}")
    private String privateKeyId;

    @Value("${firebase.client-email}")
    private String clientEmail;

    @Value("${firebase.client-id}")
    private String clientId;

    @Value("${firebase.type}")
    private String type;

    @Value("${firebase.project_id}")
    private String projectId;

    @Value("${firebase.auth_uri}")
    private String authUri;

    @Value("${firebase.token_uri}")
    private String tokenUri;

    @Value("${firebase.auth_provider_x509_cert_url}")
    private String authProviderCertUrl;

    @Value("${firebase.client_x509_cert_url}")
    private String clientCertUrl;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {

        // env에 저장된 privateKey는 \n 이 문자열로 들어있으니 실제 줄바꿈으로 변환
        String formattedPrivateKey = privateKey.replace("\\n", "\n"); // \n 을 \\n 으로 변환 (Java 문자열 내 JSON에 들어가려면 이중 escape)

        // JSON 형태로 서비스 계정 정보를 직접 생성
        String json = String.format("""
            {
              "type": "%s",
              "project_id": "%s",
              "private_key_id": "%s",
              "private_key": "%s",
              "client_email": "%s",
              "client_id": "%s",
              "auth_uri": "%s",
              "token_uri": "%s",
              "auth_provider_x509_cert_url": "%s",
              "client_x509_cert_url": "%s"
            }
            """,
                type,
                projectId,
                privateKeyId,
                formattedPrivateKey,
                clientEmail,
                clientId,
                authUri,
                tokenUri,
                authProviderCertUrl,
                clientCertUrl);

        ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        return FirebaseMessaging.getInstance();
    }
}

