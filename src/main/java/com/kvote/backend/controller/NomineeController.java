package com.kvote.backend.controller;

import com.kvote.backend.auth.utils.UserDetailsImpl;
import com.kvote.backend.dto.NomineeRequestDto;
import com.kvote.backend.dto.NomineeResponseDto;
import com.kvote.backend.global.response.ApiResponse;
import com.kvote.backend.global.response.SuccessCode;
import com.kvote.backend.service.NomineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nominees")
public class NomineeController {

    private final NomineeService nomineeService;

    @PostMapping("/{candidateId}")
    public ApiResponse<NomineeResponseDto> createNominee(@RequestBody NomineeRequestDto nomineeRequestDto,
                                                         @PathVariable Long candidateId,
                                                         @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(nomineeService.createNominee(nomineeRequestDto, candidateId, user.getUser()));
    }

    @GetMapping("/{nomineeId}")
    public ApiResponse<NomineeResponseDto> getNominee(@PathVariable Long nomineeId,
                                                      @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(nomineeService.getNomineeById(nomineeId, user.getUser()));
    }

    @GetMapping("/{candidateId}/all")
    public ApiResponse<NomineeResponseDto> getAllNominees(@PathVariable Long candidateId,
                                                                @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(nomineeService.getAllNominees(user.getUser(), candidateId));
    }

    @PutMapping("/{nomineeId}")
    public ApiResponse<NomineeResponseDto> updateNominee(@PathVariable Long nomineeId,
                                                         @RequestBody NomineeRequestDto nomineeRequestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(nomineeService.updateNominee(nomineeId, nomineeRequestDto, user.getUser()));
    }

    @DeleteMapping("/{nomineeId}")
    public ApiResponse<Void> deleteNominee(@PathVariable Long nomineeId,
                                           @AuthenticationPrincipal UserDetailsImpl user) {
        nomineeService.deleteNominee(nomineeId, user.getUser());
        return new ApiResponse<>(SuccessCode.REQUEST_OK);
    }
}
