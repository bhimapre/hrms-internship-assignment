package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.AssignTravelDto;
import com.example.hrms_backend.dto.CreateTravelRequestDto;
import com.example.hrms_backend.dto.EmployeeDto;
import com.example.hrms_backend.dto.TravelDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.Travel;
import com.example.hrms_backend.entities.TravelEmployee;
import com.example.hrms_backend.entities.enums.NotificationType;
import com.example.hrms_backend.entities.enums.TravelStatus;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.TravelEmployeeRepo;
import com.example.hrms_backend.repositories.TravelRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.time.LocalDate;
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
    private final NotificationService notificationService;
    private final EmailService emailService;
    private static final String EMPLOYEE_NOT_FOUND = "Employee not found";

    // Get all travels
    public Page<TravelDto> getAllTravel(Pageable pageable){
        String role = SecurityUtils.getRole();
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

        Page<Travel> travels;

        if (role.equals("HR")) {
            travels = travelRepo.findAll(pageable);
        }
        else if (role.equals("EMPLOYEE")) {
            travels = travelEmployeeRepo.findByEmployee_EmployeeId(employeeId, pageable)
                    .map(t ->  modelMapper.map(t, Travel.class));
        }
        else if (role.equals("MANAGER")) {

            List<Employee> teamEmployees = employeeRepo.findByManager_EmployeeId(employeeId);

            Set<UUID> teamEmployeeIds = teamEmployees.stream()
                                    .map(Employee::getEmployeeId)
                                    .collect(Collectors.toSet());

            teamEmployeeIds.add(employeeId);

            travels = travelEmployeeRepo.findByEmployee_EmployeeIdIn(teamEmployeeIds, pageable)
                    .map(t -> modelMapper.map(t, Travel.class));
        }
        else {
            throw new ResourceNotFoundException("Invalid role");
        }

        return travels
                .map(t -> modelMapper.map(t, TravelDto.class));
    }

    // Get travel By ID
    public TravelDto getTravelById(UUID travelId) {
        Travel travel = travelRepo.findById(travelId).orElseThrow(() -> new ResolutionException(TRAVEL_NOT_FOUND));

        String role = SecurityUtils.getRole();
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));

        UUID employeeId = employee.getEmployeeId();

        if (role.equals("HR")) {
            return modelMapper.map(travel, TravelDto.class);
        }

        boolean hasAccess = travelEmployeeRepo
                .existsByTravel_TravelIdAndEmployee_EmployeeId(travelId, employeeId);

        if (!hasAccess && role.equals("MANAGER")) {
            Set<UUID> teamIds = employeeRepo.findByManager_EmployeeId(employeeId)
                    .stream()
                    .map(Employee::getEmployeeId)
                    .collect(Collectors.toSet());

            hasAccess = travelEmployeeRepo.existsByTravel_TravelIdAndEmployee_EmployeeIdIn(travelId, teamIds);
        }
        if(!hasAccess && role.equals("EMPLOYEE")){
            throw new AccessDeniedException("You have no access of it");
        }

        return modelMapper.map(travel, TravelDto.class);
    }


    // create transaction for travel
    @Transactional
     public TravelDto createTravelEmployee(CreateTravelRequestDto travelDto){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

         Travel travel = modelMapper.map(travelDto, Travel.class);

         if(travelDto.getEmployeeIds() == null || travelDto.getEmployeeIds().isEmpty()){
             throw new BadRequestException("Add at least 1 employee");
         }

         if(travelDto.getTravelDateTo().isBefore(travelDto.getTravelDateFrom()))
         {
             throw new BadRequestException("Travel end date cannot be before start date");
         }

         if(travelDto.getTravelDateFrom().isAfter(LocalDate.now()) && travelDto.getTravelDateTo().equals(LocalDate.now())){
             throw new BadRequestException("Travel start and end date must be in future");
         }

         travel.setTravelStatus(TravelStatus.CREATED);
         travel.setCreatedBy(employeeId);
         travel.setCreatedAt(LocalDateTime.now());

         travel = travelRepo.save(travel);

         for(UUID empId: travelDto.getEmployeeIds()){

             Employee emp = employeeRepo.findById(empId).orElseThrow(() -> new
                     ResourceNotFoundException(EMPLOYEE_NOT_FOUND));

             TravelEmployee travelEmployee = new TravelEmployee();

             travelEmployee.setTravel(travel);
             travelEmployee.setEmployee(emp);
             travelEmployee.setCreatedAt(LocalDateTime.now());
             travelEmployee.setCreatedBy(employeeId);
             travelEmployee = travelEmployeeRepo.save(travelEmployee);

             try{
                 emailService.sendTravelAssignEmployee(emp.getEmail(), travel.getTravelTitle(), travel.getTravelLocation(), travel.getTravelDateFrom(), travel.getTravelDateTo());
             }
             catch (Exception e){
                throw new RuntimeException("Failed to send email");
             }
             notificationService.sendNotification(emp.getEmployeeId(), travel.getTravelTitle(),
                     "You have assign new travel. Please check more details on travel section.",
                     NotificationType.TRAVEL_PLAN);
         }
         return modelMapper.map(travel, TravelDto.class);
     }

     // Update Travel
    @Transactional
    public TravelDto updateTravel(UUID travelId, TravelDto travelDto) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

        Travel travel = travelRepo.findById(travelId).orElseThrow(() -> new ResourceNotFoundException(TRAVEL_NOT_FOUND));
        String role = SecurityUtils.getRole();

        if (!role.equals("HR")) {
            throw new BadRequestException("Only HR can update travel");
        }

        if(travel.getTravelStatus().equals(TravelStatus.COMPLETED) || travel.getTravelStatus().equals(TravelStatus.CANCELLED)){
            throw new BadRequestException("Travel completed or Cancelled so you do not change it");
        }

        if(travelDto.getTravelDateTo().isBefore(travelDto.getTravelDateFrom()))
        {
            throw new BadRequestException("Travel end date cannot be before start date");
        }

        if(travelDto.getTravelDateFrom().isAfter(LocalDate.now()) && travelDto.getTravelDateTo().equals(LocalDate.now())){
            throw new BadRequestException("Travel start and end date must be in future");
        }

        travel.setTravelTitle(travelDto.getTravelTitle());
        travel.setTravelDetails(travelDto.getTravelDetails());
        travel.setTravelLocation(travelDto.getTravelLocation());
        travel.setTravelDateFrom(travelDto.getTravelDateFrom());
        travel.setTravelDateTo(travelDto.getTravelDateTo());
        travel.setTravelStatus(travelDto.getTravelStatus());
        travel.setUpdatedAt(LocalDateTime.now());
        travel.setUpdatedBy(employeeId);

        travel = travelRepo.save(travel);

        return modelMapper.map(travel, TravelDto.class);
    }

    // Soft delete or canceled travel
    public void cancelTravel(UUID travelId){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();
        String role = SecurityUtils.getRole();

        if (!role.equals("HR")) {
            throw new AccessDeniedException("You have no access of it");
        }

        Travel travel = travelRepo.findById(travelId).orElseThrow(() -> new ResourceNotFoundException(TRAVEL_NOT_FOUND));

        if(!travel.getTravelStatus().equals(TravelStatus.CREATED)){
            throw new BadRequestException("Travel is already complete or canceled");
        }

        if(travel.getTravelDateFrom().isBefore(LocalDate.now())){
            throw new BadRequestException("Travel is already ongoing");
        }

        travel.setTravelStatus(TravelStatus.CANCELLED);
        travel.setUpdatedBy(employeeId);
        travel.setUpdatedAt(LocalDateTime.now());
        travel = travelRepo.save(travel);
    }

    // Assign Travel Employees
    public List<EmployeeDto> assignEmployee(UUID travelId){
        Travel travel = travelRepo.findById(travelId).orElseThrow(() -> new ResourceNotFoundException(TRAVEL_NOT_FOUND));

        List<Employee> employees = travelEmployeeRepo.findByTravel_TravelId(travelId)
                .stream().map(TravelEmployee::getEmployee)
                .toList();

        return employees.stream()
                .map(e -> modelMapper.map(e, EmployeeDto.class))
                .toList();
    }

    // Assign Travel
    public List<AssignTravelDto> getAllAssignEmployee(){
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

        return travelEmployeeRepo.findByEmployee_EmployeeId(employeeId)
                .stream()
                .map(te -> {
                    Travel t = te.getTravel();
                    return new AssignTravelDto(
                            t.getTravelId(),
                            t.getTravelTitle(),
                            t.getTravelDateFrom(),
                            t.getTravelDateTo(),
                            t.getTravelLocation(),
                            t.getTravelDetails(),
                            t.getTravelStatus()
                    );
                })
                .toList();
    }


    public List<AssignTravelDto> getManagerAssignedTravels() {

        UUID userId = SecurityUtils.getCurrentUserId();

        Employee manager = employeeRepo
                .findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("EMPLOYEE_NOT_FOUND"));

        UUID managerId = manager.getEmployeeId();

        return travelEmployeeRepo
                .findByEmployee_Manager_EmployeeId(managerId)
                .stream()
                .map(TravelEmployee::getTravel)
                .distinct()
                .map(travel -> new AssignTravelDto(
                        travel.getTravelId(),
                        travel.getTravelTitle(),
                        travel.getTravelDateFrom(),
                        travel.getTravelDateTo(),
                        travel.getTravelLocation(),
                        travel.getTravelDetails(),
                        travel.getTravelStatus()
                ))
                .toList();
    }
}

