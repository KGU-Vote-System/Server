package com.kvote.backend.service;

import com.kvote.backend.auth.jwt.JwtProvider;
import com.kvote.backend.domain.BlacklistedToken;
import com.kvote.backend.repository.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * JWT 로그아웃 시 AccessToken을 블랙리스트로 등록해주는 서비스
 */
@Service
@RequiredArgsConstructor
public class JwtBlacklistedService {

    private final BlacklistedTokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    /**
     * accessToken을 블랙리스트 테이블에 저장합니다.
     * @param token accessToken
     */
    public void addToBlacklist(String token) {
        long remainTime = jwtProvider.getRemainingTime(token);
        LocalDateTime expiredAt = LocalDateTime.now().plusNanos(remainTime * 1_000_000);
        tokenRepository.save(new BlacklistedToken(token, expiredAt));
    }

    /**
     * accessToken이 블랙리스트에 있는지 확인합니다.
     * @param token accessToken
     * @return true if blacklisted
     */
    public boolean isBlacklisted(String token) {
        return tokenRepository.existsByToken(token);
    }
}
