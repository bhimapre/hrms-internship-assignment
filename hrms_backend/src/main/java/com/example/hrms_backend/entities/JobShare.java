package com.example.hrms_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_shares")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobShare {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "job_share_id")
    private UUID jobShareId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_opening_id")
    private JobOpening jobOpening;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToMany(mappedBy = "jobShare", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobShareEmails> jobShareEmails;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
