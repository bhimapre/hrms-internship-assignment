package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.enums.BookingStatus;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateGameBookingDto {
    private UUID timeSlotId;
    private UUID gameId;
    private List<UUID> memberIds;
}
