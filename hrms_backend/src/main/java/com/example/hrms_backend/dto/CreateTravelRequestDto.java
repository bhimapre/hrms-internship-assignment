package com.example.hrms_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class CreateTravelRequestDto {

    private String travelTitle;

    @NotNull(message = "Travel Date From required")
    private LocalDate travelDateFrom;

    @NotNull(message = "Travel Date To required")
    private LocalDate travelDateTo;

    @NotBlank(message = "Travel Location required")
    private String travelLocation;

    @NotBlank(message = "Travel Details is required")
    @Size(min = 10, max =  500, message = "Travel Details must be between 10 and 500 characters long")
    private String travelDetails;

    private List<UUID> employeeIds;
}
