package com.example.hrms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeFetchDto {

    private UUID employeeId;
    private String name;
    private String email;
    private String role;
    private String profilePictureFileUrl;
}
