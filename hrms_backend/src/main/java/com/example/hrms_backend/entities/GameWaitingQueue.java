package com.example.hrms_backend.entities;

import com.example.hrms_backend.entities.enums.QueueStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_waiting_queue",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"time_slot_id", "employee_id"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameWaitingQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID queueId;

    @ManyToOne
    @JoinColumn(name = "time_slot_id", nullable = false)
    private TimeSlot timeSlot;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull(message = "Queue Status is Required")
    @Column(name = "queue_status")
    @Enumerated(EnumType.STRING)
    private QueueStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
