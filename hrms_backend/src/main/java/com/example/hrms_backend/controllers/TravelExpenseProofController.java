package com.example.hrms_backend.controllers;

import com.example.hrms_backend.dto.TravelExpenseProofDto;
import com.example.hrms_backend.services.TravelExpenseProofService;
import com.example.hrms_backend.services.TravelExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expense-proof")
public class TravelExpenseProofController {

    private final TravelExpenseProofService travelExpenseProofService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{expenseId}")
    public ResponseEntity<String> uploadExpenseProof(@PathVariable UUID expenseId, @RequestPart("file") MultipartFile file) throws IOException {
        String fileUrl = travelExpenseProofService.uploadExpenseProof(expenseId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileUrl);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{expenseProofId}")
    public ResponseEntity<TravelExpenseProofDto> updateExpenseProofFile(@PathVariable UUID expenseProofId, @RequestPart("file") MultipartFile file) throws IOException{
        TravelExpenseProofDto expenseProofDto = travelExpenseProofService.updateExpenseProof(expenseProofId, file);
        return new ResponseEntity(expenseProofDto, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{expenseId}")
    public ResponseEntity<TravelExpenseProofDto> findExpenseProofById(@PathVariable UUID expenseId){
        TravelExpenseProofDto expenseProofDto = travelExpenseProofService.getExpenseProof(expenseId);
        return ResponseEntity.ok(expenseProofDto);
    }
}
