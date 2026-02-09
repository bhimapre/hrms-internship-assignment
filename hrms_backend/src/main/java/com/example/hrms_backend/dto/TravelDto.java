package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.enums.TravelStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TravelDto {
    private UUID travelId;

    @NotBlank(message = "Travel Title required")
    @Size(min = 5, max = 50, message = "Travel title must be between 5 and 50 characters long")
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

    @NotNull(message = "Travel Status is required")
    private TravelStatus travelStatus;
}
