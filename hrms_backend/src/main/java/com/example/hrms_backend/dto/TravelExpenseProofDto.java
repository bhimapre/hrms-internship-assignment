package com.example.hrms_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TravelExpenseProofDto {

    private UUID expenseProofId;

    @NotNull(message = "file url is required")
    private String expenseFileUrl;
    private UUID travelExpenseId;
}
