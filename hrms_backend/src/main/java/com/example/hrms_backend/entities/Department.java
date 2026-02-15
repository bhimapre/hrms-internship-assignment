package com.example.hrms_backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "departments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "department_name")
    @NotBlank(message = "Department Name is required")
    @Size(min = 2, max =  100, message = "Department Name must be between 2 and 100 characters long")
    private String departmentName;

    @Column(name = "created_by")
    private UUID createdBy;

    @DateTimeFormat
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
