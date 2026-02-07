package com.example.hrms_backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "time_slots",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"game_id", "slot_date", "start_time"})
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "time_slot_id")
    private UUID timeSlotId;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @NotNull(message = "Slot Date is required")
    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @NotNull(message = "Start Time is required")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End Time is required")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;
}
