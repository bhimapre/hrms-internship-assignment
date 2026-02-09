package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.TravelDocumentDto;
import com.example.hrms_backend.entities.Travel;
import com.example.hrms_backend.entities.TravelDocument;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.TravelDocumentRepo;
import com.example.hrms_backend.repositories.TravelEmployeeRepo;
import com.example.hrms_backend.repositories.TravelRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import com.example.hrms_backend.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.module.ResolutionException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TravelDocumentService {

    private final TravelDocumentRepo documentRepo;
    private final TravelRepo travelRepo;
    private final ModelMapper modelMapper;
    private final TravelEmployeeRepo travelEmployeeRepo;

    // File path to save travel documents
    @Value("${file.upload}")
    private String uploadDir;

    // Upload travel documents
    public String uploadTravelDocuments(UUID travelId, MultipartFile file, TravelDocumentDto documentDto) throws IOException {

        String contentType = file.getContentType();
        String role = SecurityUtils.getRole();
        UUID userId = SecurityUtils.getCurrentUserId();
        Travel travel = travelRepo.findById(travelId).orElseThrow(() -> new ResourceNotFoundException("Travel not found"));

        if (role.equals("EMPLOYEE")) {
            boolean assigned = travelEmployeeRepo.existsByTravel_TravelIdAndEmployee_EmployeeId(travelId, userId);
            if (!assigned) {
                throw new BadRequestException("You are not assigned for this travel");
            }
        }

        if (role.equals("MANAGER")) {
            boolean assigned = travelEmployeeRepo.existsByTravel_TravelIdAndEmployee_EmployeeId(travelId, userId);
            if (!assigned) {
                throw new BadRequestException("You are not assigned for this travel");
            }
        }

        if (contentType == null || !(contentType.equals("image/png")
                || contentType.equals("image/jpeg") || contentType.equals("image/jpg")
                || contentType.equals("application/pdf") || contentType.equals("application/msword"))) {
            throw new BadRequestException("Only JPG, PNG, JPEG, PDF, MS WORD are allowed");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new BadRequestException("File size must be less than 10 MB");
        }

        TravelDocument travelDocument = modelMapper.map(documentDto, TravelDocument.class);

        String fileName = saveFileToLocal(file);

        travelDocument.setDocumentName(documentDto.getDocumentName());
        travelDocument.setOwnerType(SecurityUtils.getRole());
        travelDocument.setDocumentPath(fileName);
        travelDocument.setUploadedBy(SecurityUtils.getCurrentUserId());
        travelDocument.setTravel(travel);
        travelDocument.setCreatedAt(LocalDateTime.now());
        travelDocument.setCreatedBy(userId);

        documentRepo.save(travelDocument);
        return fileName;
    }

    // Save file in local storage
    private String saveFileToLocal(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    // Get all documents from the all travels
    public List<TravelDocumentDto> getAllTravelDocuments() {

        String role = SecurityUtils.getRole();
        UUID userId = SecurityUtils.getCurrentUserId();

        List<TravelDocument> documents;
        if (role.equals("HR")) {
            documents = documentRepo.findAll();
        } else if (role.equals("EMPLOYEE")) {
            documents = documentRepo.findByUploadedBy(userId);
        } else if (role.equals("MANAGER")) {
            documents = documentRepo.findByUploadedBy(userId);
        } else {
            throw new BadRequestException("Invalid role");
        }

        return documents.stream()
                .map(travel -> modelMapper.map(travel, TravelDocumentDto.class))
                .toList();
    }

    // Get travel documents By ID
    public TravelDocumentDto getTravelDocumentsById(UUID travelDocumentId) {

        TravelDocument document = documentRepo.findById(travelDocumentId).orElseThrow(() -> new ResolutionException("Travel not found"));
        String role = SecurityUtils.getRole();
        UUID userId = SecurityUtils.getCurrentUserId();
        if (role.equals("HR")) {
            return modelMapper.map(document, TravelDocumentDto.class);
        }
        if (role.equals("MANAGER")) {
            if (!document.getUploadedBy().equals(userId)) {
                throw new BadRequestException("You have no access of this document");
            }
        }
        if (role.equals("EMPLOYEE")) {
            if (!document.getUploadedBy().equals(userId)) {
                throw new BadRequestException("You have no access of this document");
            }
        }
        return modelMapper.map(document, TravelDocumentDto.class);
    }
}
