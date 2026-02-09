package com.example.hrms_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TravelEmployeeDto {

    @NotNull(message = "Travel Id is required")
    private UUID travelId;

    @NotNull(message = "Employee is required")
    private List<UUID> employeeIds;
}
