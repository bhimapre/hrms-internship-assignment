package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.GameDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.Game;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.GameRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepo gameRepo;
    private final ModelMapper modelMapper;
    private static final String GAME_NOT_FOUND = "Game not found";
    private final EmployeeRepo employeeRepo;

    // Get All Games
    public List<GameDto> getAllGames(){
        String role = SecurityUtils.getRole();
        if(!role.equals("HR")){
            List<Game> games = gameRepo.findByActive(true);
            return games.stream()
                    .map(emp -> modelMapper.map(emp, GameDto.class))
                    .toList();
        }
        List<Game> games = gameRepo.findAll();
        return games.stream()
                .map(emp -> modelMapper.map(emp, GameDto.class))
                .toList();
    }

    // Get Game By ID
    public GameDto getGameById(UUID gameId) {
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new ResolutionException(GAME_NOT_FOUND));
        return modelMapper.map(game, GameDto.class);
    }

    // Add Gamme
    public GameDto createGame(GameDto gameDto){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();
        Game game = modelMapper.map(gameDto, Game.class);
        game.setActive(true);
        game.setCreatedAt(LocalDateTime.now());
        game.setCreatedBy(employeeId);

        game = gameRepo.save(game);
        return modelMapper.map(game, GameDto.class);
    }

    // Update Game
    public GameDto updateGame(UUID gameId ,GameDto gameDto){
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        game.setGameName(gameDto.getGameName());
        game.setUpdatedBy(employeeId);
        game.setUpdatedAt(LocalDateTime.now());
        game = gameRepo.save(game);
        return  modelMapper.map(game, GameDto.class);
    }

    // Soft Delete
    public GameDto deleteGame(UUID gameId){
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        if(!game.isActive()){
            throw new BadRequestException("Game is already inactive");
        }

        game.setActive(true);
        game.setUpdatedBy(employeeId);
        game.setUpdatedAt(LocalDateTime.now());
        game = gameRepo.save(game);
        return  modelMapper.map(game, GameDto.class);
    }
}
