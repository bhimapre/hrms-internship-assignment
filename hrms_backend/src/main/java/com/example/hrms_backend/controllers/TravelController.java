package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.AssignTravelDto;
import com.example.hrms_backend.dto.CreateTravelRequestDto;
import com.example.hrms_backend.dto.EmployeeDto;
import com.example.hrms_backend.dto.TravelDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.services.TravelService;
import com.example.hrms_backend.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;

    // Travel and assigned employee
    @PreAuthorize("hasAuthority('HR')")
    @PostMapping("/api/hr/travels")
    public ResponseEntity<TravelDto> createTravel(@Valid @RequestBody CreateTravelRequestDto travelDto){
        TravelDto travel = travelService.createTravelEmployee(travelDto);
        return new ResponseEntity<>(travel, HttpStatus.CREATED);
    }

    // Get all travels
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/travels")
    public ResponseEntity<Page<TravelDto>> getAllTravels(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<TravelDto> travel = travelService.getAllTravel(pageable);
        return new ResponseEntity<>(travel, HttpStatus.OK);
    }

    // Get travel by id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/travels/{travelId}")
    public ResponseEntity<TravelDto> getTravelById(@PathVariable UUID travelId){
        return ResponseEntity.ok(travelService.getTravelById(travelId));
    }

    // Update travel details
    @PreAuthorize("hasAuthority('HR')")
    @PutMapping("/api/travels/{travelId}")
    public ResponseEntity<TravelDto> updateTravel(@PathVariable UUID travelId, @Valid @RequestBody TravelDto travelDto){
        return ResponseEntity.ok(travelService.updateTravel(travelId, travelDto));
    }

    // Cancel travel
    @PreAuthorize("hasAuthority('HR')")
    @PutMapping("api/travel/cancel/{travelId}")
    public ResponseEntity<Void> cancelTravel(@PathVariable UUID travelId){
        travelService.cancelTravel(travelId);
        return ResponseEntity.noContent().build();
    }

    //Assign Employee
    @PreAuthorize("isAuthenticated()")
    @GetMapping("api/travel/employees/{travelId}")
    public ResponseEntity<List<EmployeeDto>> getAssignEmployees(@PathVariable UUID travelId){
        return ResponseEntity.ok(travelService.assignEmployee(travelId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("api/travel/assigned")
    public ResponseEntity<List<AssignTravelDto>> getAllAssign(){
        return ResponseEntity.ok(travelService.getAllAssignEmployee());
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/api/travel/manager/assigned")
    public ResponseEntity<List<AssignTravelDto>> getManagerAssignedTravels() {
        return ResponseEntity.ok(travelService.getManagerAssignedTravels());
    }
}
