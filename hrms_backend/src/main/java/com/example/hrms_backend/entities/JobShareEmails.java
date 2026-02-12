package com.example.hrms_backend.entities;

import com.example.hrms_backend.entities.enums.EmailStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_share_emails",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"job_share_id", "email"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobShareEmails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID jobSharePersonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_share_id")
    private JobShare jobShare;

    @Email(message = "Email must be in formate")
    @Column(name = "email", nullable = false)
    @NotBlank(message = "Email is required")
    @Size(min = 7, max =  50, message = "Email must be between 7 and 50 characters long")
    private String email;

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
