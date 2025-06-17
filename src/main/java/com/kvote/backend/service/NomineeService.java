package com.kvote.backend.service;

import com.kvote.backend.domain.Candidate;
import com.kvote.backend.domain.Nominee;
import com.kvote.backend.domain.User;
import com.kvote.backend.dto.NomineeRequestDto;
import com.kvote.backend.dto.NomineeResponseDto;
import com.kvote.backend.global.exception.CheckmateException;
import com.kvote.backend.global.exception.ErrorCode;
import com.kvote.backend.global.response.ApiResponse;
import com.kvote.backend.global.response.SuccessCode;
import com.kvote.backend.repository.CandidateRepository;
import com.kvote.backend.repository.NomineeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NomineeService {

    private final NomineeRepository nomineeRepository;
    private final CandidateRepository candidateRepository;
    private final ElectionService electionService;

    @Transactional
    public NomineeResponseDto createNominee(NomineeRequestDto nomineeRequestDto, Long candidateId, User user) {
        electionService.isAdmin(user);

        if (nomineeRequestDto.isMain() && nomineeRepository.findMainNomineeByCandidateId(candidateId) != null) {
            throw CheckmateException.from(ErrorCode.MAIN_NOMINEE_ALREADY_EXISTS);
        }
        if (!nomineeRequestDto.isMain() && nomineeRepository.findSubNomineeByCandidateId(candidateId) != null) {
            throw CheckmateException.from(ErrorCode.SUB_NOMINEE_ALREADY_EXISTS);
        }
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.CANDIDATE_NOT_FOUND));
        Nominee nominee = nomineeRepository.save(nomineeRequestDto.toEntity(candidate, user));
        return NomineeResponseDto.builder()
                        .id(nominee.getId())
                        .name(nominee.getName())
                        .studentId(nominee.getStudentId())
                        .college(nominee.getCollege())
                        .department(nominee.getDepartment())
                        .description(nominee.getDescription())
                        .candidateId(candidate.getId())
                        .isMain(nominee.isMain())
                        .build();
    }

    public NomineeResponseDto getNomineeById(Long nomineeId, User user) {
//        electionService.isAdmin(user);
        Nominee nominee = nomineeRepository.findById(nomineeId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.NOMINEE_NOT_FOUND));
        return NomineeResponseDto.builder()
                .id(nominee.getId())
                .name(nominee.getName())
                .studentId(nominee.getStudentId())
                .college(nominee.getCollege())
                .department(nominee.getDepartment())
                .description(nominee.getDescription())
                .candidateId(nominee.getCandidate().getId())
                .isMain(nominee.isMain())
                .build();
    }

    public NomineeResponseDto updateNominee(Long nomineeId, NomineeRequestDto nomineeRequestDto, User user) {
        electionService.isAdmin(user);
        Nominee nominee = nomineeRepository.findById(nomineeId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.NOMINEE_NOT_FOUND));
        nominee.update(nomineeRequestDto);
        nomineeRepository.save(nominee);
        return NomineeResponseDto.builder()
                .id(nominee.getId())
                .name(nominee.getName())
                .studentId(nominee.getStudentId())
                .college(nominee.getCollege())
                .department(nominee.getDepartment())
                .description(nominee.getDescription())
                .isMain(nominee.isMain())
                .build();
    }
    
    public void deleteNominee(Long nomineeId, User user) {
        electionService.isAdmin(user);
        Nominee nominee = nomineeRepository.findById(nomineeId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.NOMINEE_NOT_FOUND));
        nomineeRepository.delete(nominee);
    }

    public List<NomineeResponseDto> getAllNominees(User user, Long candidateId) {
        electionService.isAdmin(user);
        List<Nominee> nominees = nomineeRepository.findAllByCandidateId(candidateId);
        if (nominees.isEmpty())
            throw CheckmateException.from(ErrorCode.NOMINEE_NOT_FOUND);

        return nominees.stream()
                .map(nominee -> NomineeResponseDto.builder()
                        .id(nominee.getId())
                        .name(nominee.getName())
                        .studentId(nominee.getStudentId())
                        .college(nominee.getCollege())
                        .department(nominee.getDepartment())
                        .description(nominee.getDescription())
                        .candidateId(nominee.getCandidate().getId())
                        .isMain(nominee.isMain())
                        .build())
                .toList();
    }
}
