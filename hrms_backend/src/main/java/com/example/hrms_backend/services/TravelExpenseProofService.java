package com.example.hrms_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.hrms_backend.dto.TravelExpenseDto;
import com.example.hrms_backend.dto.TravelExpenseProofDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.TravelExpense;
import com.example.hrms_backend.entities.TravelExpenseProof;
import com.example.hrms_backend.entities.enums.ExpenseStatus;
import com.example.hrms_backend.exception.BadRequestException;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.*;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.prefs.BackingStoreException;

@Service
@RequiredArgsConstructor
public class TravelExpenseProofService {

    private final TravelExpenseRepo expenseRepo;
    private final EmployeeRepo employeeRepo;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;
    private final TravelExpenseProofRepo proofRepo;

    // Upload expense proof
    public String uploadExpenseProof(UUID expenseId, MultipartFile file) throws IOException {

        UUID userId = SecurityUtils.getCurrentUserId();
        String contentType = file.getContentType();

        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        TravelExpense expense = expenseRepo.findById(expenseId).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if(!employeeId.equals(expense.getEmployee().getEmployeeId())){
            throw new AccessDeniedException("You cannot upload proof for this expense");
        }

        if(expense.getExpenseStatus() != ExpenseStatus.DRAFT){
            throw new AccessDeniedException("Expense status is different");
        }

        if(file.isEmpty()){
            throw new BadRequestException("You have to submit at least 1 expense proof");
        }

        if (contentType == null || !(contentType.equals("image/png")
                || contentType.equals("image/jpeg") || contentType.equals("image/jpg")
                || contentType.equals("application/pdf") || contentType.equals("application/msword"))) {
            throw new BadRequestException("Only JPG, PNG, JPEG, PDF, MS WORD are allowed");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new BadRequestException("File size must be less than 10 MB");
        }

        TravelExpenseProof expenseProof = new TravelExpenseProof();
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "travel/expense-proof")
        );

        expenseProof.setExpenseFileUrl(uploadResult.get("secure_url").toString());
        expenseProof.setPublicId(uploadResult.get("public_id").toString());
        expenseProof.setUploadedBy(employeeId);
        expenseProof.setCreatedAt(LocalDateTime.now());
        expenseProof.setCreatedBy(employeeId);
        expenseProof.setTravelExpense(expense);
        expenseProof = proofRepo.save(expenseProof);

        return expenseProof.getExpenseFileUrl();
    }

    // Update travel expense proofs
    public TravelExpenseProofDto updateExpenseProof(UUID expenseProofId, MultipartFile newFile) throws IOException{

        UUID userId = SecurityUtils.getCurrentUserId();
        String role = SecurityUtils.getRole();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        UUID employeeId = employee.getEmployeeId();

        TravelExpenseProof expenseProof = proofRepo.findById(expenseProofId).orElseThrow(() -> new ResourceNotFoundException("Expense Proof not found"));

        if(!role.equals("HR")){
            if(!expenseProof.getUploadedBy().equals(employeeId)){
                throw new AccessDeniedException("You have no access of it");
            }
        }

        if (newFile == null || newFile.isEmpty()) {
            throw new BadRequestException("File is required");
        }

        String contentType = newFile.getContentType();
        if (contentType == null || !(contentType.equals("image/png")
                || contentType.equals("image/jpeg") || contentType.equals("image/jpg")
                || contentType.equals("application/pdf") || contentType.equals("application/msword"))) {
            throw new BadRequestException("Only JPG, PNG, JPEG, PDF, MS WORD are allowed");
        }

        if (newFile.getSize() > 10 * 1024 * 1024) {
            throw new BadRequestException("File size must be less than 10MB");
        }

        Map<String, Object> uploadResult =
                cloudinary.uploader().upload(
                        newFile.getBytes(),
                        ObjectUtils.asMap("folder", "travel/expense-proof"));

        cloudinary.uploader().destroy(
                expenseProof.getPublicId(),
                ObjectUtils.emptyMap()
        );

        expenseProof.setExpenseFileUrl(uploadResult.get("secure_url").toString());
        expenseProof.setPublicId(uploadResult.get("public_id").toString());

        expenseProof.setUpdatedBy(employeeId);
        expenseProof.setUpdatedAt(LocalDateTime.now());
        expenseProof.setUploadedBy(employeeId);

        expenseProof = proofRepo.save(expenseProof);
        return modelMapper.map(expenseProof, TravelExpenseProofDto.class);
    }


}

