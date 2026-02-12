package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.BookingMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface GameBookingMemberRepo extends JpaRepository<BookingMember, UUID> {

    boolean existsByTimeSlot_Game_GameIdAndTimeSlot_SlotDateAndEmployee_EmployeeIdIn(UUID gameId, LocalDate slotDate, List<UUID> bookingPlayerIds);

    boolean existsByTimeSlot_StartTimeAndTimeSlot_EndTimeAndTimeSlot_SlotDateAndEmployee_EmployeeIdIn(LocalTime timeSlotStartTime, LocalTime timeSlotEndTime, LocalDate timeSlotSlotDate, List<UUID> employeeIds);
}
