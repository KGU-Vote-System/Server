package com.kvote.backend.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableMethodSecurity
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtProvider jwtProvider;

    @Override
    public void configure(final HttpSecurity http){
        JwtFilter customFilter = new JwtFilter(jwtProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
