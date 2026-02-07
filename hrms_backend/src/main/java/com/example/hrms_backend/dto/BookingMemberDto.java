package com.example.hrms_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingMemberDto {
    private UUID bookingMemberId;
    private UUID gameBookingId;
    private UUID employeeID;
    private LocalDateTime joinedAt;
}
