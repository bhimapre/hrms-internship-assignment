package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class BookingDetailsDto {

    private UUID gameBookingId;
    private String gameName;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus bookingStatus;
    private List<BookingParticipantsDto> participants;
}
