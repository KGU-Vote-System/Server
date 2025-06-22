package com.kvote.backend.service;

import com.kvote.backend.contract.ElectionManager;
import com.kvote.backend.domain.Election;
import com.kvote.backend.domain.User;
import com.kvote.backend.domain.VoteAuditLog;
import com.kvote.backend.dto.VoteAuditLogResponseDto;
import com.kvote.backend.dto.VoteTxRequestDto;
import com.kvote.backend.global.exception.CheckmateException;
import com.kvote.backend.global.exception.ErrorCode;
import com.kvote.backend.repository.ElectionRepository;
import com.kvote.backend.repository.VoteAuditLogRepository;
import org.web3j.protocol.core.methods.response.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteAuditLogService {

    private final ElectionManager electionManager;
    private final ElectionService electionService;
    private final VoteAuditLogRepository voteAuditLogRepository;
    private final Web3j web3j;


    @Transactional
    public void vote(BigInteger electionId, BigInteger candidateId, User user) throws Exception {
        // 선거가 활성화 상태인지 확인
        electionService.isElectionActive(electionId);

//        //  블록체인에서 hasVoted 여부 조회
//        voteAuditLogRepository.findByElectionIdAndUserId(electionId.longValue(), user.getId())
//                .ifPresent(log -> {
//                    throw CheckmateException.from(ErrorCode.VOTE_ALREADY_CAST);
//                });

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
            } else if (msg != null && msg.contains("You are not eligible to vote")) {
                throw CheckmateException.from(ErrorCode.VOTE_NOT_ELIGIBLE, "투표 자격이 없습니다");
            } else if (msg != null && msg.contains("Candidate does not belong to this election")) {
                throw CheckmateException.from(ErrorCode.ELECTION_DOT_NOT_UPDATE, "투표 권한이 없습니다");
            }
            log.info("트랜잭션 예외 발생: {}", msg);
            throw CheckmateException.from(ErrorCode.VOTE_FAILD, "알 수 없는 트랜잭션 예외: " + msg);
        }

        // RDB에 감사로그 기록 (선택)
        VoteAuditLog auditLog = VoteAuditLog.builder()
                .electionId(electionId.longValue())
                .voterAddress(user.getWalletAddress())
                .txHash(receipt.getTransactionHash())
                .timestamp(new Date())
                .build();

//        candidateService.syncVoteCount(electionId); // 투표 즉시 반영
        voteAuditLogRepository.save(auditLog);
    }

    public VoteAuditLogResponseDto recordAuditLogFromTx(VoteTxRequestDto voteTxRequestDto, User user) {
        try {
            EthTransaction txResponse = web3j.ethGetTransactionByHash(voteTxRequestDto.getTxHash()).send();
            Transaction tx = txResponse.getTransaction()
                    .orElseThrow(() -> CheckmateException.from(ErrorCode.TRANSACTION_NOT_FOUND, "트랜잭션을 찾을 수 없습니다"));

//            String from = tx.getFrom();
            String input = tx.getInput();

            String rawData = input.substring(10); // 4 bytes selector (8 hex chars) + 0x
            BigInteger electionId = new BigInteger(rawData.substring(0, 64), 16);
            BigInteger candidateId = new BigInteger(rawData.substring(64, 128), 16);

            VoteAuditLog log = VoteAuditLog.builder()
                    .electionId(electionId.longValue())
                    .voterAddress(tx.getFrom())
                    .txHash(voteTxRequestDto.getTxHash())
                    .timestamp(new Date())
                    .build();

            voteAuditLogRepository.save(log);
            return VoteAuditLogResponseDto.from(log);
        } catch (Exception e) {
            log.error("Error processing transaction: {}", e.getMessage());
            throw CheckmateException.from(ErrorCode.VOTE_AUDIT_LOG_CREATION_FAILED, "트랜잭션 처리 중 오류 발생: " + e.getMessage());
        }
    }

    public List<VoteAuditLogResponseDto> getAllVoteAuditLogs(User user) {
        List<VoteAuditLog> logs = voteAuditLogRepository.findAll();
        if (logs.isEmpty()) {
            throw CheckmateException.from(ErrorCode.VOTE_AUDIT_LOG_NOT_FOUND, "투표 감사 로그가 없습니다");
        }

        return logs.stream()
                .map(log -> VoteAuditLogResponseDto.builder()
                        .id(log.getId())
                        .electionId(log.getElectionId())
                        .voterAddress(log.getVoterAddress())
                        .txHash(log.getTxHash())
                        .timestamp(log.getTimestamp())
                        .build())
                .toList();
    }

    public boolean isEligibleToVote(BigInteger electionId, User user) {
        // 선거가 활성화 상태인지 확인
        Election election = electionService.isElectionActive(electionId);

        // 이미 투표한 경우
        if (voteAuditLogRepository.findByElectionIdAndVoterAddress(electionId.longValue(), user.getWalletAddress()).isPresent()) {
            throw CheckmateException.from(ErrorCode.VOTE_ALREADY_CAST, "이미 해당 선거에 투표했습니다.");
        }
        // 선거 기간이 유효하지 않은 경우
        if (election.getEndAt().before(new Date()) || election.getStartAt().after(new Date())) {
            throw CheckmateException.from(ErrorCode.ELECTION_NOT_ACTIVE_OR_DELETED, "해당 선거는 현재 투표할 수 없는 상태입니다.");
        }
//        // 캠퍼스 불일치
//        if (election.getCampus() == null || !election.getCollageMajorName().equals(user.getCollegeMajorName())) {
//            throw CheckmateException.from(ErrorCode.UNAUTHORIZED, "해당 선거에 투표할 권한이 없습니다.");
//        }

        try {
            TransactionReceipt receipt = electionManager
                    .allowToVote(electionId, user.getWalletAddress())
                    .send();  // 동기 처리

            if (receipt.isStatusOK()) {
                return true;
            } else {
                throw CheckmateException.from(ErrorCode.VOTE_NOT_ELIGIBLE, "트랜잭션 실패로 투표 권한 부여 실패");
            }
        } catch (Exception e) {
            log.error("투표 자격 부여 중 오류", e);
            throw CheckmateException.from(ErrorCode.VOTE_ELIGIBILITY_CHECK_FAILED, "투표 자격 확인 중 오류 발생: " + e.getMessage());
        }
    }
}
