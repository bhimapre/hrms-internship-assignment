package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.Game;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeSlotsDto {
    private UUID timeSlotId;
    private UUID gameId;

    @NotNull(message = "Slot Date is required")
    private LocalDate slotDate;

    @NotNull(message = "Start Time is required")
    private LocalTime startTime;

    @NotNull(message = "End Time is required")
    private LocalTime endTime;
}
