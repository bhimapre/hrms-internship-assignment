package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
}
