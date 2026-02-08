package com.example.hrms_backend.services;

import com.example.hrms_backend.entities.Game;
import com.example.hrms_backend.entities.GameTimeSlotConfig;
import com.example.hrms_backend.entities.TimeSlot;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.GameRepo;
import com.example.hrms_backend.repositories.GameTimeSlotConfigRepo;
import com.example.hrms_backend.repositories.TimeSlotRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final GameTimeSlotConfigRepo configRepo;
    private final GameRepo gameRepo;
    private final TimeSlotRepo timeSlotRepo;

    @Transactional
    public void generateSlots(UUID gameId, LocalDate fromDate, LocalDate toDate, UUID createdBy){
        System.out.println("working system");
        GameTimeSlotConfig config = configRepo.findByGame_GameIdAndActiveTrue(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Time slot is not available"));

        Game game = gameRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));

        for (LocalDate date = fromDate;
             !date.isAfter(toDate);
             date = date.plusDays(1)) {
            LocalTime current = config.getConfigStartTime();

            while (current.plusMinutes(config.getSlotDuration())
                    .compareTo(config.getConfigEndTime()) <= 0) {
                TimeSlot slot = new TimeSlot();
                    slot.setGame(game);
                    slot.setSlotDate(date);
                    slot.setStartTime(current);
                    slot.setEndTime(current.plusMinutes(config.getSlotDuration()));
                    slot.setCreatedAt(LocalDateTime.now());
                    slot.setCreatedBy(createdBy);
                try{
                    timeSlotRepo.save(slot);
                }
                catch (DataIntegrityViolationException e) {
                }

                current = current.plusMinutes(config.getSlotDuration());
            }
        }
    }
}
