package com.example.hrms_backend.repositories;

import com.example.hrms_backend.dto.PriorityResultDto;
import com.example.hrms_backend.entities.Game;
import com.example.hrms_backend.entities.GameWaitingQueue;
import com.example.hrms_backend.entities.enums.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
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
    List<PriorityResultDto> calculatePriority(@Param("GameId") UUID gameId,
                                              @Param("TimeSlotId") UUID timeSlotId,
                                              @Param("MaxPlayer") Integer maxPlayer);


    @Transactional
    @Query("""
    update GameWaitingQueue w
    set w.status = :status,
    w.updatedAt = current timestamp 
    where w.queueId = :ququeId
""")

    void updateStatus(@Param("queueId") UUID queueId,
                      @Param("status") QueueStatus status);
}
