package com.kvote.backend.controller;

import com.kvote.backend.auth.utils.UserDetailsImpl;
import com.kvote.backend.service.VoteAuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/vote-audit-logs")
@RequiredArgsConstructor
public class VoteAuditLogController {

    private final VoteAuditLogService voteAuditLogService;

    @PostMapping("/{electionId}/vote")
    @Operation(summary = "Vote in an election")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> vote(@PathVariable BigInteger electionId, @RequestParam BigInteger candidateId,
                                       @AuthenticationPrincipal UserDetailsImpl user) throws Exception {

        voteAuditLogService.vote(electionId, candidateId, user.getUser());
        return ResponseEntity.ok("Vote cast successfully");
    }

}
