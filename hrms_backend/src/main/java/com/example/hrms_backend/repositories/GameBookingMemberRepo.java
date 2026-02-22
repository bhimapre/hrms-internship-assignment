package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.BookingMember;
import com.example.hrms_backend.entities.GameBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
SELECT COUNT(bm) > 0
FROM BookingMember bm
WHERE bm.employee.employeeId IN :employeeIds
AND bm.gameBooking.game.gameId = :gameId
AND bm.gameBooking.bookingStatus = 'CONFIRMED'
AND bm.timeSlot.slotDate = :slotDate
""")
    boolean existsMemberPlayedGameToday(
            @Param("employeeIds") List<UUID> employeeIds,
            @Param("gameId") UUID gameId,
            @Param("slotDate") LocalDate slotDate
    );


    @Query(
            value = """
            SELECT CASE 
                WHEN EXISTS (
                    SELECT 1
                    FROM booking_members bm
                    INNER JOIN game_bookings gb 
                        ON gb.game_booking_id = bm.game_booking_id
                    INNER JOIN time_slots ts 
                        ON ts.time_slot_id = bm.time_slot_id
                    WHERE bm.employee_id IN (:employeeIds)
                      AND gb.booking_status = 'CONFIRMED'
                      AND ts.slot_date = :slotDate
                      AND (
                            DATEDIFF(SECOND, '00:00:00', ts.start_time)
                                < DATEDIFF(SECOND, '00:00:00', :endTime)
                        AND DATEDIFF(SECOND, '00:00:00', ts.end_time)
                                > DATEDIFF(SECOND, '00:00:00', :startTime)
                      )
                )
                THEN CAST(1 AS BIT)
                ELSE CAST(0 AS BIT)
            END
            """,
            nativeQuery = true
    )
    boolean existsMemberOverlappingBooking(
            List<UUID> employeeIds,
            LocalDate slotDate,
            LocalTime startTime,
            LocalTime endTime
    );
}
