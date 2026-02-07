package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.Game;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class TimeSlotsDto {
    private UUID timeSlotId;
    private UUID gameId;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @NotNull(message = "Slot Date is required")
    private LocalDate slotDate;

    @NotNull(message = "Start Time is required")
    private LocalTime startTime;

    @NotNull(message = "End Time is required")
    private LocalTime endTime;
}
