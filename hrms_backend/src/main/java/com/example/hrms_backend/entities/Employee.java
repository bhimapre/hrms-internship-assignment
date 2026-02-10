package com.example.hrms_backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id")
    private UUID employeeId;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max =  100, message = "Name must be between 2 and 100 characters long")
    private String name;

    @Email(message = "Email must be in formate")
    @Column(name = "email", unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    @Size(min = 7, max =  50, message = "Email must be between 7 and 50 characters long")
    private String email;

    @Column(name = "dob", nullable = false)
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @NotNull(message = "Date of Birth is required")
    private LocalDate dob;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "City is required")
    @Size(min = 2, max =  100, message = "City must be between 2 and 100 characters long")
    private String city;

    @Column(name = "phone_Number", nullable = false)
    @Pattern(regexp = "^[6-9]\\\\d{9}$", message = "Invalid phone number format")
    @NotBlank(message = "Phone Number is required")
    @Size(min = 10, max =  10, message = "Phone Number size must be 10 digits")
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "Address is required")
    @Size(min = 10, max =  500, message = "Address must be between 10 and 500 characters long")
    private String address;

    @Column(name = "joining_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "designation", nullable = false)
    @NotBlank(message = "Designation is required")
    @Size(min = 2, max =  100, message = "Designation must be between 2 and 100 characters long")
    private String designation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @ManyToMany()
    @JoinTable(
            name = "employee_game_preferences",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Set<Game> gamePreferences = new HashSet<>();

    @Column(name = "photo_path")
    private String photoPath;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
