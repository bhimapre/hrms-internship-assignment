package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.GameDto;
import com.example.hrms_backend.services.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    // Add Game by HR
    @PreAuthorize("hasRole('HR')")
    @PostMapping("/api/hr/add-game")
    public ResponseEntity<GameDto> addGame(@Valid @RequestBody GameDto gameDto){
        GameDto game = gameService.createGame(gameDto);
        return new ResponseEntity<>(game, HttpStatus.CREATED);
    }

    // Get all games only accessible by HR
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/common/game")
    public ResponseEntity<List<GameDto>> getAllGames(){
        List<GameDto> game = gameService.getAllGames();
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    // Get game by id accessible by all
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/common/game/{gameId}")
    public ResponseEntity<GameDto> getGameById(@PathVariable UUID gameId){
        GameDto gameDto = gameService.getGameById(gameId);
        return ResponseEntity.ok(gameDto);
    }
}
