package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.GameBooking;
import com.example.hrms_backend.entities.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

public interface GameBookingRepo extends JpaRepository<GameBooking, UUID> {
    boolean existsByTimeSlot_TimeSlotId(UUID timeSlotTimeSlotId);

    boolean existsByBooker_EmployeeIdAndGame_GameIdAndTimeSlot_SlotDate(UUID bookerEmployeeId, UUID gameGameId, LocalDate timeSlotSlotDate);

    boolean existsByBooker_EmployeeIdAndTimeSlot_StartTimeAndTimeSlot_EndTimeAndTimeSlot_SlotDate(UUID bookerEmployeeId, LocalTime timeSlotStartTime, LocalTime timeSlotEndTime, LocalDate timeSlotSlotDate);

    GameBooking findByTimeSlot_TimeSlotId(UUID timeSlotId);
}
