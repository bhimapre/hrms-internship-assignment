package com.example.hrms_backend.entities;

import com.example.hrms_backend.entities.enums.TravelStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "travels")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "travel_id")
    private UUID travelId;

    @NotBlank(message = "Travel Title required")
    @Size(min = 5, max = 50, message = "Travel title must be between 5 and 50 characters long")
    @Column(name = "travel_Title", nullable = false)
    private String travelTitle;

    @NotNull(message = "Travel Date From required")
    @Column(name = "travel_date_from", nullable = false)
    private LocalDate travelDateFrom;

    @NotNull(message = "Travel Date To required")
    @Column(name = "travel_date_to", nullable = false)
    private LocalDate travelDateTo;

    @NotBlank(message = "Travel Location required")
    @Column(name = "travel_location", nullable = false)
    private String travelLocation;

    @Column(name = "travel_details", nullable = false)
    @NotBlank(message = "Travel Details is required")
    @Size(min = 10, max =  500, message = "Travel Details must be between 10 and 500 characters long")
    private String travelDetails;

    @Enumerated(EnumType.STRING)
    @Column(name = "travel_status", nullable = false)
    private TravelStatus travelStatus;

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TravelEmployee> travelEmployees;

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TravelExpense> travelExpenses;

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TravelDocument> travelDocuments;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
