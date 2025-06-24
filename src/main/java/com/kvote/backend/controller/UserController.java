package com.kvote.backend.controller;

import com.kvote.backend.auth.utils.UserDetailsImpl;
import com.kvote.backend.dto.UserResponseDto;
import com.kvote.backend.global.response.ApiResponse;
import com.kvote.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    final private UserService userService;

    @Operation(summary = "Get User Info", description = "Retrieve user information based on the authenticated user")
    @GetMapping("/")
    public ApiResponse<UserResponseDto> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ApiResponse<>(userService.getUserInfo(userDetails.getUser()));
    }

}
