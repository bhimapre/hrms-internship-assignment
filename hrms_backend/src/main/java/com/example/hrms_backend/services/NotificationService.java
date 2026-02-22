package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.NotificationDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.Notifications;
import com.example.hrms_backend.entities.enums.NotificationType;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.NotificationRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final ModelMapper modelMapper;
    private final EmployeeRepo employeeRepo;

    public void sendNotification(UUID employeeId, String title, String message, NotificationType notificationType){
        Notifications notifications = new Notifications();

        notifications.setReceivedBy(employeeId);
        notifications.setMessage(message);
        notifications.setNotificationTitle(title);
        notifications.setNotificationType(notificationType);
        notifications.setCreatedAt(LocalDateTime.now());
        notifications = notificationRepo.save(notifications);
    }

    // Fetch my notifications
    public List<Notifications> getMyNotifications(){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employee_Id = employee.getEmployeeId();

        return  notificationRepo.findByReceivedByOrderByCreatedAtDesc(employee_Id);
    }

    // Get unread Count
    public long getUnreadCount(){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employee_Id = employee.getEmployeeId();

        return notificationRepo.countByReceivedByAndIsReadFalse(employee_Id);
    }

    // Mark as read Notification
    public void markAsRead(UUID notificationId){
        Notifications notifications = notificationRepo.findById(notificationId).orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        if(!notifications.getIsRead()){
            notifications.setIsRead(true);
            notifications.setReadAt(LocalDateTime.now());
            notificationRepo.save(notifications);
        }
    }
}
