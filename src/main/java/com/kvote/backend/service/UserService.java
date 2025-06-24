package com.kvote.backend.service;

import com.kvote.backend.domain.User;
import com.kvote.backend.dto.UserResponseDto;
import com.kvote.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 관련 서비스 (주로 refreshToken 삭제용)
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 주어진 이메일을 가진 사용자의 refreshToken을 null로 설정합니다.
     * @param email 사용자 이메일
     */
    public void removeRefreshToken(String email) {
        User user = userRepository.findByKakaoEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일의 유저를 찾을 수 없습니다."));
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    public UserResponseDto getUserInfo(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .collegeMajorName(user.getCollegeMajorName())
                .kakaoEmail(user.getKakaoEmail())
                .refreshToken(user.getRefreshToken())
                .role(user.getRole())
                .studentVerified(user.isStudentVerified())
                .studentEmail(user.getStudentEmail())
                .walletAddress(user.getWalletAddress())
                .build();
    }
}
