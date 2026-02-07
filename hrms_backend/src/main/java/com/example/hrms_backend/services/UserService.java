package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.UserDto;
import com.example.hrms_backend.entities.Role;
import com.example.hrms_backend.entities.User;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.repositories.RoleRepo;
import com.example.hrms_backend.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    public void register(UserDto userDto)
    {
        if(userRepo.findByEmail(userDto.getEmail()).isPresent()){
            throw new RuntimeException("User already exist");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepo.findByRoleName(userDto.getRole())
                        .orElseThrow(() -> new BadRequestException("Invalid role"));

        user.setRole(role);
        user.setActive(true);

        userRepo.save(user);
    }
}
