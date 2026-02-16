package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.TravelDto;
import com.example.hrms_backend.dto.TravelExpenseDto;
import com.example.hrms_backend.entities.*;
import com.example.hrms_backend.entities.enums.ExpenseStatus;
import com.example.hrms_backend.entities.enums.NotificationType;
import com.example.hrms_backend.entities.enums.TravelStatus;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.TravelEmployeeRepo;
import com.example.hrms_backend.repositories.TravelExpenseRepo;
import com.example.hrms_backend.repositories.TravelRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cglib.core.Local;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelExpenseService {

    private final TravelExpenseRepo travelExpenseRepo;
    private final EmployeeRepo employeeRepo;
    private final TravelRepo travelRepo;
    private final TravelEmployeeRepo travelEmployeeRepo;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;

    // Add Expense
    public TravelExpenseDto addExpense(TravelExpenseDto travelExpenseDto){

        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        Travel travel = travelRepo.findById(travelExpenseDto.getTravelId()).orElseThrow(() -> new ResourceNotFoundException("Travel not found"));

        if(travel.getTravelStatus().equals(TravelStatus.CANCELLED)){
            throw new BadRequestException("Travel is cancelled");
        }
        
        boolean hasAccess = travelEmployeeRepo.existsByTravel_TravelIdAndEmployee_EmployeeId(travelExpenseDto.getTravelId(), employeeId);
        if(!hasAccess){
            throw new BadRequestException("Travel or Employee not found");
        }
        TravelExpense expense = modelMapper.map(travelExpenseDto, TravelExpense.class);

        LocalDate expenseDate = expense.getExpenseDate();
        LocalDate travelStart = travel.getTravelDateFrom();
        LocalDate travelEndDate = travel.getTravelDateTo();
        LocalDate allowExpenseDate = (travelEndDate.plusDays(10));

        if(expenseDate.isBefore(travelStart) || expenseDate.isAfter(allowExpenseDate)){
            throw new BadRequestException("Expense Date is not before travel start date or You miss the end date of submit expenses");
        }

        expense.setExpenseStatus(ExpenseStatus.DRAFT);
        expense.setTravel(travel);
        expense.setEmployee(employee);
        expense.setCreatedBy(employeeId);
        expense.setCreatedAt(LocalDateTime.now());

        expense = travelExpenseRepo.save(expense);
        return modelMapper.map(expense, TravelExpenseDto.class);
    }

    // Submit expense
    public void submitExpense(UUID expenseId){

        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        TravelExpense expense = travelExpenseRepo.findById(expenseId).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if(!employeeId.equals(expense.getEmployee().getEmployeeId())){
            throw new BadRequestException("Employee not found");
        }

        if(!expense.getExpenseStatus().equals(ExpenseStatus.DRAFT)){
            throw new BadRequestException("Expense status is different");
        }

        if(expense.getTravelExpenseProofs().isEmpty()){
            throw new BadRequestException("You have to add at least 1 expense proof");
        }

        expense.setExpenseStatus(ExpenseStatus.SUBMITTED);
        expense.setUpdatedAt(LocalDateTime.now());
        expense.setUpdatedBy(employeeId);

        expense = travelExpenseRepo.save(expense);

        notificationService.sendNotification(employeeId, expense.getExpenseName(),
                "New Expense submitted by: " + employee.getName() + "for:  " + expense.getTravel().getTravelTitle(),
                NotificationType.EXPENSE_SUBMIT);
    }

    // Approve by HR
    public void approveExpense(UUID expenseId, String remark){

        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        TravelExpense expense = travelExpenseRepo.findById(expenseId).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if(!expense.getExpenseStatus().equals(ExpenseStatus.SUBMITTED)){
            throw new AccessDeniedException("Expense status is different");
        }

        String role = SecurityUtils.getRole();
        if(!role.equals("HR")){
            throw new AccessDeniedException("You have no access of it");
        }

        expense.setExpenseStatus(ExpenseStatus.APPROVED);
        expense.setHrRemark(remark);
        expense.setApprovedAt(LocalDateTime.now());
        expense.setApprovedBy(employeeId);
        expense.setUpdatedAt(LocalDateTime.now());
        expense.setUpdatedBy(employeeId);
        expense = travelExpenseRepo.save(expense);
    }

    // Reject by HR
    public void rejectExpense(UUID expenseId, String remark){

        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        TravelExpense expense = travelExpenseRepo.findById(expenseId).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if(expense.getExpenseStatus().equals(ExpenseStatus.DRAFT) || expense.getExpenseStatus().equals(ExpenseStatus.REJECTED)){
            throw new AccessDeniedException("Expense status is different");
        }

        String role = SecurityUtils.getRole();
        if(!role.equals("HR")){
            throw new AccessDeniedException("You have access of it");
        }

        expense.setExpenseStatus(ExpenseStatus.REJECTED);
        expense.setHrRemark(remark);
        expense.setUpdatedAt(LocalDateTime.now());
        expense.setUpdatedBy(employeeId);
        expense = travelExpenseRepo.save(expense);
    }

    // Get all expenses
    public List<TravelExpenseDto> getAllExpense(){

        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();
        List<TravelExpense> travelExpenses;

        String role = SecurityUtils.getRole();
        if(role.equals("HR")){
            travelExpenses = travelExpenseRepo.findAll();
        }
        else if (role.equals("MANAGER")){

            List<Employee> teamEmployees = employeeRepo.findByManager_EmployeeId(employeeId);

            Set<UUID> teamEmployeeIds = teamEmployees.stream()
                    .map(Employee::getEmployeeId)
                    .collect(Collectors.toSet());

            teamEmployeeIds.add(employeeId);

            travelExpenses = travelExpenseRepo.findByEmployee_EmployeeIdIn(teamEmployeeIds);
        }
        else if (role.equals("EMPLOYEE")) {
            travelExpenses = travelExpenseRepo.findByEmployee_EmployeeId(employeeId);
        }

        else {
            throw new AccessDeniedException("Invalid role");
        }

        return travelExpenses.stream()
                .map(t -> modelMapper.map(t, TravelExpenseDto.class))
                .toList();
    }

    // Get travel expense By ID
    public TravelExpenseDto getTravelExpenseById(UUID expenseId) {

        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        TravelExpense expense = travelExpenseRepo.findById(expenseId).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        String role = SecurityUtils.getRole();

        if (role.equals("HR")) {
            return modelMapper.map(expense, TravelExpenseDto.class);
        }
        else if (role.equals("EMPLOYEE")) {
            if(!expense.getEmployee().getEmployeeId().equals(employeeId)){
                throw new AccessDeniedException("You have no access to view this expense");
            }
        }
        else if (role.equals("MANAGER")) {
            if(expense.getEmployee().getEmployeeId().equals(employeeId)){
                return modelMapper.map(expense, TravelExpenseDto.class);
            }

            List<Employee> team = employeeRepo.findByManager_EmployeeId(employeeId);

            boolean isTeamExpense = team.stream()
                    .anyMatch(e -> e.getEmployeeId().equals(
                            expense.getEmployee().getEmployeeId()
                    ));

            if (!isTeamExpense){
                throw new AccessDeniedException("You have no access to view this expense");
            }
        }
        return modelMapper.map(expense, TravelExpenseDto.class);
    }

    // Update travel expense details
    public TravelExpenseDto updateTravelExpenseDetails(UUID expenseId, TravelExpenseDto expenseDto){
        UUID userId = SecurityUtils.getCurrentUserId();
        String role = SecurityUtils.getRole();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        TravelExpense expense = travelExpenseRepo.findById(expenseId).orElseThrow(() -> new ResourceNotFoundException("No travel expense found"));
        if(!role.equals("HR")){
            if(!expense.getCreatedBy().equals(employeeId)){
                throw new AccessDeniedException("You have no access of it");
            }
        }
        expense.setUpdatedBy(employeeId);
        expense.setUpdatedAt(LocalDateTime.now());
        travelExpenseRepo.save(expense);

        return modelMapper.map(expense, TravelExpenseDto.class);
    }

    // Cancel Expense
    public void cancelExpense(UUID expenseId){

        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        TravelExpense expense = travelExpenseRepo.findById(expenseId).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if(!expense.getEmployee().getEmployeeId().equals(employeeId)){
            throw new AccessDeniedException("You have not access to cancel expense");
        }

        if(expense.getExpenseStatus().equals(ExpenseStatus.APPROVED) || expense.getExpenseStatus().equals(ExpenseStatus.REJECTED)){
            throw new AccessDeniedException("You cannot change approved and rejected expense");
        }

        expense.setExpenseStatus(ExpenseStatus.CANCELLED);
        expense.setUpdatedAt(LocalDateTime.now());
        expense.setUpdatedBy(employeeId);

        travelExpenseRepo.save(expense);
    }
}
