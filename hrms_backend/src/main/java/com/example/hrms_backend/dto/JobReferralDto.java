package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.enums.EmailStatus;
import com.example.hrms_backend.entities.enums.JobReferralStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class JobReferralDto {

    private UUID jobReferralId;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max =  100, message = "Name must be between 2 and 100 characters long")
    private String name;

    @Email(message = "Email must be in formate")
    @NotBlank(message = "Email is required")
    @Size(min = 7, max =  50, message = "Email must be between 7 and 50 characters long")
    private String email;

    @Pattern(regexp = "^[6-9]\\\\d{9}$", message = "Invalid phone number format")
    @NotBlank(message = "Phone Number is required")
    @Size(min = 10, max =  10, message = "Phone Number size must be 10 digits")
    private String phoneNumber;

    @NotNull(message = "CV file url is required")
    private String cvFileUrl;

    @NotNull(message = "public id is required")
    private String publicId;

    private String shortNote;
    private UUID jobOpening;
    private UUID employee;
}
