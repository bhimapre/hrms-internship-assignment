package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.AuthRequest;
import com.example.hrms_backend.dto.LoginResponse;
import com.example.hrms_backend.dto.UserDto;
import com.example.hrms_backend.services.CustomUserDetails;
import com.example.hrms_backend.services.JwtService;
import com.example.hrms_backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String register(@Valid @RequestBody UserDto userDto){
        userService.register(userDto);
        return "User Registered Successfully";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return new LoginResponse(token);
    }
}
