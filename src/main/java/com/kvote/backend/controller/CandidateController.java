package com.kvote.backend.controller;

import com.kvote.backend.auth.utils.UserDetailsImpl;
import com.kvote.backend.dto.CandidateRequestDto;
import com.kvote.backend.dto.CandidateResponseDto;
import com.kvote.backend.dto.CandidateResultsDto;
import com.kvote.backend.global.response.ApiResponse;
import com.kvote.backend.global.response.SuccessCode;
import com.kvote.backend.service.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping("/all/{electionId}")
    @Operation(summary = "Get candidates by election ID")
    public ApiResponse<CandidateResponseDto> getCandidatesByElection(@PathVariable Long electionId,
                                                                           @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(candidateService.getCandidatesByElection(electionId));
    }

    @GetMapping("/{candidateId}")
    @Operation(summary = "Get a candidate by election ID and candidate ID")
    public ApiResponse<CandidateResponseDto> getCandidateById(
                                                @PathVariable Long candidateId,
                                                @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(candidateService.getCandidateById(candidateId));
    }

    @PostMapping("/")
    @Operation(summary = "Create a new candidate")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CandidateResponseDto> createCandidate(@RequestBody CandidateRequestDto dto,
                                                @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        return new ApiResponse<>(candidateService.addCandidate(dto, user.getUser()));
    }

    @PutMapping("/{candidateId}")
    @Operation(summary = "Update a candidate")
    public ApiResponse<CandidateResponseDto> updateCandidate(@PathVariable Long candidateId,
                                                @RequestBody CandidateRequestDto dto,
                                                @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(candidateService.updateCandidate(candidateId, dto, user.getUser()));
    }

    @DeleteMapping("/{candidateId}")
    @Operation(summary = "Delete a candidate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteCandidate(@PathVariable Long candidateId,
                                                @AuthenticationPrincipal UserDetailsImpl user) {
        candidateService.deleteCandidate(candidateId, user.getUser());
        return new ApiResponse<>(SuccessCode.REQUEST_OK);
    }

    @GetMapping("/{electionId}/result")
    @Operation(summary = "Get election results by election ID")
    public ApiResponse<CandidateResultsDto> getElectionResults(@PathVariable Long electionId,
                                                                     @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(candidateService.getElectionResults(electionId));
    }

}
