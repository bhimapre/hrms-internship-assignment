package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.GameWaitingQueue;
import com.example.hrms_backend.entities.enums.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameWaitingQueueRepo extends JpaRepository<GameWaitingQueue, UUID> {
    boolean existsByEmployee_EmployeeIdAndTimeSlot_TimeSlotIdAndStatus(UUID employeeId, UUID timeSlotId, QueueStatus queueStatus);
}
