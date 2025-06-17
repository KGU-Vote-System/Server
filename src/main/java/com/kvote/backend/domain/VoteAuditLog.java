package com.kvote.backend.domain;

import jakarta.persistence.Entity;
import lombok.Getter;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "vote_audit_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VoteAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long electionId;

    @Column(nullable = false)
    private String voterAddress;  // 투표자의 블록체인 주소

    @Column(nullable = false, unique = true)
    private String txHash;  // 블록체인 트랜잭션 해시

    @Column(nullable = false)
    private Date timestamp;
}
