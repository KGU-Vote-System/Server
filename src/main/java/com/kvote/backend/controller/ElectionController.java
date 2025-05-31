package com.kvote.backend.controller;

import com.kvote.backend.auth.utils.UserDetailsImpl;
import com.kvote.backend.dto.ElectionRequestDto;
import com.kvote.backend.dto.ElectionResponseDto;
import com.kvote.backend.service.ElectionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.User;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/elections")
@RequiredArgsConstructor
public class ElectionController {

    private final ElectionService electionService;

    @PostMapping("/")
    @Operation(summary = "Create a new election")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ElectionResponseDto> createElection(@RequestBody ElectionRequestDto dto,
                                              @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        ElectionResponseDto res = electionService.createElection(dto, user.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/{electionId}")
    @Operation(summary = "Get details of an election")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ElectionResponseDto> getElection(@PathVariable Long electionId,
                                                           @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        ElectionResponseDto response = electionService.getElectionById(electionId);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/{electionId}/vote-count")
//    @Operation(summary = "Get vote count for an election")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<String> getElectionVoteCount(@PathVariable BigInteger electionId,
//                                                                    @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
//        Long res = electionService.getElectionVoteCount(electionId);
//        return ResponseEntity.ok("electionId: " + electionId + ", vote count: " + res);
//    }

    @PostMapping("/{electionId}/end")
    @Operation(summary = "End an election")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> endElection(@PathVariable BigInteger electionId,
                                              @AuthenticationPrincipal UserDetailsImpl user) throws Exception {

        electionService.endElection(electionId, user.getUser());
        return ResponseEntity.ok("Election ended");
    }
}
