package com.kvote.backend.controller;

import com.kvote.backend.auth.utils.UserDetailsImpl;
import com.kvote.backend.dto.PledgeRequestDto;
import com.kvote.backend.dto.PledgeResponseDto;
import com.kvote.backend.global.response.ApiResponse;
import com.kvote.backend.global.response.SuccessCode;
import com.kvote.backend.service.PledgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pledges")
public class PledgeController {

    private final PledgeService pledgeService;

    @PostMapping("/{candidateId}")
    public ApiResponse<PledgeResponseDto> createPledge(@PathVariable Long candidateId,
                                                       @RequestBody List<PledgeRequestDto> pledgeRequestDtoList,
                                                       @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(pledgeService.createPledge(pledgeRequestDtoList, candidateId, user.getUser()));
    }

    @GetMapping("/{pledgeId}")
    public ApiResponse<PledgeResponseDto> getPledge(@PathVariable Long pledgeId,
                                                    @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(pledgeService.getPledgeById(pledgeId, user.getUser()));
    }

    @GetMapping("/{candidateId}/all")
    public ApiResponse<List<PledgeResponseDto>> getAllPledges(@PathVariable Long candidateId,
                                                              @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<List<PledgeResponseDto>>(pledgeService.getAllPledges(user.getUser(), candidateId));
    }

    @PutMapping("/{pledgeId}")
    public ApiResponse<PledgeResponseDto> updatePledge(@PathVariable Long pledgeId,
                                                       @RequestBody PledgeRequestDto pledgeRequestDto,
                                                       @AuthenticationPrincipal UserDetailsImpl user) {
        return new ApiResponse<>(pledgeService.updatePledge(pledgeId, pledgeRequestDto, user.getUser()));
    }

    @DeleteMapping("/{pledgeId}")
    public ApiResponse<Void> deletePledge(@PathVariable Long pledgeId,
                                           @AuthenticationPrincipal UserDetailsImpl user) {
        pledgeService.deletePledge(pledgeId, user.getUser());
        return new ApiResponse<>(SuccessCode.REQUEST_OK);
    }
}
