package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.GameWaitingQueueDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.GameBooking;
import com.example.hrms_backend.entities.GameWaitingQueue;
import com.example.hrms_backend.entities.TimeSlot;
import com.example.hrms_backend.entities.enums.BookingStatus;
import com.example.hrms_backend.entities.enums.QueueStatus;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.GameBookingRepo;
import com.example.hrms_backend.repositories.GameWaitingQueueRepo;
import com.example.hrms_backend.repositories.TimeSlotRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameWaitingQueueService {

    private final GameWaitingQueueRepo waitingQueueRepo;
    private final EmployeeRepo employeeRepo;
    private final ModelMapper modelMapper;
    private final GameBookingRepo bookingRepo;
    private final TimeSlotRepo timeSlotRepo;

    public GameWaitingQueueDto joinWaitingQueue(GameWaitingQueueDto waitingQueueDto){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        TimeSlot slot = timeSlotRepo.findById(waitingQueueDto.getTimeSlotId()).orElseThrow(() -> new ResourceNotFoundException("Time slot not found"));

        // Check if employee have preference for this game
        if(!employee.getGamePreferences().contains(slot.getGame())){
            throw new BadRequestException("You must select this game preference before joining the queue");
        }

        // Check that you are not joining past slot
        LocalDateTime slotStartDateTime = LocalDateTime.of(slot.getSlotDate(), slot.getStartTime());
        if(slotStartDateTime.isBefore(LocalDateTime.now())){
            throw new BadRequestException("You cannot join waiting queue for past slots");
        }

        // Check if you are already in queue or not
        boolean alreadyInQueue = waitingQueueRepo.existsByEmployee_EmployeeIdAndTimeSlot_TimeSlotIdAndStatus(employeeId, slot.getTimeSlotId(), QueueStatus.WAITING);
        if(alreadyInQueue){
            throw new BadRequestException("You are already in queue");
        }

        GameWaitingQueue waitingQueue = new GameWaitingQueue();

        // If time slot is empty and booker never played or booked particular game then show message that directly book the slot
        boolean isTimeSlotExist = bookingRepo.existsByTimeSlot_TimeSlotId(waitingQueueDto.getTimeSlotId());
        if(!isTimeSlotExist) {
            LocalDate today = LocalDate.now();
            boolean isBookerPlayedGameToday = bookingRepo.existsByBooker_EmployeeIdAndGame_GameIdAndTimeSlot_SlotDate(employeeId, waitingQueueDto.getGameId(), today);
            if (!isBookerPlayedGameToday) {
                throw new BadRequestException("The slot is empty you can directly book that");
            }
        }

        waitingQueue.setTimeSlot(slot);
        waitingQueue.setEmployee(employee);
        waitingQueue.setStatus(QueueStatus.WAITING);
        waitingQueue.setCreatedAt(LocalDateTime.now());
        waitingQueue.setCreatedBy(employeeId);
        waitingQueue = waitingQueueRepo.save(waitingQueue);

        return modelMapper.map(waitingQueue, GameWaitingQueueDto.class);
    }
}
