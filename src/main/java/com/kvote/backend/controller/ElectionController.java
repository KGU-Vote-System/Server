package com.kvote.backend.controller;

import com.kvote.backend.auth.utils.UserDetailsImpl;
import com.kvote.backend.domain.User;
import com.kvote.backend.dto.ElectionRequestDto;
import com.kvote.backend.dto.ElectionResponseDto;
import com.kvote.backend.service.ElectionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ElectionResponseDto> createElection(@RequestBody ElectionRequestDto dto,
                                              @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ElectionResponseDto res = electionService.createElection(dto, user.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

//    @PostMapping("/")
//    @Operation(summary = "Create a new election (no auth)")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<ElectionResponseDto> createElectionNoAuth(@RequestBody ElectionRequestDto dto) throws Exception {
//        // 인증 없이 선거 생성 (테스트용)
//
//        ElectionResponseDto res = electionService.createElection(dto, user);
//        return ResponseEntity.status(HttpStatus.CREATED).body(res);
//    }

    @GetMapping("/{electionId}")
    @Operation(summary = "Get details of an election")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ElectionResponseDto> getElection(@PathVariable Long electionId,
                                                           @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        ElectionResponseDto response = electionService.getElectionById(electionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all elections")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ElectionResponseDto>> getAllElections(@AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        List<ElectionResponseDto> elections = electionService.getAllElections(user.getUser());
        return ResponseEntity.ok(elections);
    }

    @GetMapping("/{electionId}/total-vote-count")
    @Operation(summary = "Get vote count for an election")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getElectionVoteCount(@PathVariable BigInteger electionId,
                                                       @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        Long res = electionService.getElectionVoteCount(electionId, user.getUser());
        return ResponseEntity.ok("electionId: " + electionId + ", vote count: " + res);
    }

    @PostMapping("/{electionId}/end")
    @Operation(summary = "End an election")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> endElection(@PathVariable BigInteger electionId,
                                              @AuthenticationPrincipal UserDetailsImpl user) throws Exception {

        electionService.endElection(electionId, user.getUser());
        return ResponseEntity.ok("Election ended");
    }

    @PutMapping("/{electionId}")
    @Operation(summary = "Update an election")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ElectionResponseDto> updateElection(@PathVariable Long electionId,
                                                              @RequestBody ElectionRequestDto dto,
                                                              @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        ElectionResponseDto updatedElection = electionService.updateElection(electionId, dto, user.getUser());
        return ResponseEntity.ok(updatedElection);
    }

    @DeleteMapping("/{electionId}")
    @Operation(summary = "Delete an election")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteElection(@PathVariable Long electionId,
                                               @AuthenticationPrincipal UserDetailsImpl user) throws Exception {
        electionService.deleteElection(electionId, user.getUser());
        return ResponseEntity.noContent().build();
    }
}
