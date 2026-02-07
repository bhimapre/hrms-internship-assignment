package com.example.hrms_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class EmployeeDto {
    private UUID employeeId;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max =  100, message = "Name must be between 2 and 100 characters long")
    private String name;

    @Email(message = "Email must be in formate")
    @NotBlank(message = "Email is required")
    @Size(min = 7, max =  50, message = "Email must be between 7 and 50 characters long")
    private String email;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @NotNull(message = "Date of Birth is required")
    private LocalDate dob;

    @NotBlank(message = "City is required")
    @Size(min = 2, max =  100, message = "City must be between 2 and 100 characters long")
    private String city;

    @NotBlank(message = "Phone Number is required")
    @Size(min = 10, max =  10, message = "Phone Number size must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Size(min = 10, max =  500, message = "Address must be between 10 and 500 characters long")
    private String address;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    private UUID departmentId;

    @NotBlank(message = "Designation is required")
    @Size(min = 2, max =  100, message = "Designation must be between 2 and 100 characters long")
    private String designation;

    private UUID managerId;
    private UUID userId;
}
