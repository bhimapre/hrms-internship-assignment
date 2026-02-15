package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.JobReferral;
import com.example.hrms_backend.entities.JobShare;
import com.example.hrms_backend.entities.enums.JobOpeningStatus;
import com.example.hrms_backend.entities.enums.JobType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class JobOpeningDto {
    private UUID jobOpeningId;

    @NotBlank(message = "Job title is required")
    @Size(min = 2, max =  100, message = "Job title must be between 2 and 100 characters long")
    private String jobTitle;

    @NotBlank(message = "Job description is required")
    @Size(min = 10, max =  500, message = "Job description must be between 10 and 500 characters long")
    private String jobDescription;

    @NotBlank(message = "Job location is required")
    @Size(min = 2, max =  100, message = "Job location must be between 2 and 100 characters long")
    private String jobLocation;

    @Positive(message = "No of opening must be positive value")
    @NotNull(message = "No of opening is required")
    private Integer noOfOpening;

    @NotNull(message = "Experience is required")
    @Positive(message = "Experience must be positive value")
    private Integer experience;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Job type is required")
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private JobOpeningStatus jobOpeningStatus;

    private String jobDescriptionFileUrl;

    private List<JobReferral> jobReferrals;
    private List<JobShare> jobShares;
}
