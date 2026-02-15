package com.example.hrms_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.hrms_backend.dto.EmployeeDto;
import com.example.hrms_backend.dto.EmployeeProfileUpdate;
import com.example.hrms_backend.entities.Department;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.Game;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.DepartmentRepo;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.GameRepo;
import com.example.hrms_backend.repositories.UserRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final ModelMapper modelMapper;
    private final DepartmentRepo departmentRepo;
    private final UserRepo userRepo;
    private final GameRepo gameRepo;
    private static final String EMPLOYEE_NOT_FOUND = "Employee not found";
    private final Cloudinary cloudinary;

    // Get All Employees
    public List<EmployeeDto> getAllEmployees(){
        List<Employee> employees = employeeRepo.findAll();
        return employees.stream()
                .map(emp -> modelMapper.map(emp, EmployeeDto.class))
                .toList();
    }

    // Get Employee By ID
    public EmployeeDto getEmployeeById(UUID employeeId) {
        Employee employee = employeeRepo.findById(employeeId).orElseThrow(() -> new ResolutionException(EMPLOYEE_NOT_FOUND));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    // Add Employee
    public EmployeeDto createEmployee(EmployeeDto employeeDto){
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        Department department = departmentRepo.findById(employeeDto.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        employee.setDepartment(department);
        if(employeeDto.getManagerId() != null)
        {
            Employee emp = employeeRepo.findById(employeeDto.getManagerId()).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
            employee.setManager(emp);
        }
        employee.setUser(userRepo.findById(employeeDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found")));

        employee = employeeRepo.save(employee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    // Update Employee
    public EmployeeDto updateEmployee(UUID employeeId, EmployeeDto employeeDto){
        Employee employee = employeeRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));

        if(employeeDto.getName() != null){
            employee.setName(employeeDto.getName());
        }

        if(employeeDto.getEmail() != null){
            employee.setEmail(employeeDto.getEmail());
        }

        if(employeeDto.getCity() != null){
            employee.setCity(employeeDto.getCity());
        }

        if(employeeDto.getDob() != null){
            employee.setDob(employeeDto.getDob());
        }

        if(employeeDto.getPhoneNumber() != null){
            employee.setPhoneNumber(employeeDto.getPhoneNumber());
        }

        if(employeeDto.getAddress() != null){
            employee.setAddress(employeeDto.getAddress());
        }

        if(employeeDto.getJoiningDate() != null){
            employee.setJoiningDate(employeeDto.getJoiningDate());
        }

        if(employeeDto.getDesignation() != null){
            employee.setDesignation(employeeDto.getDesignation());
        }

        if(employeeDto.getDepartmentId() != null){
            Department department = departmentRepo.findById(employeeDto.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            employee.setDepartment(department);
        }

        if(employeeDto.getManagerId() != null)
        {
           Employee emp = employeeRepo.findById(employeeDto.getManagerId()).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
           employee.setManager(emp);
        }

        Employee updateEmployee = employeeRepo.save(employee);
        return modelMapper.map(updateEmployee, EmployeeDto.class);
    }

    // Update game preference by employee
    public void updateSelfProfile(UUID employeeId, EmployeeProfileUpdate profileDto){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginEmail = auth.getName();

        Employee employee = employeeRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));

        if(!employee.getEmail().equals(loginEmail))
        {
            throw new AccessDeniedException("You cannot change other's game preference");
        }

        if(profileDto.getGamePreferences() != null){

            Set<UUID> gameIds = profileDto.getGamePreferences();
            List<Game> games = gameRepo.findAllById(gameIds);

            if(games.size() != gameIds.size())
            {
                throw new ResourceNotFoundException("Games not found");
            }

            for(Game game: games){
                if(!game.isActive()){
                    throw new BadRequestException("You cannot add this game as preference");
                }
            }

            employee.setGamePreferences(new HashSet<>(games));
        }

        employeeRepo.save(employee);
    }

    // Add & update profile picture
    public String uploadProfilePicture(MultipartFile file) throws IOException {

        String contentType = file.getContentType();
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

        if (contentType == null || !(contentType.equals("image/png")
                || contentType.equals("image/jpeg") || contentType.equals("image/jpg"))) {
            throw new BadRequestException("Only JPG, PNG, JPEG are allowed");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new BadRequestException("File size must be less than 10 MB");
        }

        if(employee.getPublicId() != null && !employee.getPublicId().isBlank()){
            cloudinary.uploader().destroy(
                    employee.getPublicId(),
                    ObjectUtils.emptyMap()
            );
        }

        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "employee/profilePictures")
        );

        employee.setProfilePictureFileUrl(uploadResult.get("secure_url").toString());
        employee.setPublicId(uploadResult.get("public_id").toString());
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setUpdatedBy(employeeId);
        employee = employeeRepo.save(employee);

        return employee.getProfilePictureFileUrl();
    }
}
