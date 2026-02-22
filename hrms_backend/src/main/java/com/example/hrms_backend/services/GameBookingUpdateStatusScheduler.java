package com.example.hrms_backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameBookingUpdateStatusScheduler {

    private final GameBookingService gameBookingService;

    @Scheduled(cron = "0 */5 * * * *")
    public void processCompletedGameSlots() {

        log.info("GameBookingScheduler started");

        try {
            gameBookingService.processCompletedBookings();
            log.info("GameBookingScheduler completed successfully");
        } catch (Exception ex) {
            log.error("Something went wrong while update status and update state", ex);
        }
    }
}
