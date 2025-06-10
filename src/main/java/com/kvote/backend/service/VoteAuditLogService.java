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
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteAuditLogService {

    private final ElectionManager electionManager;
    private final CandidateService candidateService;
    private final ElectionService electionService;
    private final VoteAuditLogRepository voteAuditLogRepository;
    private final ElectionRepository electionRepository;


    @Transactional
    public void vote(BigInteger electionId, BigInteger candidateId, User user) throws Exception {
        // 선거가 활성화 상태인지 확인
        electionService.isElectionActive(electionId);

        //  블록체인에서 hasVoted 여부 조회
        voteAuditLogRepository.findByElectionIdAndUserId(electionId.longValue(), user.getId())
                .ifPresent(log -> {
                    throw CheckmateException.from(ErrorCode.VOTE_ALREADY_CAST);
                });

        TransactionReceipt receipt;
        // 투표 트랜잭션 실행
        try {
            receipt = electionManager.vote(electionId, candidateId).send();

            if (!receipt.isStatusOK()) {
                throw CheckmateException.from(ErrorCode.VOTE_FAILD, "트랜잭션 상태 비정상");
            }

        } catch (Exception e) {
            String msg = e.getMessage();

            if (msg != null && msg.contains("Already voted in this election")) {
                throw CheckmateException.from(ErrorCode.VOTE_ALREADY_CAST, "이미 투표했습니다");
            } else if (msg != null && msg.contains("Election not active or deleted")) {
                throw CheckmateException.from(ErrorCode.ELECTION_NOT_ACTIVE_OR_DELETED);
            } else if (msg != null && msg.contains("Invalid candidate")) {
                throw CheckmateException.from(ErrorCode.CANDIDATE_NOT_FOUND, "유효하지 않은 후보자입니다");
            } else if (msg != null && msg.contains("Candidate is deleted")) {
                throw CheckmateException.from(ErrorCode.CANDIDATE_NOT_FOUND, "삭제된 후보자입니다");
            }
            throw CheckmateException.from(ErrorCode.VOTE_FAILD, "알 수 없는 트랜잭션 예외: " + msg);
        }

        // RDB에 감사로그 기록 (선택)
        VoteAuditLog auditLog = VoteAuditLog.builder()
                .electionId(electionId.longValue())
                .userId(user.getId())
                .txHash(receipt.getTransactionHash())
                .timestamp(new Date())
                .build();

//        candidateService.syncVoteCount(electionId); // 투표 즉시 반영
        voteAuditLogRepository.save(auditLog);
    }
}
