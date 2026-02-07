package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.Game;
import com.example.hrms_backend.entities.TimeSlot;
import com.example.hrms_backend.entities.enums.QueueStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class GameWaitingQueueDto {
    private UUID queueId;
    private UUID timeSlotId;
    private UUID gameId;
    private UUID employeeId;
    private QueueStatus status;
    private LocalDateTime createdAt;
}
