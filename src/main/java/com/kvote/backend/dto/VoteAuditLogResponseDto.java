package com.kvote.backend.dto;

import com.kvote.backend.domain.VoteAuditLog;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteAuditLogResponseDto {
    private Long id;
    private Long electionId;
    private String voterAddress;  // 투표자의 블록체인 주소
    private String txHash;  // 블록체인 트랜잭션 해시
    private Date timestamp;

    public static VoteAuditLogResponseDto from(VoteAuditLog log) {
        return VoteAuditLogResponseDto.builder()
                .id(log.getId())
                .electionId(log.getElectionId())
                .voterAddress(log.getVoterAddress())
                .txHash(log.getTxHash())
                .timestamp(log.getTimestamp())
                .build();
    }
}
