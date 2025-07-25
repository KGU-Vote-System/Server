package com.kvote.backend.controller;

import com.kvote.backend.auth.utils.UserDetailsImpl;
import com.kvote.backend.domain.User;
import com.kvote.backend.dto.ElectionRequestDto;
import com.kvote.backend.dto.ElectionResponseDto;
import com.kvote.backend.dto.TotalVoteCountDto;
import com.kvote.backend.global.response.ApiResponse;
import com.kvote.backend.global.response.SuccessCode;
import com.kvote.backend.service.ElectionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/elections")
@RequiredArgsConstructor
public class ElectionController {

    private final ElectionService electionService;

    @PostMapping("/")
    @Operation(summary = "Create a new election")
    public ApiResponse<ElectionResponseDto> createElection(@RequestBody ElectionRequestDto dto,
                                                           @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        return new ApiResponse<>(electionService.createElection(dto, user.getUser()));
    }

    @GetMapping("/{electionId}")
    @Operation(summary = "Get details of an election")
    public ApiResponse<ElectionResponseDto> getElection(@PathVariable Long electionId,
                                                           @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(electionService.getElectionById(electionId));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all elections")
    public ApiResponse<List<ElectionResponseDto>> getAllElections(@AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<List<ElectionResponseDto>>(electionService.getAllElections(user.getUser()));
    }

    @GetMapping("/{electionId}/total-vote-count")
    @Operation(summary = "Get vote count for an election")
    public ApiResponse<TotalVoteCountDto> getElectionVoteCount(@PathVariable BigInteger electionId,
                                                               @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        return new ApiResponse<>(electionService.getElectionVoteCount(electionId, user.getUser()));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Get elections by year")
    public ApiResponse<List<ElectionResponseDto>> getElectionsByYear(@PathVariable int year,
                                                                      @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<List<ElectionResponseDto>>(electionService.getElectionsByYear(year, user.getUser()));
    }

    @PostMapping("/{electionId}/start")
    @Operation(summary = "Start an election")
    public ApiResponse<Void> startElection(@PathVariable BigInteger electionId,
                                              @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        electionService.startElection(electionId, user.getUser());
        return new ApiResponse<>(SuccessCode.REQUEST_OK);
    }

    @PostMapping("/{electionId}/end")
    @Operation(summary = "End an election")
    public ApiResponse<Void> endElection(@PathVariable BigInteger electionId,
                                              @AuthenticationPrincipal UserDetailsImpl user) throws Exception {

        electionService.endElection(electionId, user.getUser());
        return new ApiResponse<>(SuccessCode.REQUEST_OK);
    }

    @PutMapping("/{electionId}")
    @Operation(summary = "Update an election")
    public ApiResponse<ElectionResponseDto> updateElection(@PathVariable Long electionId,
                                                              @RequestBody ElectionRequestDto dto,
                                                              @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        return new ApiResponse<>(electionService.updateElection(electionId, dto, user.getUser()));
    }

    @DeleteMapping("/{electionId}")
    @Operation(summary = "Delete an election")
    public ApiResponse<Void> deleteElection(@PathVariable Long electionId,
                                               @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        electionService.deleteElection(electionId, user.getUser());
        return new ApiResponse<>(SuccessCode.REQUEST_OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status")
    @Operation(summary = "예정, 진행중, 종료됨으로 나눠서 선거 조회")
    public ResponseEntity<List<ElectionResponseDto>> getElectionsByStatus(@RequestParam String status) {
        List<ElectionResponseDto> elections;

        switch (status.toLowerCase()) {
            case "upcoming":
                elections = electionService.getUpcomingElections();
                break;
            case "ongoing":
                elections = electionService.getOngoingElections();
                break;
            case "ended":
                elections = electionService.getEndedElections();
                break;
            default:
                throw new IllegalArgumentException("Invalid election status: " + status);
        }

        return ResponseEntity.ok(elections);
    }
}
