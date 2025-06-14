package com.kvote.backend.service;

import com.kvote.backend.contract.ElectionManager;
import com.kvote.backend.domain.Candidate;
import com.kvote.backend.domain.Election;
import com.kvote.backend.domain.User;
import com.kvote.backend.dto.CandidateRequestDto;
import com.kvote.backend.dto.CandidateResponseDto;
import com.kvote.backend.dto.ElectionResponseDto;
import com.kvote.backend.global.exception.CheckmateException;
import com.kvote.backend.global.exception.ErrorCode;
import com.kvote.backend.repository.CandidateRepository;
import com.kvote.backend.repository.ElectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.Tuple;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ElectionManager electionManager;
    private final ElectionService electionService;
    private final ElectionRepository electionRepository;

    @Transactional
    public void syncVoteCount(BigInteger electionId) throws Exception {
        log.info("Syncing vote counts for election ID: {}", electionId);
        List<Candidate> candidates = candidateRepository.findByElectionId(electionId.longValue());

        for (Candidate candidate : candidates) {
            Tuple3<String, BigInteger, Boolean> res = electionManager.getCandidate(electionId, BigInteger.valueOf(candidate.getId())).send();
            Long voteCount = res.component2().longValue();
            candidate.updateVoteCount(voteCount);
        }
    }

    public List<CandidateResponseDto> getCandidatesByElection(Long electionId) {
        List<Candidate> candidates = candidateRepository.findByElectionId(electionId);
        return candidates.stream()
                .map(candidate -> CandidateResponseDto.builder()
                        .id(candidate.getId())
                        .electionId(candidate.getElection().getId())
                        .name(candidate.getName())
                        .voteCount(candidate.getVoteCount())
                        .build())
                .toList();
    }

    public CandidateResponseDto addCandidate(CandidateRequestDto dto, User owner) throws Exception {
        electionService.isAdmin(owner);
        TransactionReceipt receipt = electionManager.addCandidate(
                BigInteger.valueOf(dto.getElectionId()), dto.getName()).send();

        List<ElectionManager.CandidateAddedEventResponse> events = electionManager.getCandidateAddedEvents(receipt);
        if (events.isEmpty()) {
            throw new RuntimeException("No CandidateAdded event found in receipt");
        }

        BigInteger candidateId = events.getFirst().candidateId;
        Election election = electionRepository.findById(dto.getElectionId())
                .orElseThrow(() -> CheckmateException.from(ErrorCode.ELECTION_NOT_FOUND));

        // RDB 저장
        Candidate res = candidateRepository.save(Candidate.builder()
                .election(election)
                .id(candidateId.longValue())
                .name(dto.getName())
                .voteCount(0L)
                .build());

        return CandidateResponseDto.builder()
                .id(res.getId())
                .electionId(res.getElection().getId())
                .name(res.getName())
                .voteCount(res.getVoteCount())
                .build();
    }

    public CandidateResponseDto getCandidateById(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));
        return CandidateResponseDto.builder()
                .id(candidate.getId())
                .electionId(candidate.getElection().getId())
                .name(candidate.getName())
                .voteCount(candidate.getVoteCount())
                .build();
    }

    public CandidateResponseDto updateCandidate(Long candidateId, CandidateRequestDto dto, User user) {
        electionService.isAdmin(user);
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));
        candidate.updateName(dto.getName());
        Candidate updatedCandidate = candidateRepository.save(candidate);
        return CandidateResponseDto.builder()
                .id(updatedCandidate.getId())
                .electionId(updatedCandidate.getElection().getId())
                .name(updatedCandidate.getName())
                .voteCount(updatedCandidate.getVoteCount())
                .build();
    }

    public void deleteCandidate(Long candidateId, User user) {
        electionService.isAdmin(user);
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));
        candidateRepository.delete(candidate);
        try {
            electionManager.deleteCandidate(BigInteger.valueOf(candidate.getElection().getId()), BigInteger.valueOf(candidate.getId())).send();
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove candidate from blockchain: " + e.getMessage(), e);
        }
    }
}
