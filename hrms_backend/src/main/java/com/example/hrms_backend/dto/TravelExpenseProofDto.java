package com.example.hrms_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class TravelExpenseProofDto {

    private UUID expenseProofId;

    @NotBlank(message = "Expense Proof Path is required")
    private String expenseProofPath;

    private UUID travelExpenseId;
}
