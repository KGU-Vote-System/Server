package com.kvote.backend.service;

import com.kvote.backend.domain.Candidate;
import com.kvote.backend.domain.Pledge;
import com.kvote.backend.domain.User;
import com.kvote.backend.dto.PledgeRequestDto;
import com.kvote.backend.dto.PledgeResponseDto;
import com.kvote.backend.global.exception.CheckmateException;
import com.kvote.backend.global.exception.ErrorCode;
import com.kvote.backend.global.response.SuccessCode;
import com.kvote.backend.repository.CandidateRepository;
import com.kvote.backend.repository.PledgeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PledgeService {

    private final PledgeRepository pledgeRepository;
    private final ElectionService electionService;
    private final CandidateRepository candidateRepository;

    @Transactional
    public List<PledgeResponseDto> createPledge(List<PledgeRequestDto> pledgeRequestDto, Long candidateId, User user) {
        electionService.isAdmin(user);
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.CANDIDATE_NOT_FOUND));

        // 공약 생성 로직
        List<Pledge> pledges = pledgeRequestDto.stream()
                .map(dto -> dto.toEntity(candidate))
                .toList();
        return pledges.stream()
                .map(pledgeRepository::save)
                .map(PledgeResponseDto::from)
                .toList();
    }

    public PledgeResponseDto getPledgeById(Long pledgeId, User user) {
//        electionService.isAdmin(user);
        Pledge pledge = pledgeRepository.findById(pledgeId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.PLEDGE_NOT_FOUND));
        return PledgeResponseDto.from(pledge);
    }

    @Transactional
    public PledgeResponseDto updatePledge(Long pledgeId, PledgeRequestDto pledgeRequestDto, User user) {
        electionService.isAdmin(user);
        Pledge pledge = pledgeRepository.findById(pledgeId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.PLEDGE_NOT_FOUND));

        // 공약 내용 업데이트
        pledge.update(pledgeRequestDto);
        pledgeRepository.save(pledge);

        return PledgeResponseDto.from(pledge);
    }

    @Transactional
    public void deletePledge(Long pledgeId, User user) {
        electionService.isAdmin(user);
        Pledge pledge = pledgeRepository.findById(pledgeId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.PLEDGE_NOT_FOUND));

        // 공약 삭제
        pledgeRepository.delete(pledge);
    }

    public List<PledgeResponseDto> getAllPledges(User user, Long candidateId) {
//        electionService.isAdmin(user);
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.CANDIDATE_NOT_FOUND));
        List<PledgeResponseDto> pledges = pledgeRepository.findAllByCandidate(candidate)
                .stream()
                .map(PledgeResponseDto::from)
                .toList();

        if (pledges.isEmpty())
            throw CheckmateException.from(ErrorCode.PLEDGE_NOT_FOUND);
        else
            return pledges;

    }
}
