package com.example.hrms_backend.entities;

import com.example.hrms_backend.entities.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notification_id")
    private UUID notificationId;

    @NotBlank(message = "Title is required")
    @Column(name = "notification_title", nullable = false)
    private String notificationTitle;

    @NotBlank(message = "Message is required")
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull(message = "Notification type is required")
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @NotNull(message = "Received by is required")
    @Column(name = "received_by", nullable = false)
    private UUID receivedBy;

    @NotNull(message = "Is read is required")
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;
}
