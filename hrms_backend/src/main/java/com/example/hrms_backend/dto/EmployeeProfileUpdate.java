package com.example.hrms_backend.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class EmployeeProfileUpdate {
    private UUID employeeId;
    private Set<UUID> gamePreferences;
}
