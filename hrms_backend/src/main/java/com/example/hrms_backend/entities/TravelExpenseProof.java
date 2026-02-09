package com.example.hrms_backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "travel_expense_proofs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelExpenseProof {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "expense_proof_id")
    private UUID expenseProofId;

    @Column(name = "expense_proof_path",nullable = false)
    @NotBlank(message = "Expense Proof Path is required")
    private String expenseProofPath;

    @Column(name = "uploaded_by", nullable = false)
    @NotBlank(message = "Uploaded by is required")
    private UUID uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_expense_id")
    private TravelExpense travelExpense;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
