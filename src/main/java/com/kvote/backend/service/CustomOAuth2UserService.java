package com.kvote.backend.service;

import com.kvote.backend.domain.User;
import com.kvote.backend.domain.UserRole;
import com.kvote.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        Map<String, Object> attributes = user.getAttributes();

        String kakaoEmail = ((Map<String, Object>) attributes.get("kakao_account")).get("email").toString();
        String name = ((Map<String, Object>) attributes.get("properties")).get("nickname").toString();

        // 사용자 저장 또는 조회
        User savedUser = userRepository.findByKakaoEmail(kakaoEmail)
                .orElseGet(() -> {
                    return userRepository.save(User.builder()
                            .name(name)
                            .kakaoEmail(kakaoEmail)
                            .collegeMajorName("미지정")
                            .role(UserRole.ROLE_USER)
                            .studentVerified(false)
                            .walletAddress("TO_BE_ASSIGNED")
                            .studentEmail("TO_BE_ASSIGNED")
                            .keyId("TO_BE_ASSIGNED")
                            .krn("TO_BE_ASSIGNED")
                            .build());
                });

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(savedUser.getRole().name())),
                attributes,
                "id"
        );
    }
}
