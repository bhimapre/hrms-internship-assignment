package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.Role;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    @Email(message = "Email must be in formate")
    @Size(min = 7, max =  50, message = "Email must be between 7 and 50 characters long")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank
    private String role;
}
