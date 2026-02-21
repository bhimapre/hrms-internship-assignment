package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.BookingDetailsDto;
import com.example.hrms_backend.dto.BookingParticipantsDto;
import com.example.hrms_backend.dto.CreateGameBookingDto;
import com.example.hrms_backend.dto.GameBookingDto;
import com.example.hrms_backend.entities.*;
import com.example.hrms_backend.entities.enums.BookingStatus;
import com.example.hrms_backend.entities.enums.NotificationType;
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
import java.util.*;

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
    private final NotificationService notificationService;

    @Transactional
    public GameBookingDto createGameBooking(CreateGameBookingDto createGameBookingDto){

        GameBooking booking = new GameBooking();

        Employee employee = employeeRepo.findById(createGameBookingDto.getBookerId()).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        TimeSlot timeSlot = timeSlotRepo.findById(createGameBookingDto.getTimeSlotId()).orElseThrow(() -> new ResourceNotFoundException("Time slot not found"));
        Game game = timeSlot.getGame();
        GameTimeSlotConfig timeSlotConfig = gameTimeSlotConfigRepo.findByGame_GameId(game.getGameId()).orElseThrow(() -> new ResourceNotFoundException("Game not found"));

//        if(!employee.getGamePreferences().contains(game)){
//            throw new BadRequestException("You must select this game preference before booking game slot");
//        }

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
        LocalTime startTime = timeSlot.getStartTime();
        LocalTime endTime = timeSlot.getEndTime();

        // Check if time slot is past or not
        LocalDateTime compare = LocalDateTime.of(timeSlot.getSlotDate(), timeSlot.getStartTime());
        if(compare.isBefore(LocalDateTime.now())){
            throw new BadRequestException("You cannot book past slots");
        }

        // Check booker is played particular game today
        boolean isBookerPlayedGameToday = gameBookingRepo.existsByBooker_EmployeeIdAndGame_GameIdAndTimeSlot_SlotDate(employee.getEmployeeId(), game.getGameId(), today);
        if(isBookerPlayedGameToday){
            throw new BadRequestException("You already played this game or you already booked one slot of this game. You can join the waiting queue.");
        }

        // Check booker is already booked any time slot during this time slot
        boolean isBookerHaveAlreadyTimeSlotInAnotherGame = gameBookingRepo.existsByBooker_EmployeeIdAndTimeSlot_SlotDateAndTimeSlot_StartTimeLessThanAndTimeSlot_EndTimeGreaterThan(employee.getEmployeeId(), timeSlot.getSlotDate(), endTime, startTime);
        if(isBookerHaveAlreadyTimeSlotInAnotherGame){
            throw new BadRequestException("You already have booked this slot for another game");
        }

        // Check any booking member is not played particular game today
        boolean isBookingPlayersPlayedGameToday = gameBookingMemberRepo.existsByEmployee_EmployeeIdInAndTimeSlot_SlotDateAndTimeSlot_StartTimeLessThanAndTimeSlot_EndTimeGreaterThan(members, timeSlot.getSlotDate(), endTime, startTime);
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
        booking.setGame(timeSlot.getGame());
        booking.setCreatedBy(employee.getEmployeeId());
        booking.setCreatedAt(LocalDateTime.now());

        booking = gameBookingRepo.save(booking);

        notificationService.sendNotification(employee.getEmployeeId(), "Game Booking Alert",
                booking.getGame().getGameName() + " booking successful on " + timeSlot.getSlotDate() + " from " + timeSlot.getStartTime() + " to " + timeSlot.getEndTime(),
                NotificationType.GAME_BOOKING);

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

            notificationService.sendNotification(emp.getEmployeeId(), "Game Booking Alert",
                    booking.getGame().getGameName() + " booking successful on " + timeSlot.getSlotDate() + " form " + timeSlot.getStartTime() + " to " + timeSlot.getEndTime(),
                    NotificationType.GAME_BOOKING);
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

        notificationService.sendNotification(employeeId, "Cancel Booking Alert",
                "Your " + gameBooking.getGame().getGameName() + " booking on " + gameBooking.getTimeSlot().getSlotDate() + " form " + gameBooking.getTimeSlot().getStartTime() + " to " + gameBooking.getTimeSlot().getEndTime() + " is cancelled",
                NotificationType.GAME_BOOKING_CANCELLED);

        List<BookingMember> bookingMembers = gameBookingMemberRepo.findByGameBooking_GameBookingId((gameBookingId));
        for (BookingMember member: bookingMembers){
            notificationService.sendNotification(member.getEmployee().getEmployeeId(), "Cancel Booking Alert",
                    "Your " + gameBooking.getGame().getGameName() + " booking on " + gameBooking.getTimeSlot().getSlotDate() + " form " + gameBooking.getTimeSlot().getStartTime() + " to " + gameBooking.getTimeSlot().getEndTime() + " is cancelled",
                    NotificationType.GAME_BOOKING_CANCELLED);
        }

        gameBooking.setBookingStatus(BookingStatus.CANCELLED);
        gameBooking.setUpdatedBy(employeeId);
        gameBooking.setUpdatedAt(LocalDateTime.now());
        gameBookingRepo.save(gameBooking);

        return "Game cancelled successfully";
    }

    // Get upcoming booking for employee
    public List<BookingDetailsDto> getMyUpcomingBookings() {

        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<BookingDetailsDto> response = new ArrayList<>();

        // Get Booker from game booking
        List<GameBooking> bookerBookings = gameBookingRepo.findByBooker_EmployeeIdAndTimeSlot_SlotDateGreaterThanEqual(employeeId, today);

        for (GameBooking booking : bookerBookings) {
            TimeSlot slot = booking.getTimeSlot();

            // If slot date is in past then continue
            if (slot.getSlotDate().isEqual(today) && slot.getStartTime().isBefore(now)) {
                continue;
            }

            BookingDetailsDto bookingDetailsDto = modelMapper.map(booking, BookingDetailsDto.class);

            bookingDetailsDto.setGameBookingId(booking.getGameBookingId());
            bookingDetailsDto.setGameName(booking.getGame().getGameName());
            bookingDetailsDto.setSlotDate(slot.getSlotDate());
            bookingDetailsDto.setStartTime(slot.getStartTime());
            bookingDetailsDto.setEndTime(slot.getEndTime());
            response.add(bookingDetailsDto);
        }

        // Get Booking members
        List<BookingMember> memberBookings = gameBookingMemberRepo.findByEmployee_EmployeeIdAndTimeSlot_SlotDateGreaterThanEqual(employeeId, today);

        for (BookingMember member : memberBookings) {

            GameBooking booking = member.getGameBooking();
            TimeSlot slot = member.getTimeSlot();

            // If booker is same in the member table
            if (booking.getBooker().getEmployeeId().equals(employeeId)) {
                continue;
            }

            // If slot date is in past then continue
            if (slot.getSlotDate().isEqual(today) && slot.getStartTime().isBefore(now)) {
                continue;
            }

            BookingDetailsDto bookingDetailsDto = modelMapper.map(booking, BookingDetailsDto.class);

            bookingDetailsDto.setGameBookingId(booking.getGameBookingId());
            bookingDetailsDto.setGameName(booking.getGame().getGameName());
            bookingDetailsDto.setSlotDate(slot.getSlotDate());
            bookingDetailsDto.setStartTime(slot.getStartTime());
            bookingDetailsDto.setEndTime(slot.getEndTime());
            response.add(bookingDetailsDto);
        }

        return response.stream().sorted(Comparator
                        .comparing(BookingDetailsDto::getSlotDate)
                        .thenComparing(BookingDetailsDto::getStartTime))
                .toList();
    }

    // Get booking details based on id
    public BookingDetailsDto getBookingDetail(UUID bookingId) {

        GameBooking booking = gameBookingRepo.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        TimeSlot slot = booking.getTimeSlot();

        BookingDetailsDto bookingDetailsDto = modelMapper.map(booking, BookingDetailsDto.class);

        bookingDetailsDto.setGameBookingId(booking.getGameBookingId());
        bookingDetailsDto.setGameName(booking.getGame().getGameName());
        bookingDetailsDto.setSlotDate(slot.getSlotDate());
        bookingDetailsDto.setStartTime(slot.getStartTime());
        bookingDetailsDto.setEndTime(slot.getEndTime());

        List<BookingParticipantsDto> participants = new ArrayList<>();

        participants.add(
                new BookingParticipantsDto(
                        booking.getBooker().getEmployeeId(),
                        booking.getBooker().getName(),
                        "BOOKER"
                )
        );

        List<BookingMember> members =
                gameBookingMemberRepo.findByGameBooking_GameBookingId(bookingId);

        for (BookingMember member : members) {
            participants.add(new BookingParticipantsDto(
                            member.getEmployee().getEmployeeId(),
                            member.getEmployee().getName(),
                            "MEMBER")
            );
        }
        bookingDetailsDto.setParticipants(participants);
        return bookingDetailsDto;
    }
}
