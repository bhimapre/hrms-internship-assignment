package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.GameWaitingQueueDto;
import com.example.hrms_backend.services.GameWaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GameWaitingQueueController {

    private final GameWaitingQueueService waitingQueueService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/waiting-queue")
    public ResponseEntity<GameWaitingQueueDto> joinWaitingQueue(@RequestBody GameWaitingQueueDto waitingQueueDto){
        return ResponseEntity.ok(waitingQueueService.joinWaitingQueue(waitingQueueDto));
    }
}
