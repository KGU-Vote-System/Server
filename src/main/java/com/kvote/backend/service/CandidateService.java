package com.kvote.backend.service;

import com.kvote.backend.contract.ElectionManager;
import com.kvote.backend.domain.Candidate;
import com.kvote.backend.domain.Election;
import com.kvote.backend.domain.User;
import com.kvote.backend.dto.CandidateRequestDto;
import com.kvote.backend.dto.CandidateResponseDto;
import com.kvote.backend.dto.ElectionResponseDto;
import com.kvote.backend.repository.CandidateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.Tuple;
import org.web3j.tuples.generated.Tuple2;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ElectionManager electionManager;
    private final ElectionService electionService;

    @Transactional
    public void syncVoteCount(BigInteger electionId) throws Exception {
        List<Candidate> candidates = candidateRepository.findByElectionId(electionId.longValue());

        for (Candidate candidate : candidates) {
            Tuple2<String, BigInteger> res = electionManager.getCandidate(electionId, BigInteger.valueOf(candidate.getId())).send();
            Long voteCount = res.component2().longValue();
            candidate.updateVoteCount(voteCount);
        }
    }

    public List<CandidateResponseDto> getCandidatesByElection(Long electionId) {
        List<Candidate> candidates = candidateRepository.findByElectionId(electionId);
        return candidates.stream()
                .map(candidate -> CandidateResponseDto.builder()
                        .id(candidate.getId())
                        .name(candidate.getName())
                        .electionId(candidate.getElectionId())
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

        BigInteger candidateId = events.get(0).candidateId;

        // RDB 저장
        Candidate candidate = Candidate.builder()
                .electionId(dto.getElectionId())
                .id(candidateId.longValue())
                .name(dto.getName())
                .voteCount(0L)
                .build();
        Candidate res = candidateRepository.save(candidate);

        return CandidateResponseDto.builder()
                .id(res.getId())
                .electionId(res.getElectionId())
                .name(res.getName())
                .voteCount(res.getVoteCount())
                .build();
    }
}
