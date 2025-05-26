package com.kvote.backend.auth.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        // 유효한 자격증명을 제공하지 않고 접근 -> 401 에러
        // ex) 사용자 정보 오류, 토큰이 유효하지 않은 경우
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
