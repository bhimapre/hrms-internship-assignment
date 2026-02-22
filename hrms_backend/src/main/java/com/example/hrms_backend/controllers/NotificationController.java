package com.example.hrms_backend.controllers;

import com.example.hrms_backend.entities.Notifications;
import com.example.hrms_backend.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Get my notification
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/notifications/my")
    public ResponseEntity<List<Notifications>> getMyNotifications(){
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }

    // Get unread count
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/notifications/unread-count")
    public ResponseEntity<Long> getUnreadCount(){
        return ResponseEntity.ok(notificationService.getUnreadCount());
    }

    // Mark notification as read
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/api/notifications/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID notificationId){
        notificationService.markAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }
}
