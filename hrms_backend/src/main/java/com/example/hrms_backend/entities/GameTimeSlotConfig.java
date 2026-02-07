package com.example.hrms_backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "game_time_slot_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameTimeSlotConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "config_id")
    private UUID configId;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @NotNull(message = "Start Time is required")
    @Column(name = "config_start_time", nullable = false)
    private LocalTime configStartTime;

    @NotNull(message = "End Time is required")
    @Column(name = "config_end_time", nullable = false)
    private LocalTime configEndTime;

    @NotNull(message = "Slot Duration is required")
    @Column(name = "slot_duration", nullable = false)
    private int slotDuration;

    @NotNull(message = "Max players field is required")
    @Column(name = "max_players", nullable = false)
    private int maxPlayers;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;
}
