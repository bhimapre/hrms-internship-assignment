package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepo extends JpaRepository<Notifications, UUID> {

    List<Notifications> findByReceivedByOrderByCreatedAtDesc(UUID receivedBy);

    long countByReceivedByAndIsReadFalse(UUID receivedBy);
}
