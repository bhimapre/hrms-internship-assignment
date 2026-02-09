package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.enums.ExpenseStatus;
import com.example.hrms_backend.entities.enums.TravelExpenseCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TravelExpenseDto {

    private UUID travelExpenseId;

    @NotBlank(message = "Expense Name is required")
    @Size(min = 2, max =  100, message = "Expense Name must be between 2 and 100 characters long")
    private String expenseName;

    @NotBlank(message = "Expense date is required")
    private LocalDate expenseDate;

    @Positive(message = "Amount must be positive value")
    @NotNull(message = "Expense amount is required")
    private BigDecimal expenseAmount;

    @Enumerated(EnumType.STRING)
    private Enum<TravelExpenseCategory> expenseCategory;

    @Enumerated(EnumType.STRING)
    private Enum<ExpenseStatus> expenseStatus;

    private String hrRemark;
    private UUID travelId;
    private UUID approvedBy;
    private LocalDateTime approvedAt;
    private UUID employeeId;
}
