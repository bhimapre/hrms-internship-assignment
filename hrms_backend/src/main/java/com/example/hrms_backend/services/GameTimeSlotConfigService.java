package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.GameTimeSlotConfigDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.Game;
import com.example.hrms_backend.entities.GameTimeSlotConfig;
import com.example.hrms_backend.entities.TimeSlot;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.GameRepo;
import com.example.hrms_backend.repositories.GameTimeSlotConfigRepo;
import com.example.hrms_backend.repositories.TimeSlotRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameTimeSlotConfigService {
    private final GameTimeSlotConfigRepo configRepo;
    private final GameRepo gameRepo;
    private final ModelMapper modelMapper;
    private final EmployeeRepo employeeRepo;

    // create time slot config
    public GameTimeSlotConfigDto createGameTimeConfig(GameTimeSlotConfigDto gameTimeSlotConfigDto){

        UUID userId = SecurityUtils.getCurrentUserId();
        String role = SecurityUtils.getRole();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        if(!role.equals("HR")){
            throw new AccessDeniedException("You have no access of it");
        }
        GameTimeSlotConfig config = modelMapper.map(gameTimeSlotConfigDto, GameTimeSlotConfig.class);
        Game game = gameRepo.findById(gameTimeSlotConfigDto.getGameId()).orElseThrow(() -> new ResourceNotFoundException("Game not found"));

        if(configRepo.findByGame_GameIdAndActiveTrue(game.getGameId()).isPresent()){
            throw new IllegalStateException("Game already exist in configuration");
        }

        config.setGame(game);
        config.setCreatedAt(LocalDateTime.now());
        config.setCreatedBy(employeeId);

        GameTimeSlotConfig saved = configRepo.save(config);
        return modelMapper.map(saved, GameTimeSlotConfigDto.class);
    }

    // Update game time slot config
    public GameTimeSlotConfigDto updateGameTimeSlotConfig(UUID gameId, GameTimeSlotConfigDto gameTimeSlotConfigDto){

        UUID userId = SecurityUtils.getCurrentUserId();
        String role = SecurityUtils.getRole();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        if(!role.equals("HR")){
            throw new AccessDeniedException("You have no access of it");
        }

        GameTimeSlotConfig config = configRepo.findByGame_GameId(gameId).orElseThrow(() -> new ResourceNotFoundException("Time Slot config not found for this game"));

        config.setConfigStartTime(gameTimeSlotConfigDto.getConfigStartTime());
        config.setConfigEndTime(gameTimeSlotConfigDto.getConfigEndTime());
        config.setSlotDuration(gameTimeSlotConfigDto.getSlotDuration());
        config.setMaxPlayers(gameTimeSlotConfigDto.getMaxPlayers());
        config.setUpdatedAt(LocalDateTime.now());
        config.setUpdatedBy(employeeId);

        GameTimeSlotConfig saved = configRepo.save(config);
        return modelMapper.map(saved, GameTimeSlotConfigDto.class);
    }
}
