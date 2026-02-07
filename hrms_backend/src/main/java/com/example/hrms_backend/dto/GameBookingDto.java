package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.Game;
import com.example.hrms_backend.entities.TimeSlot;
import com.example.hrms_backend.entities.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
public class GameBookingDto {
    private UUID gameBookingId;
    private UUID timeSlotId;
    private UUID gameId;
    private UUID bookerId;
    private BookingStatus bookingStatus;
}
