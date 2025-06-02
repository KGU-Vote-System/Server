package com.kvote.backend.service;

import com.kvote.backend.contract.ElectionManager;
import com.kvote.backend.domain.Candidate;
import com.kvote.backend.domain.Election;
import com.kvote.backend.domain.User;
import com.kvote.backend.domain.VoteAuditLog;
import com.kvote.backend.global.exception.CheckmateException;
import com.kvote.backend.global.exception.ErrorCode;
import com.kvote.backend.repository.ElectionRepository;
import com.kvote.backend.repository.VoteAuditLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteAuditLogService {

    private final ElectionManager electionManager;
    private final CandidateService candidateService;
    private final ElectionService electionService;
    private final VoteAuditLogRepository voteAuditLogRepository;
    private final ElectionRepository electionRepository;

    /**
     * 모든 선거의 투표 수를 주기적으로 동기화합니다.
     * 이 메서드는 10분마다 실행됩니다.
     */
    @Scheduled(fixedRate = 600000)  // 600,000ms = 10분
    public void syncAllVoteCounts() {
        List<Election> elections = electionRepository.findAll();
        for (Election election : elections) {
            try {
                candidateService.syncVoteCount(BigInteger.valueOf(election.getId()));
            } catch (Exception e) {
                // 예외 로깅 또는 처리
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void vote(BigInteger electionId, BigInteger candidateId, User user) throws Exception {
        // 선거가 활성화 상태인지 확인
        electionService.isElectionActive(electionId);

        //  블록체인에서 hasVoted 여부 조회
        voteAuditLogRepository.findByElectionIdAndUserId(electionId.longValue(), user.getId())
                .ifPresent(log -> {
                    throw CheckmateException.from(ErrorCode.VOTE_ALREADY_CAST);
                });

        // 투표 트랜잭션 실행
        TransactionReceipt receipt = electionManager.vote(electionId, candidateId).send();

        // RDB에 감사로그 기록 (선택)
        VoteAuditLog auditLog = VoteAuditLog.builder()
                .electionId(electionId.longValue())
                .txHash(receipt.getTransactionHash())
                .timestamp(new Date())
                .build();

//        candidateService.syncVoteCount(electionId); // 투표 즉시 반영 (시연할때 쓸듯)
        voteAuditLogRepository.save(auditLog);
    }
}
