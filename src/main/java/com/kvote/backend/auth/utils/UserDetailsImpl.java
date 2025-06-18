package com.kvote.backend.auth.utils;


import com.kvote.backend.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> user.getRole().name());
    }

    @Override
    public String getPassword() {
        return null;  // 카카오 유저면 null 가능
    }

    @Override
    public String getUsername() {
        return user.getKakaoEmail();  // 또는 user.getEmail()
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}