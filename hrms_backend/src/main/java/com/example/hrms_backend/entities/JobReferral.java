package com.example.hrms_backend.entities;

import com.example.hrms_backend.entities.enums.EmailStatus;
import com.example.hrms_backend.entities.enums.JobReferralStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_referrals",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "job_opening_id"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobReferral {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "job_referral_id")
    private UUID jobReferralId;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max =  100, message = "Name must be between 2 and 100 characters long")
    private String name;

    @Email(message = "Email must be in formate")
    @Column(name = "email", nullable = false)
    @NotBlank(message = "Email is required")
    @Size(min = 7, max =  50, message = "Email must be between 7 and 50 characters long")
    private String email;

    @Column(name = "phone_Number", nullable = false)
//    @Pattern(regexp = "^[6-9]\\\\d{9}$", message = "Invalid phone number format")
    @NotBlank(message = "Phone Number is required")
    @Size(min = 10, max =  10, message = "Phone Number size must be 10 digits")
    private String phoneNumber;

    @Column(name = "cv_file_url", nullable = false)
    @NotNull(message = "CV file url is required")
    private String cvFileUrl;

    @Column(name =  "public_id", nullable = false)
    @NotNull(message = "public id is required")
    private String publicId;

    @Column(name = "short_note")
    private String shortNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_opening_id")
    private JobOpening jobOpening;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_referral_status", nullable = false)
    private JobReferralStatus jobReferralStatus;

    @Column(name = "status_change_time")
    private LocalDateTime statusChangeTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_status")
    private EmailStatus emailStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
