package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.GameWaitingQueueDto;
import com.example.hrms_backend.dto.PriorityQueueDto;
import com.example.hrms_backend.entities.*;
import com.example.hrms_backend.entities.enums.BookingStatus;
import com.example.hrms_backend.entities.enums.QueueStatus;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.*;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameWaitingQueueService {

    private final GameWaitingQueueRepo waitingQueueRepo;
    private final EmployeeRepo employeeRepo;
    private final ModelMapper modelMapper;
    private final GameBookingRepo bookingRepo;
    private final TimeSlotRepo timeSlotRepo;
    private final GameTimeSlotConfigRepo configRepo;
    private final GameBookingMemberRepo bookingMemberRepo;

    public GameWaitingQueueDto joinWaitingQueue(GameWaitingQueueDto waitingQueueDto){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        TimeSlot slot = timeSlotRepo.findById(waitingQueueDto.getTimeSlotId()).orElseThrow(() -> new ResourceNotFoundException("Time slot not found"));

//        // Check if employee have preference for this game
//        if(!employee.getGamePreferences().contains(slot.getGame())){
//            throw new BadRequestException("You must select this game preference before joining the queue");
//        }

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

        // If booker already booked same slots
        boolean alreadyBookedSameSlot = bookingRepo.existsBookerOverlappingWaiting(employeeId, slot.getSlotDate(), slot.getStartTime(), slot.getEndTime());
        if(alreadyBookedSameSlot){
            throw new BadRequestException("You already have booked this slot for other games");
        }

        waitingQueue.setGame(slot.getGame());
        waitingQueue.setTimeSlot(slot);
        waitingQueue.setEmployee(employee);
        waitingQueue.setStatus(QueueStatus.WAITING);
        waitingQueue.setCreatedAt(LocalDateTime.now());
        waitingQueue.setCreatedBy(employeeId);
        waitingQueue = waitingQueueRepo.save(waitingQueue);

        return modelMapper.map(waitingQueue, GameWaitingQueueDto.class);
    }

    // Allocate the priority
    @Transactional
    public void allocate(TimeSlot timeSlot) {

        GameTimeSlotConfig config = configRepo
                .findByGameAndActiveTrue(timeSlot.getGame())
                .orElseThrow(() -> new IllegalStateException("GameTimeSlotConfig not found"));

        int maxPlayers = config.getMaxPlayers();

        LocalDateTime slotStart = timeSlot.getSlotDate().atTime(timeSlot.getStartTime());
        if(slotStart.isBefore(LocalDateTime.now())){
            log.warn("Skipping allocation for past slot {}", timeSlot.getTimeSlotId());
            return;
        }

        //  Priority calculation
        List<PriorityQueueDto> priorityList =
                waitingQueueRepo.calculatePriority(
                        timeSlot.getGame().getGameId(),
                        timeSlot.getTimeSlotId());

        if (priorityList.isEmpty()) {
            log.info("No waiting queue is waiting");
            return;
        }

        //  Select players
        List<PriorityQueueDto> selected = priorityList.stream()
                .collect(Collectors.toMap(
                        PriorityQueueDto::getEmployeeId,
                        dto -> dto,
                        (existing, duplicate) -> existing
                ))
                .values()
                .stream()
                .limit(maxPlayers)
                .toList();

        if(selected.isEmpty()){
            log.info("No player found");
            return;
        }

        //  Create booking
        GameBooking booking = new GameBooking();
        booking.setGame(timeSlot.getGame());
        booking.setTimeSlot(timeSlot);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setCreatedAt(LocalDateTime.now());

        PriorityQueueDto priorityQueueDto = selected.getFirst();
        Employee booker = employeeRepo.findById(priorityQueueDto.getEmployeeId()).orElseThrow(() -> new ResourceNotFoundException("Booker not found"));

        booking.setBooker(booker);
        booking = bookingRepo.save(booking);

        //  Booking members
        for (int i = 1; i < selected.size(); i++) {
            Employee emp = employeeRepo.findById(selected.get(i).getEmployeeId()).orElseThrow(() -> new ResourceNotFoundException("Member not found"));

            BookingMember member = new BookingMember();
            member.setGameBooking(booking);
            member.setTimeSlot(timeSlot);
            member.setEmployee(emp);
            member.setCreatedAt(LocalDateTime.now());

            bookingMemberRepo.save(member);
        }

        //  Update queue statuses
        List<UUID> allocatedQueueIds =
                selected.stream()
                        .map(PriorityQueueDto::getQueueId)
                        .toList();

        waitingQueueRepo.updateStatus(allocatedQueueIds, QueueStatus.ALLOCATED);
        waitingQueueRepo.cancelRemaining(timeSlot.getTimeSlotId(), allocatedQueueIds);
    }
}
