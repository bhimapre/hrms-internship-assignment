package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.BookingDetailsDto;
import com.example.hrms_backend.dto.CreateGameBookingDto;
import com.example.hrms_backend.dto.GameBookingDto;
import com.example.hrms_backend.entities.GameBooking;
import com.example.hrms_backend.services.GameBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GameBookingController {

    private final GameBookingService bookingService;

    // Create game booking
    @PreAuthorize("isAuthenticated()")
    @PostMapping("api/game-booking")
    public ResponseEntity<GameBookingDto> createGameBooking (@Valid @RequestBody CreateGameBookingDto createGameBookingDto){
        GameBookingDto bookingDto = bookingService.createGameBooking(createGameBookingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingDto);
    }

    // Cancel game booking
    @PreAuthorize("isAuthenticated()")
    @PostMapping("api/game-booking/cancel/{gameBookingId}")
    public ResponseEntity<String> cancelBooking (@PathVariable UUID gameBookingId){
        String message =  bookingService.cancelBooking(gameBookingId);
        return ResponseEntity.ok(message);
    }

    // Get all my upcoming game booking
    @PreAuthorize("isAuthenticated()")
    @GetMapping("api/game-booking/upcoming")
    public ResponseEntity<List<BookingDetailsDto>> getUpcomingGameBookings(){
        List<BookingDetailsDto> detailsDto = bookingService.getMyUpcomingBookings();
        return ResponseEntity.ok(detailsDto);
    }

    // Get Booking details by game booking id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("api/game-booking/{bookingId}")
    public ResponseEntity<BookingDetailsDto> getBookingDetailsById(@PathVariable UUID bookingId) {
        BookingDetailsDto details = bookingService.getBookingDetail(bookingId);
        return ResponseEntity.ok(details);
    }
}
