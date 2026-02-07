package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.Game;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class GamePlayStatsDto {
    private UUID statsId;

    private UUID employeeId;
    private UUID gameId;
    private int completedSlots;
    private LocalDateTime lastPlayedAt;
}
