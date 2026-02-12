package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.GameTimeSlotConfigDto;
import com.example.hrms_backend.entities.GameTimeSlotConfig;
import com.example.hrms_backend.services.GameTimeSlotConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GameTimeSlotConfigController {

    private final GameTimeSlotConfigService gameTimeSlotConfigService;

    // create time slot config
    @PreAuthorize("hasAuthority('HR')")
    @PostMapping("/api/hr/add-game-time-slot-config")
    public ResponseEntity<GameTimeSlotConfigDto> createTimeSlotConfig(@Valid @RequestBody GameTimeSlotConfigDto dto){

        GameTimeSlotConfigDto config = gameTimeSlotConfigService.createGameTimeConfig(dto);
        return ResponseEntity.ok(config);
    }

    // Update time slot config
    @PreAuthorize("hasAuthority('HR')")
    @PutMapping("/api/hr/game-time-slot-config/{gameId}")
    public ResponseEntity<GameTimeSlotConfigDto> updateTimeSlotConfig(@PathVariable UUID gameId, @Valid @RequestBody GameTimeSlotConfigDto dto){

        GameTimeSlotConfigDto config = gameTimeSlotConfigService.updateGameTimeSlotConfig(gameId, dto);
        return ResponseEntity.ok(config);
    }
}
