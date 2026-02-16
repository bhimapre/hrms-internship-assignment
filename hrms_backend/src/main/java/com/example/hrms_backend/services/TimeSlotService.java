package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.TimeSlotsDto;
import com.example.hrms_backend.entities.*;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.*;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Security;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final GameTimeSlotConfigRepo configRepo;
    private final GameRepo gameRepo;
    private final TimeSlotRepo timeSlotRepo;
    private final EmployeeRepo employeeRepo;
    private final GameBookingRepo gameBookingRepo;

    @Transactional
    public void generateSlots(UUID gameId, LocalDate fromDate, LocalDate toDate){

        UUID userId = SecurityUtils.getCurrentUserId();
        String role = SecurityUtils.getRole();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        if(!role.equals("HR")){
            throw new AccessDeniedException("You have no access of it");
        }

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
                    slot.setCreatedBy(employeeId);
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
