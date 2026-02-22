package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.GameBooking;
import com.example.hrms_backend.entities.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameBookingRepo extends JpaRepository<GameBooking, UUID> {
    boolean existsByTimeSlot_TimeSlotId(UUID timeSlotTimeSlotId);

    boolean existsByBooker_EmployeeIdAndGame_GameIdAndTimeSlot_SlotDate(UUID bookerEmployeeId, UUID gameGameId, LocalDate timeSlotSlotDate);

    boolean existsByBooker_EmployeeIdAndTimeSlot_StartTimeAndTimeSlot_EndTimeAndTimeSlot_SlotDate(UUID bookerEmployeeId, LocalTime timeSlotStartTime, LocalTime timeSlotEndTime, LocalDate timeSlotSlotDate);

    List<GameBooking> findByTimeSlot_SlotDate(LocalDate timeSlotSlotDate);

    @Procedure(name = "sp_calculate_priority")
    Optional<GameBooking> getGameBookingFromWaitingList(@Param("game_id") UUID gameId, @Param("time_slot_id") UUID timeSlotId);

    List<GameBooking> findByBooker_EmployeeIdAndTimeSlot_SlotDateGreaterThanEqual(UUID employeeId, LocalDate date);

    boolean existsByBooker_EmployeeIdAndTimeSlot_SlotDateAndTimeSlot_StartTimeLessThanAndTimeSlot_EndTimeGreaterThan(UUID employeeId, LocalDate slotDate, LocalTime endTime, LocalTime startTime);

    boolean existsByBooker_EmployeeIdAndTimeSlot_TimeSlotId(UUID employeeId, UUID timeSlotId);

    @Query(
            value = """
            SELECT CASE 
                WHEN EXISTS (
                    SELECT 1
                    FROM game_bookings gb
                    INNER JOIN time_slots ts 
                        ON ts.time_slot_id = gb.time_slot_id
                    WHERE gb.booker_id = :employeeId
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
    boolean existsBookerOverlappingBooking(
            UUID employeeId,
            LocalDate slotDate,
            LocalTime startTime,
            LocalTime endTime
    );


    @Query(
            value = """
        SELECT CASE
            WHEN EXISTS (
                SELECT 1
                FROM game_bookings gb
                INNER JOIN time_slots ts
                    ON ts.time_slot_id = gb.time_slot_id
                WHERE gb.booker_id = :employeeId
                  AND gb.booking_status = 'CONFIRMED'
                  AND ts.slot_date = :slotDate
                  AND (
                        ts.start_time < CAST(:endTime AS TIME)
                    AND ts.end_time > CAST(:startTime AS TIME)
                  )
            )
            THEN CAST(1 AS BIT)
            ELSE CAST(0 AS BIT)
        END
        """,
            nativeQuery = true
    )
    boolean existsBookerOverlappingWaiting(
            UUID employeeId,
            LocalDate slotDate,
            LocalTime startTime,
            LocalTime endTime
    );

    @Query(
            value = """
            SELECT gb.*
            FROM game_bookings gb
            INNER JOIN time_slots ts
                ON ts.time_slot_id = gb.time_slot_id
            WHERE gb.booking_status = 'CONFIRMED'
              AND (
                    CAST(ts.slot_date AS DATETIME)
                    + CAST(ts.end_time AS DATETIME)
                  ) < :now
            """,
            nativeQuery = true
    )
    List<GameBooking> findConfirmedBookingsWithEndedSlot(@Param("now") LocalDateTime now);
}
