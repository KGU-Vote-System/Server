package com.kvote.backend.controller;

import com.kvote.backend.auth.utils.UserDetailsImpl;
import com.kvote.backend.dto.CandidateRequestDto;
import com.kvote.backend.dto.CandidateResponseDto;
import com.kvote.backend.service.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping("/by-election/{electionId}")
    @Operation(summary = "Get candidates by election ID")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CandidateResponseDto>> getCandidatesByElection(@PathVariable Long electionId,
                                                              @AuthenticationPrincipal UserDetailsImpl user) {
        List<CandidateResponseDto> res = candidateService.getCandidatesByElection(electionId);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/")
    @Operation(summary = "Create a new candidate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CandidateResponseDto> createCandidate(@RequestBody CandidateRequestDto dto,
                                                @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(candidateService.addCandidate(dto, user.getUser()));
    }
}
