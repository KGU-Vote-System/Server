package com.kvote.backend.controller;

import com.kvote.backend.auth.utils.UserDetailsImpl;
import com.kvote.backend.dto.VoteAuditLogResponseDto;
import com.kvote.backend.dto.VoteTxRequestDto;
import com.kvote.backend.global.exception.CheckmateException;
import com.kvote.backend.global.exception.ErrorCode;
import com.kvote.backend.global.response.ApiResponse;
import com.kvote.backend.global.response.SuccessCode;
import com.kvote.backend.service.VoteAuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/vote-audit-logs")
@RequiredArgsConstructor
public class VoteAuditLogController {

    private final VoteAuditLogService voteAuditLogService;

    @PostMapping("/{electionId}/vote")
    @Operation(summary = "Vote in an election")
    public ApiResponse<Void> vote(@PathVariable BigInteger electionId, @RequestParam BigInteger candidateId,
                                       @AuthenticationPrincipal UserDetailsImpl user) throws Exception {

        voteAuditLogService.vote(electionId, candidateId, user.getUser());
        return new ApiResponse<>(SuccessCode.REQUEST_OK);
    }

    @PostMapping("/{electionId}/is-eligible")
    @Operation(summary = "Check if user is eligible to vote in an election")
    public ApiResponse<Boolean> isEligibleToVote(@RequestParam BigInteger electionId,
                                                 @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(voteAuditLogService.isEligibleToVote(electionId, user.getUser()));
    }

    @PostMapping("/")
    @Operation(summary = "Create a vote audit log from transaction")
    public ApiResponse<VoteAuditLogResponseDto> createVoteAuditLog(@RequestBody VoteTxRequestDto voteTxRequestDto,
                                                @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        return new ApiResponse<>(voteAuditLogService.recordAuditLogFromTx(voteTxRequestDto, user.getUser()));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all vote audit logs")
    public ApiResponse<VoteAuditLogResponseDto> getAllVoteAuditLogs(@AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(voteAuditLogService.getAllVoteAuditLogs(user.getUser()));
    }

//    @GetMapping("/test")
//    @Operation(summary = "Test endpoint for VoteAuditLog")
//    public ResponseEntity<String> test() {
//        throw CheckmateException.from(ErrorCode.VOTE_FAILD, "Test error for VoteAuditLog");
//    }
}
