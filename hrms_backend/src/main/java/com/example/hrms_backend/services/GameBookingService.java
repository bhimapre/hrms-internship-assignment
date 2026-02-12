package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.CreateGameBookingDto;
import com.example.hrms_backend.dto.GameBookingDto;
import com.example.hrms_backend.entities.*;
import com.example.hrms_backend.entities.enums.BookingStatus;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.*;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameBookingService {

    private final ModelMapper modelMapper;
    private final GameBookingRepo gameBookingRepo;
    private final GameBookingMemberRepo gameBookingMemberRepo;
    private final EmployeeRepo employeeRepo;
    private final TimeSlotRepo timeSlotRepo;
    private final GameRepo gameRepo;
    private final GameTimeSlotConfigRepo gameTimeSlotConfigRepo;

    @Transactional
    public GameBookingDto createGameBooking(CreateGameBookingDto createGameBookingDto){

        GameBooking booking = modelMapper.map(createGameBookingDto, GameBooking.class);

        Employee employee = employeeRepo.findById(createGameBookingDto.getBookerId()).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        TimeSlot timeSlot = timeSlotRepo.findById(createGameBookingDto.getTimeSlotId()).orElseThrow(() -> new ResourceNotFoundException("Time slot not found"));
        Game game = gameRepo.findById(createGameBookingDto.getGameId()).orElseThrow(() -> new ResourceNotFoundException("Game is not found"));
        GameTimeSlotConfig timeSlotConfig = gameTimeSlotConfigRepo.findByGame_GameId(game.getGameId()).orElseThrow(() -> new ResourceNotFoundException("Game not found"));

        List<UUID> members = createGameBookingDto.getMemberIds();
        long memberCount = members.size();

        // If member count is 0
        if(memberCount == 0){
            throw new BadRequestException("You have to provide at least one more player other than you");
        }

        // Now check if there has any duplicate member found
        Set<UUID> uniqueMembers = new HashSet<>(members);
        if(uniqueMembers.size() != members.size()){
            throw new BadRequestException("Duplicate members are not allowed");
        }

        // Add 1 in member count as booker and check max player allowed
        if(memberCount+1 > timeSlotConfig.getMaxPlayers()){
            throw new BadRequestException("You cannot book more than its maximum player capacity");
        }

        if(members.contains(createGameBookingDto.getBookerId())){
            throw new BadRequestException("Booker must not include in member list");
        }

        // Check Time slot is exist or not
        boolean isTimeSlotExist = gameBookingRepo.existsByTimeSlot_TimeSlotId(createGameBookingDto.getTimeSlotId());
        if(isTimeSlotExist){
                throw new BadRequestException("This slot is already booked. please check other slot or join in waiting queue");
        }

        LocalDate today = LocalDate.now();
        LocalTime startTime = booking.getTimeSlot().getStartTime();
        LocalTime endTime = booking.getTimeSlot().getEndTime();

        // Check if time slot is past or not
        LocalDateTime compare = LocalDateTime.of(booking.getTimeSlot().getSlotDate(), booking.getTimeSlot().getStartTime());
        if(compare.isBefore(LocalDateTime.now())){
            throw new BadRequestException("You cannot book past slots");
        }

        // Check booker is played particular game today
        boolean isBookerPlayedGameToday = gameBookingRepo.existsByBooker_EmployeeIdAndGame_GameIdAndTimeSlot_SlotDate(createGameBookingDto.getBookerId(), createGameBookingDto.getGameId(), today);
        if(isBookerPlayedGameToday){
            throw new BadRequestException("You already played this game or you already booked one slot of this game. You can join the waiting queue.");
        }

        // Check booker is already booked any time slot during this time slot
        boolean isBookerHaveAlreadyTimeSlotInAnotherGame = gameBookingRepo.existsByBooker_EmployeeIdAndTimeSlot_StartTimeAndTimeSlot_EndTimeAndTimeSlot_SlotDate(booking.getBooker().getEmployeeId(), startTime, endTime, timeSlot.getSlotDate());
        if(isBookerHaveAlreadyTimeSlotInAnotherGame){
            throw new BadRequestException("You already have booked this slot for another game");
        }

        // Check any booking member is not played particular game today
        boolean isBookingPlayersPlayedGameToday = gameBookingMemberRepo.existsByTimeSlot_Game_GameIdAndTimeSlot_SlotDateAndEmployee_EmployeeIdIn(createGameBookingDto.getGameId(), today, members);
        if(isBookingPlayersPlayedGameToday){
            throw new BadRequestException("Some one already played this game today");
        }

        // Check booking members are already booked any time slot during this time slot
        boolean isBookingPlayerHaveAlreadyBookedTimeSlotInAnotherGame = gameBookingMemberRepo.existsByTimeSlot_StartTimeAndTimeSlot_EndTimeAndTimeSlot_SlotDateAndEmployee_EmployeeIdIn(startTime, endTime, timeSlot.getSlotDate(), members);
        if(isBookingPlayerHaveAlreadyBookedTimeSlotInAnotherGame){
            throw new BadRequestException("Some one already played this game today");
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setBooker(employee);
        booking.setTimeSlot(timeSlot);
        booking.setGame(game);
        booking.setCreatedBy(createGameBookingDto.getBookerId());
        booking.setCreatedAt(LocalDateTime.now());

        booking = gameBookingRepo.save(booking);

        for(UUID memberId : members){

            Employee emp = employeeRepo.findById(memberId).orElseThrow(() -> new
                    ResourceNotFoundException("Employee not found"));

            BookingMember bookingMember = new BookingMember();

            bookingMember.setGameBooking(booking);
            bookingMember.setEmployee(emp);
            bookingMember.setTimeSlot(timeSlot);
            bookingMember.setCreatedBy(createGameBookingDto.getBookerId());
            bookingMember.setCreatedAt(LocalDateTime.now());
            gameBookingMemberRepo.save(bookingMember);
        }
        return modelMapper.map(booking, GameBookingDto.class);
    }

    // Cancel game booking
    public String cancelBooking(UUID gameBookingId){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        GameBooking gameBooking = gameBookingRepo.findById(gameBookingId).orElseThrow(() -> new ResourceNotFoundException("Game booking not found"));
        if(!gameBooking.getBooker().getEmployeeId().equals(employeeId)){
            throw new AccessDeniedException("You have no access of it");
        }

        LocalDateTime startDateTime = LocalDateTime.of(gameBooking.getTimeSlot().getSlotDate(), gameBooking.getTimeSlot().getStartTime());

        if(!LocalDateTime.now().isAfter(startDateTime.minusMinutes(30))){
            throw new BadRequestException("You only cancel booking before 30 minutes of starting time");
        }

        if(gameBooking.getBookingStatus() == BookingStatus.CANCELLED){
            throw new BadRequestException("Booking already cancelled");
        }

        gameBooking.setBookingStatus(BookingStatus.CANCELLED);
        gameBooking.setUpdatedBy(employeeId);
        gameBooking.setUpdatedAt(LocalDateTime.now());

        gameBookingRepo.save(gameBooking);

        return "Game cancelled successfully";
    }
}
