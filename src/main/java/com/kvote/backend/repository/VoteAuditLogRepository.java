package com.kvote.backend.repository;

import com.kvote.backend.domain.VoteAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VoteAuditLogRepository extends JpaRepository<VoteAuditLog, Long> {

    // 트랜잭션 해시로 투표 감사 로그 조회
    Optional<VoteAuditLog> findByTxHash(String txHash);

    // 선거 ID로 투표 감사 로그 조회
    List<VoteAuditLog> findByElectionId(Long electionId);

    // 특정 시간 이후의 투표 감사 로그 조회
    List<VoteAuditLog> findByTimestampAfter(Date  timestamp);

    // 중복투표 체크
    Optional<VoteAuditLog> findByElectionIdAndUserId(Long electionId, Long userId);
}
