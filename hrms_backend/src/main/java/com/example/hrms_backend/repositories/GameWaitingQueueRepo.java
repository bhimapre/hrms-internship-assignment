package com.example.hrms_backend.repositories;

import com.example.hrms_backend.dto.PriorityQueueDto;
import com.example.hrms_backend.dto.PriorityResultDto;
import com.example.hrms_backend.entities.Game;
import com.example.hrms_backend.entities.GameWaitingQueue;
import com.example.hrms_backend.entities.enums.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface GameWaitingQueueRepo extends JpaRepository<GameWaitingQueue, UUID> {
    boolean existsByEmployee_EmployeeIdAndTimeSlot_TimeSlotIdAndStatus(UUID employeeId, UUID timeSlotId, QueueStatus queueStatus);

    List<GameWaitingQueue> findByGame_GameIdAndTimeSlot_TimeSlotIdAndStatus(UUID gameGameId, UUID timeSlotTimeSlotId, QueueStatus status);

    @Procedure(procedureName = "sp_calculate_priority")
    List<PriorityQueueDto> calculatePriority(@Param("GameId") UUID gameId,
                                             @Param("TimeSlotId") UUID timeSlotId);


    @Modifying
    @Query("""
        UPDATE GameWaitingQueue q
        SET q.status = :status
        WHERE q.queueId IN :queueIds
    """)
    void updateStatus(
            @Param("queueIds") List<UUID> queueIds,
            @Param("status") QueueStatus status
    );

    @Modifying
    @Query("""
        UPDATE GameWaitingQueue q
        SET q.status = 'CANCELLED'
        WHERE q.timeSlot.timeSlotId = :timeSlotId
          AND q.status = 'WAITING'
          AND q.queueId NOT IN :allocatedIds
    """)
    void cancelRemaining(
            @Param("timeSlotId") UUID timeSlotId,
            @Param("allocatedIds") List<UUID> allocatedIds
    );
}
