package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.TravelDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.Travel;
import com.example.hrms_backend.entities.TravelEmployee;
import com.example.hrms_backend.entities.enums.TravelStatus;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.TravelEmployeeRepo;
import com.example.hrms_backend.repositories.TravelRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final ModelMapper modelMapper;
    private final TravelRepo travelRepo;
    private static final String TRAVEL_NOT_FOUND = "Travel not found";
    private final TravelEmployeeRepo travelEmployeeRepo;
    private final EmployeeRepo employeeRepo;

    // Get all travels
    public List<TravelDto> getAllTravel(){
        String role = SecurityUtils.getRole();
        UUID userId = SecurityUtils.getCurrentUserId();

        List<Travel> travels;

        if (role.equals("HR")) {
            travels = travelRepo.findAll();
        }
        else if (role.equals("EMPLOYEE")) {
            travels = travelEmployeeRepo.findAllByEmployee_Employee(userId)
                    .stream()
                    .map(TravelEmployee::getTravel)
                    .distinct()
                    .toList();
        }
        else if (role.equals("MANAGER")) {

            List<Employee> teamEmployees = employeeRepo.findByManager_EmployeeId(userId);

            Set<UUID> teamEmployeeIds = teamEmployees.stream()
                                    .map(Employee::getEmployeeId)
                                    .collect(Collectors.toSet());

            teamEmployeeIds.add(userId);

            travels = travelEmployeeRepo.findByEmployee_EmployeeIdIn(teamEmployeeIds)
                    .stream()
                    .map(TravelEmployee::getTravel)
                    .distinct()
                    .toList();
        }
        else {
            throw new ResourceNotFoundException("Invalid role");
        }

        return travels.stream()
                .map(t -> modelMapper.map(t, TravelDto.class))
                .toList();
    }

    // Get travel By ID
    public TravelDto getTravelById(UUID travelId) {
        Travel travel = travelRepo.findById(travelId).orElseThrow(() -> new ResolutionException(TRAVEL_NOT_FOUND));

        String role = SecurityUtils.getRole();
        UUID userId = SecurityUtils.getCurrentUserId();

        if (role.equals("HR")) {
            return modelMapper.map(travel, TravelDto.class);
        }

        boolean hasAccess = travelEmployeeRepo
                .existsByTravel_TravelIdAndEmployee_EmployeeId(travelId, userId);

        if (!hasAccess && role.equals("MANAGER")) {
            Set<UUID> teamIds = employeeRepo.findByManager_EmployeeId(userId)
                    .stream()
                    .map(Employee::getEmployeeId)
                    .collect(Collectors.toSet());

            hasAccess = travelEmployeeRepo.existsByTravel_TravelIdAndEmployee_EmployeeIdIn(travelId, teamIds);
        }

        return modelMapper.map(travel, TravelDto.class);
    }

    // create transaction for travel
    @Transactional
     public TravelDto createTravelEmployee(TravelDto travelDto, List<UUID>employees){
         Travel travel = modelMapper.map(travelDto, Travel.class);

         if(employees == null ||employees.isEmpty()){
             throw new BadRequestException("Add at least 1 employee");
         }
         if(travelDto.getTravelDateTo().isBefore(travelDto.getTravelDateFrom()))
         {
             throw new BadRequestException("Travel end date cannot be before start date");
         }

         Set<UUID> employee = new HashSet<>(employees);

         travel.setTravelTitle(travelDto.getTravelTitle());
         travel.setTravelDateFrom(travelDto.getTravelDateFrom());
         travel.setTravelDateTo(travelDto.getTravelDateTo());
         travel.setTravelLocation(travelDto.getTravelLocation());
         travel.setTravelDetails(travelDto.getTravelDetails());
         travel.setTravelStatus(TravelStatus.CREATED);
         travel.setCreatedBy(SecurityUtils.getCurrentUserId());
         travel.setCreatedAt(LocalDateTime.now());

         travel = travelRepo.save(travel);

         for(UUID empId: employee){

             Employee emp = employeeRepo.findById(empId).orElseThrow(() -> new
                     ResourceNotFoundException("Employee not found"));

             TravelEmployee travelEmployee = new TravelEmployee();

             travelEmployee.setTravel(travel);
             travelEmployee.setEmployee(emp);
             travelEmployee.setCreatedAt(LocalDateTime.now());
             travelEmployee.setCreatedBy(SecurityUtils.getCurrentUserId());
             travelEmployeeRepo.save(travelEmployee);
         }
         return modelMapper.map(travel, TravelDto.class);
     }

    @Transactional
    public TravelDto updateTravel(UUID travelId, TravelDto travelDto) {

        String role = SecurityUtils.getRole();

        if (!role.equals("HR")) {
            throw new BadRequestException("Only HR can update travel");
        }

        Travel travel = travelRepo.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel is not found"));

        travel.setTravelTitle(travelDto.getTravelTitle());
        travel.setTravelDetails(travelDto.getTravelDetails());
        travel.setTravelLocation(travelDto.getTravelLocation());
        travel.setTravelDateFrom(travelDto.getTravelDateFrom());
        travel.setTravelDateTo(travelDto.getTravelDateTo());
        travel.setTravelStatus(travelDto.getTravelStatus());

        return modelMapper.map(travelRepo.save(travel), TravelDto.class);
    }
}
