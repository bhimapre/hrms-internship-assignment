package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.GameTimeSlotConfigDto;
import com.example.hrms_backend.dto.TimeSlotGenerationDto;
import com.example.hrms_backend.dto.TimeSlotsDto;
import com.example.hrms_backend.repositories.TimeSlotRepo;
import com.example.hrms_backend.services.CustomUserDetails;
import com.example.hrms_backend.services.GameTimeSlotConfigService;
import com.example.hrms_backend.services.TimeSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TimeSlotController {

    private final GameTimeSlotConfigService gameTimeSlotConfigService;
    private final TimeSlotRepo timeSlotRepo;
    private final TimeSlotService timeSlotService;

    // generate slots based on start and end date
    @PreAuthorize("hasRole('HR')")
    @PostMapping("/api/hr/slots-generate/{gameId}")
    public ResponseEntity<String> generateSlots(
            @PathVariable UUID gameId,
            @Valid @RequestBody TimeSlotGenerationDto request,
            Authentication authentication
    ) {
        UUID createdBy =
                ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        timeSlotService.generateSlots(
                gameId,
                request.getFromDate(),
                request.getToDate(),
                createdBy
        );

        return ResponseEntity.ok("Time slots generated successfully");
    }

    // Get time slots based on game id and date
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/game-slots/{gameId}")
    public ResponseEntity<List<TimeSlotsDto>> getSlotsByDate(
            @PathVariable UUID gameId,
            @RequestParam LocalDate date
    ) {
        List<TimeSlotsDto> slots = timeSlotRepo
                .findSlotsByGameAndDate(gameId, date)
                .stream()
                .map(slot -> new TimeSlotsDto(
                        slot.getTimeSlotId(),
                        slot.getGame().getGameId(),
                        slot.getSlotDate(),
                        slot.getStartTime(),
                        slot.getEndTime()
                ))
                .toList();

        return ResponseEntity.ok(slots);
    }
}
