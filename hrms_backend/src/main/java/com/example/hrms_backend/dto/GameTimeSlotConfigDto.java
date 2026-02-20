package com.example.hrms_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class GameTimeSlotConfigDto {
    private UUID configId;
    private UUID gameId;

    @NotNull(message = "Start Time is required")
    private LocalTime configStartTime;

    @NotNull(message = "End Time is required")
    private LocalTime configEndTime;

    @NotNull(message = "Slot Duration is required")
    private int slotDuration;

    @NotNull(message = "Max players field is required")
    private int maxPlayers;

    private String gameName;

    private boolean active;
}
