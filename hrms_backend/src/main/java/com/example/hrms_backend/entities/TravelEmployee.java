package com.example.hrms_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "travel_employees",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"travel_id", "employee_id"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "travel_employee_id")
    private UUID travelEmployeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
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
