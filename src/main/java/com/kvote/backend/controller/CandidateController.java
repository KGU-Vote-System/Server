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

    @GetMapping("/{electionId}/all")
    @Operation(summary = "Get candidates by election ID")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CandidateResponseDto>> getCandidatesByElection(@PathVariable Long electionId,
                                                              @AuthenticationPrincipal UserDetailsImpl user) {
        List<CandidateResponseDto> res = candidateService.getCandidatesByElection(electionId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{candidateId}")
    @Operation(summary = "Get a candidate by election ID and candidate ID")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CandidateResponseDto> getCandidateById(
                                                @PathVariable Long candidateId,
                                                @AuthenticationPrincipal UserDetailsImpl user) {
        CandidateResponseDto res = candidateService.getCandidateById(candidateId);
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

    @PutMapping("/{candidateId}")
    @Operation(summary = "Update a candidate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CandidateResponseDto> updateCandidate(@PathVariable Long candidateId,
                                                @RequestBody CandidateRequestDto dto,
                                                @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        CandidateResponseDto res = candidateService.updateCandidate(candidateId, dto, user.getUser());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{candidateId}")
    @Operation(summary = "Delete a candidate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long candidateId,
                                                @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        candidateService.deleteCandidate(candidateId, user.getUser());
        return ResponseEntity.noContent().build();
    }
}
