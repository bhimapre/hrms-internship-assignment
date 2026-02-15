package com.example.hrms_backend.dto;

import com.example.hrms_backend.entities.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class NotificationDto {

    private String notificationTitle;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Notification type is required")
    private NotificationType notificationType;
    private UUID receivedBy;
}
