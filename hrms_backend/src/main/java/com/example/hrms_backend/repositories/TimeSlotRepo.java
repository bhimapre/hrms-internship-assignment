package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface TimeSlotRepo extends JpaRepository<TimeSlot, UUID> {

    // Get slots based on game id and time
    @Query("""
    select t from TimeSlot t where t.game.gameId = :gameId
    and t.slotDate = :slotDate order by t.startTime
""")
    List<TimeSlot> findSlotsByGameAndDate(@Param("gameId") UUID gameId,
        @Param("slotDate") LocalDate slotDate);

    @Query("""
    select ts from TimeSlot ts where ts.slotDate = :date
    and ts.startTime >= :startTime and ts.startTime <= :endTime
""")
    List<TimeSlot> findBySlotDateAndStartTimeBetween(LocalDate slotDate, LocalTime startTimeAfter, LocalTime startTimeBefore);

    @Query("""
    select ts from TimeSlot ts where ts.slotDate = :date
    and ts.startTime >= :startTime and ts.startTime <= :endTime
""")
    List<TimeSlot> findUpcomingSlots(@Param("date") LocalDate date,
                                     @Param("startTime") LocalTime startTime,
                                     @Param("endTime") LocalTime endTime);

    // Find slots start
    @Query(value = """
        SELECT *
        FROM time_slots ts
        WHERE CAST(ts.slot_date AS DATETIME)
            + CAST(ts.start_time AS DATETIME)
            <= :oneHourLater
    """, nativeQuery = true)
    List<TimeSlot> findSlotsStartingWithin(LocalDateTime oneHourLater);
}
