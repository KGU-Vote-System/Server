package com.kvote.backend.repository;

import com.kvote.backend.domain.Campus;
import com.kvote.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 지갑 주소로 사용자 조회
    Optional<User> findByWalletAddress(String walletAddress);

    Optional<User> findByKakaoEmail(String kakaoEmail);

    Optional<User> findByRefreshToken(String refreshToken);

    List<User> findAllByCollegeMajorName(String collageMajorName);

    List<User> findAllByFcmTokenIsNotNull();

}