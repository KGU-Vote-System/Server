package com.kvote.backend.service;


import com.kvote.backend.contract.ElectionManager;
import com.kvote.backend.domain.*;
import com.kvote.backend.dto.CandidateResponseDto;
import com.kvote.backend.dto.ElectionRequestDto;
import com.kvote.backend.dto.ElectionResponseDto;
import com.kvote.backend.global.exception.CheckmateException;
import com.kvote.backend.global.exception.ErrorCode;
import com.kvote.backend.repository.CandidateRepository;
import com.kvote.backend.repository.ElectionRepository;
import com.kvote.backend.repository.VoteAuditLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectionService {

    private final ElectionRepository electionRepository;
    private final ElectionManager electionManager;


    public void isAdmin(User user) {
        if (user == null)
            throw CheckmateException.from(ErrorCode.USER_NOT_FOUND);
        if (user.getRole() != UserRole.ROLE_ADMIN)
            throw CheckmateException.from(ErrorCode.UNAUTHORIZED, "관리자 권한이 필요합니다.");
    }

    public ElectionResponseDto electionToDto(Election election) {
        return ElectionResponseDto.builder()
                .id(election.getId())
                .title(election.getTitle())
                .description(election.getDescription())
                .startAt(election.getStartAt())
                .endAt(election.getEndAt())
                .isActive(election.getIsActive())
                .campus(election.getCampus())
                .ownerId(election.getOwner().getId())
                .build();
    }

    public void isElectionActive(BigInteger electionId) {
        Election election = electionRepository.findById(electionId.longValue())
                .orElseThrow(() -> CheckmateException.from(ErrorCode.ELECTION_NOT_FOUND));
        if (!election.getIsActive()) {
            throw CheckmateException.from(ErrorCode.ELECTION_NOT_ACTIVE);
        }
    }

    public Long getElectionVoteCount(BigInteger electionId, User user) throws Exception {
        if (electionRepository.findById(electionId.longValue()).isEmpty()) {
            throw CheckmateException.from(ErrorCode.ELECTION_NOT_FOUND);
        }
        return electionManager.getTotalVotes(electionId).send().longValue();
    }

    @Transactional
    public ElectionResponseDto createElection(ElectionRequestDto dto, User owner) throws Exception {
        // 관리자만 선거를 생성할 수 있음
        isAdmin(owner);

        TransactionReceipt receipt = electionManager.createElection(dto.getTitle()).send();

        // 이벤트에서 electionId 추출
        List<ElectionManager.ElectionCreatedEventResponse> events = electionManager.getElectionCreatedEvents(receipt);
        if (events.isEmpty()) {
            throw CheckmateException.from(ErrorCode.ELECTION_CREATION_FAILED, "No ElectionCreated event found in receipt");
        }
        BigInteger electionId = events.get(0).electionId;

        // RDB에 저장
        Election election = dto.toEntity(electionId, receipt.getTransactionHash(), owner);

        Election saved = electionRepository.save(election);
        return electionToDto(saved);
    }

    public ElectionResponseDto getElectionById(Long electionId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.ELECTION_NOT_FOUND));
        return electionToDto(election);
    }

    public void endElection(BigInteger electionId, User user) throws Exception {
        isAdmin(user);
        electionManager.endElection(electionId).send();
    }

    public List<ElectionResponseDto> getAllElections(User user) {
        List<Election> elections = electionRepository.findAll();
        List<ElectionResponseDto> responseDtos = new ArrayList<>();
        for (Election election : elections) {
            responseDtos.add(electionToDto(election));
        }
        return responseDtos;
    }

    public ElectionResponseDto updateElection(Long electionId, ElectionRequestDto dto, User user) {
        isAdmin(user);
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.ELECTION_NOT_FOUND));

        // 선거가 활성화 상태가 아니면 수정 불가
        if (!election.getIsActive()) {
            throw CheckmateException.from(ErrorCode.ELECTION_NOT_ACTIVE);
        }

        // RDB 업데이트
        election.update(dto);
        Election updatedElection = electionRepository.save(election);
        return electionToDto(updatedElection);
    }

    @Transactional
    public void deleteElection(Long electionId, User user) {
        isAdmin(user);
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.ELECTION_NOT_FOUND));

        // 선거가 활성화 상태면 삭제 불가
        if (election.getIsActive()) {
            throw CheckmateException.from(ErrorCode.ELECTION_ACTIVE, "활성화된 선거는 삭제할 수 없습니다.");
        }

        // 블록체인에서 선거 삭제
        try {
            electionManager.deleteElection(BigInteger.valueOf(electionId)).send();
        } catch (Exception e) {
            throw CheckmateException.from(ErrorCode.ELECTION_DELETION_FAILED);
        }
        // RDB에서 삭제
        electionRepository.delete(election);
    }
}
