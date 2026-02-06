package com.example.hrms_backend.services;

import com.example.hrms_backend.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user)
    {
        this.user = user;
    }

    public UUID getUserId() {
        return user.getUserId();
    }

    public String getRole() {
        return user.getRole().getRoleName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().getRoleName()));
    }

    @Override
    public String getPassword()
    {
        return user.getPassword();
    }

    @Override
    public String getUsername()
    {
        return user.getEmail();
    }

    @Override
    public boolean isEnabled()
    {
        return user.isActive();
    }

    @Override public boolean isAccountNonExpired()
    {
        return true;
    }
    @Override public boolean isAccountNonLocked()
    {
        return true;
    }
    @Override public boolean isCredentialsNonExpired()
    {
        return true;
    }
}
