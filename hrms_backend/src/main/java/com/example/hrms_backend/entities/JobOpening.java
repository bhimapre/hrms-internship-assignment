package com.example.hrms_backend.entities;

import com.example.hrms_backend.entities.enums.JobOpeningStatus;
import com.example.hrms_backend.entities.enums.JobType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_openings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobOpening {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "job_opening_id")
    private UUID jobOpeningId;

    @Column(name = "job_title", nullable = false)
    @NotBlank(message = "Job title is required")
    @Size(min = 2, max =  100, message = "Job title must be between 2 and 100 characters long")
    private String jobTitle;

    @Column(name = "job_description", nullable = false)
    @NotBlank(message = "Job description is required")
    @Size(min = 10, max =  500, message = "Job description must be between 10 and 500 characters long")
    private String jobDescription;

    @Column(name = "job_location", nullable = false)
    @NotBlank(message = "Job location is required")
    @Size(min = 2, max =  100, message = "Job location must be between 2 and 100 characters long")
    private String jobLocation;

    @Column(name = "no_of_opening", nullable = false)
    @Positive(message = "No of opening must be positive value")
    @NotNull(message = "No of opening is required")
    private Integer noOfOpening;

    @Column(name = "experience", nullable = false)
    @NotNull(message = "Experience is required")
    @Positive(message = "Experience must be positive value")
    private Integer experience;

    @Column(name = "job_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Job type is required")
    private JobType jobType;

    @Column(name = "job_opening_status")
    @Enumerated(EnumType.STRING)
    private JobOpeningStatus jobOpeningStatus;

    @Column(name = "job_description_file_url", nullable = false)
    @NotNull(message = "Job description file url is required")
    private String jobDescriptionFileUrl;

    @Column(name =  "public_id", nullable = false)
    @NotNull(message = "public id is required")
    private String publicId;

    @OneToMany(mappedBy = "jobOpening", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobReferral> jobReferrals;

    @OneToMany(mappedBy = "jobOpening", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobShare> jobShares;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
