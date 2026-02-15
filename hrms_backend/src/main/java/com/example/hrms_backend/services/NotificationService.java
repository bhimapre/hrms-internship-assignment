package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.NotificationDto;
import com.example.hrms_backend.entities.Notifications;
import com.example.hrms_backend.entities.enums.NotificationType;
import com.example.hrms_backend.repositories.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final ModelMapper modelMapper;

    public void sendNotification(UUID employeeId, String title, String message, NotificationType notificationType){
        Notifications notifications = new Notifications();

        notifications.setReceivedBy(employeeId);
        notifications.setMessage(message);
        notifications.setNotificationTitle(title);
        notifications.setNotificationType(notificationType);
        notifications.setCreatedAt(LocalDateTime.now());
        notifications = notificationRepo.save(notifications);
    }
}
