package com.example.hrms_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TimeSlotGenerationDto {

    @NotNull(message = "Start date required")
    private LocalDate fromDate;

    @NotNull(message = "End date required")
    private LocalDate toDate;
}
