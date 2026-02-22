package com.example.hrms_backend.services;

import com.example.hrms_backend.entities.TimeSlot;
import com.example.hrms_backend.repositories.TimeSlotRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WaitingQueueSchedulerService {
    private final TimeSlotRepo timeSlotRepo;
    private final GameWaitingQueueService waitingQueueService;

    @Scheduled(cron = "0 */5 * * * *")
    public void allocateQueues() {
        try {
            LocalDateTime oneHourLater = LocalDateTime.now().plusHours(1);

            List<TimeSlot> upcomingSlots = timeSlotRepo.findSlotsStartingWithin(oneHourLater);

            for (TimeSlot slot : upcomingSlots) {
                waitingQueueService.allocate(slot);
            }
            log.info("Allocation Scheduler works. New priority allocate.");
        }
        catch (Exception e){
            log.error("some thing went wrong", e);
        }

    }
}
