package com.example.hrms_backend.entities;

import com.example.hrms_backend.entities.enums.ExpenseStatus;
import com.example.hrms_backend.entities.enums.TravelExpenseCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.classfile.Label;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "travel_expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "travel_expense_id")
    private UUID travelExpenseId;

    @Column(name = "expense_name", nullable = false)
    @NotBlank(message = "Expense Name is required")
    @Size(min = 2, max =  100, message = "Expense Name must be between 2 and 100 characters long")
    private String expenseName;

    @Column(name = "expense_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @NotBlank(message = "Expense date is required")
    private LocalDate expenseDate;

    @NotNull(message = "Expense amount is required")
    @Positive(message = "Amount must be positive value")
    @Column(name = "expense_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal expenseAmount;

    @Column(name = "expense_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Enum<TravelExpenseCategory> expenseCategory;

    @Column(name = "expense_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Enum<ExpenseStatus> expenseStatus;

    @Column(name = "hr_remark")
    private String hrRemark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @OneToMany(mappedBy = "travelExpense")
    private List<TravelExpenseProof> travelExpenseProofs;

    @Column(name = "approved_by")
    private UUID approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
