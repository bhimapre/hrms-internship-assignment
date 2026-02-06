package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.AuthRequest;
import com.example.hrms_backend.services.CustomUserDetails;
import com.example.hrms_backend.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated())
        {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return jwtService.generateToken(userDetails);
        }
        else
        {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }
}
