package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.BookingMember;
import com.example.hrms_backend.entities.GameBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface GameBookingMemberRepo extends JpaRepository<BookingMember, UUID> {

    boolean existsByTimeSlot_Game_GameIdAndTimeSlot_SlotDateAndEmployee_EmployeeIdIn(UUID gameId, LocalDate slotDate, List<UUID> bookingPlayerIds);

    boolean existsByTimeSlot_StartTimeAndTimeSlot_EndTimeAndTimeSlot_SlotDateAndEmployee_EmployeeIdIn(LocalTime timeSlotStartTime, LocalTime timeSlotEndTime, LocalDate timeSlotSlotDate, List<UUID> employeeIds);

    List<BookingMember> findByGameBooking(GameBooking gameBooking);

    List<BookingMember> findByGameBooking_GameBookingId(UUID gameBookingId);

    List<BookingMember> findByEmployee_EmployeeIdAndTimeSlot_SlotDateGreaterThanEqual(UUID employeeId, LocalDate date);

    boolean existsByEmployee_EmployeeIdInAndTimeSlot_SlotDateAndTimeSlot_StartTimeLessThanAndTimeSlot_EndTimeGreaterThan(List<UUID> employeeIds, LocalDate slotDate, LocalTime endTime, LocalTime startTime);
}
