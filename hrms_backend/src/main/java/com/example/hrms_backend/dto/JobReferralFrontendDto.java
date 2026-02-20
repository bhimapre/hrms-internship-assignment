package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.enums.JobReferralStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class JobReferralFrontendDto {
    private UUID jobReferralId;
    private String name;
    private String email;
    private String phoneNumber;
    private String shortNote;
    private JobReferralStatus jobReferralStatus;
}
