package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.enums.TravelStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignTravelDto {
    private UUID travelId;
    private String travelTitle;
    private LocalDate travelDateTo;
    private LocalDate travelDateFrom;
    private String travelLocation;
    private String travelDetails;
    private TravelStatus travelStatus;
}
