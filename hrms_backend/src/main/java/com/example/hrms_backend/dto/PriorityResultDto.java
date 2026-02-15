package com.example.hrms_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class PriorityResultDto {

    private UUID queueId;
    private UUID employeeId;
    private UUID gameId;
    private UUID timeSlotId;
    private Integer weeklyCount;
    private Integer completedSlots;
    private LocalDateTime lastPlayedAt;
    private LocalDateTime createdAt;
}
