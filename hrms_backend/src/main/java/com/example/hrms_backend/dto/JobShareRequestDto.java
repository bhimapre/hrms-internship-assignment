package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.JobShareEmails;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class JobShareRequestDto {

    @NotNull(message = "JobOpening is required")
    private UUID jobOpening;
    private UUID employee;

    private List<String> jobShareEmailIds;
}
