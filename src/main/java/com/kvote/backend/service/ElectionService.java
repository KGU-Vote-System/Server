package com.kvote.backend.service;


import com.kvote.backend.contract.ElectionManager;
import com.kvote.backend.domain.*;
import com.kvote.backend.dto.ElectionRequestDto;
import com.kvote.backend.dto.ElectionResponseDto;
import com.kvote.backend.dto.TotalVoteCountDto;
import com.kvote.backend.global.exception.CheckmateException;
import com.kvote.backend.global.exception.ErrorCode;
import com.kvote.backend.global.response.SuccessCode;
import com.kvote.backend.repository.ElectionRepository;
import com.kvote.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectionService {

    private final ElectionRepository electionRepository;
    private final ElectionManager electionManager;
    private final UserRepository userRepository;


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
                .collageMajorName(election.getCollageMajorName())
                .ownerId(election.getOwner().getId())
                .build();
    }

    public Election isElectionActive(BigInteger electionId) {
        Election election = electionRepository.findById(electionId.longValue())
                .orElseThrow(() -> CheckmateException.from(ErrorCode.ELECTION_NOT_FOUND));
        if (!election.getIsActive()) {
            throw CheckmateException.from(ErrorCode.ELECTION_NOT_ACTIVE);
        }
        return election;
    }

    public TotalVoteCountDto getElectionVoteCount(BigInteger electionId, User user) throws Exception {
        if (electionRepository.findById(electionId.longValue()).isEmpty()) {
            throw CheckmateException.from(ErrorCode.ELECTION_NOT_FOUND);
        }
        Long voteCount = electionManager.getTotalVotes(electionId).send().longValue();
        List<User> voters = userRepository.findAllByCollegeMajorName(
                electionRepository.findById(electionId.longValue())
                        .orElseThrow(() -> CheckmateException.from(ErrorCode.ELECTION_NOT_FOUND))
                        .getCollageMajorName()
        );
        return TotalVoteCountDto.builder()
                .electionId(electionId.longValue())
                .votedCount(voteCount)
                .voterCount((long) voters.size())
                .turnoutRate(
                        voters.isEmpty() ? 0.0 : (double) voteCount / voters.size() * 100)
                .build();
    }

    @Transactional
    public ElectionResponseDto createElection(ElectionRequestDto dto, User owner) throws Exception {
        // 관리자만 선거를 생성할 수 있음
        isAdmin(owner);

        long endTime = dto.getEndAt().toInstant().getEpochSecond(); // Date를 초 단위로 변환
        if (endTime <= Instant.now().getEpochSecond()) {
            throw CheckmateException.from(ErrorCode.ELECTION_END_TIME_INVALID, "선거 종료 시간은 현재 시간 이후여야 합니다.");
        }
        TransactionReceipt receipt = electionManager.createElection(dto.getTitle(), BigInteger.valueOf(endTime)).send();

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

    public void startElection(BigInteger electionId, User user) {
        isAdmin(user);
        Election election = isElectionActive(electionId);
        if (election.getStartAt().before(new Date())) {
            throw CheckmateException.from(ErrorCode.ELECTION_ALREADY_STARTED, "이미 시작된 선거입니다.");
        }
        if (election.getEndAt().before(new Date())) {
            throw CheckmateException.from(ErrorCode.ELECTION_END_TIME_INVALID, "선거 종료 시간이 현재 시간 이전입니다.");
        }

        // RDB 업데이트
        election.startElection();
        electionRepository.save(election);
    }

    public void endElection(BigInteger electionId, User user) throws Exception {
        isAdmin(user);
        Election election= electionRepository.findById(electionId.longValue())
                .orElseThrow(() -> CheckmateException.from(ErrorCode.ELECTION_NOT_FOUND));
        if (!election.getIsActive()) {
            throw CheckmateException.from(ErrorCode.ELECTION_NOT_ACTIVE, "선거가 비활성화 상태입니다.");
        }
        election.updateActiveStatus(false);
        electionRepository.save(election);
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
        Election election = isElectionActive(BigInteger.valueOf(electionId));
        if (election.getStartAt().before(new Date())) {
            throw CheckmateException.from(ErrorCode.ELECTION_DOT_NOT_UPDATE, "이미 시작된 선거는 수정할 수 없습니다.");
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

    public void scheduleEndElection() {
        // 현재 시간 기준으로 선거 종료 스케줄링
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        Date endAt = Date.from(now.toInstant(ZoneOffset.UTC));

        // 모든 활성화된 선거를 조회
        List<Election> activeElections = electionRepository.findByIsActiveTrue();

        for (Election election : activeElections) {
            if (election.getEndAt().before(endAt)) {
                try {
                    endElection(BigInteger.valueOf(election.getId()), election.getOwner());
                } catch (Exception e) {
                    throw CheckmateException.from(ErrorCode.ELECTION_END_FAILED, "선거 종료 중 오류 발생: " + e.getMessage());
                }
            }
        }
    }

    public List<ElectionResponseDto> getElectionsByYear(int year, User user) {
        List<Election> elections = electionRepository.findByStartAtBetween(
                Date.from(LocalDateTime.of(year, 1, 1, 0, 0).toInstant(ZoneOffset.UTC)),
                Date.from(LocalDateTime.of(year + 1, 1, 1, 0, 0).toInstant(ZoneOffset.UTC))
        );

        if (elections.isEmpty()) {
            throw CheckmateException.from(ErrorCode.ELECTION_NOT_FOUND, "해당 연도의 선거가 없습니다.");
        }

        List<ElectionResponseDto> responseDtos = new ArrayList<>();
        for (Election election : elections) {
            responseDtos.add(electionToDto(election));
        }
        return responseDtos;
    }
}
