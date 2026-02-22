package com.example.hrms_backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PriorityQueueDto {
    UUID getQueueId();
    UUID getEmployeeId();
    UUID getGameId();
    UUID getTimeSlotId();

    Integer getWeeklyCount();
    Integer getCompletedSlots();
    LocalDateTime getLastPlayedAt();
}
