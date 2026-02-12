package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.enums.JobReferralStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobReferralStatusDto {

    @NotNull(message = "Staus is required")
    private JobReferralStatus jobReferralStatus;
}
