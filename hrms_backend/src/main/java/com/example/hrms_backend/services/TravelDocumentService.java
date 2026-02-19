package com.example.hrms_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.hrms_backend.dto.TravelDocumentDto;
import com.example.hrms_backend.entities.Employee;
import com.example.hrms_backend.entities.Travel;
import com.example.hrms_backend.entities.TravelDocument;
import com.example.hrms_backend.exception.ResourceNotFoundException;
import com.example.hrms_backend.repositories.EmployeeRepo;
import com.example.hrms_backend.repositories.TravelDocumentRepo;
import com.example.hrms_backend.repositories.TravelEmployeeRepo;
import com.example.hrms_backend.repositories.TravelRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import com.example.hrms_backend.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.lang.module.ResolutionException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TravelDocumentService {

    private final TravelDocumentRepo documentRepo;
    private final TravelRepo travelRepo;
    private final ModelMapper modelMapper;
    private final EmployeeRepo employeeRepo;
    private final TravelEmployeeRepo travelEmployeeRepo;
    private final Cloudinary cloudinary;
    private static final String EMPLOYEE_NOT_FOUND = "Employee not found";
    private static final String TRAVEL_DOCUMENT_NOT_FOUND = "Travel document not found";

    // Upload travel documents
    public String uploadTravelDocuments(UUID travelId, MultipartFile file, TravelDocumentDto documentDto) throws IOException {

        String contentType = file.getContentType();
        String role = SecurityUtils.getRole();
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

        Travel travel = travelRepo.findById(travelId).orElseThrow(() -> new ResourceNotFoundException("Travel not found"));

        if (role.equals("EMPLOYEE") || role.equals("MANAGER")) {
            boolean assigned = travelEmployeeRepo.existsByTravel_TravelIdAndEmployee_EmployeeId(travelId, employeeId);
            if (!assigned) {
                throw new AccessDeniedException("You are not assigned for this travel");
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

        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "travel/documents")
        );

        travelDocument.setTravelDocumentFileUrl(uploadResult.get("secure_url").toString());
        travelDocument.setPublicId(uploadResult.get("public_id").toString());
        travelDocument.setOwnerType(SecurityUtils.getRole());
        travelDocument.setUploadedBy(employeeId);
        travelDocument.setTravel(travel);
        travelDocument.setEmployee(employee);
        travelDocument.setCreatedAt(LocalDateTime.now());
        travelDocument.setCreatedBy(employeeId);

        travelDocument = documentRepo.save(travelDocument);
        return travelDocument.getTravelDocumentFileUrl();
    }

    // Get all documents from the all travels
    public Page<TravelDocumentDto> getAllTravelDocuments(Pageable pageable) {

        String role = SecurityUtils.getRole();
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

        Page<TravelDocument> documents;
        if (role.equals("HR")) {
            documents = documentRepo.findAll(pageable);
        }
        else if (role.equals("EMPLOYEE") || role.equals("MANAGER")) {
            List<Employee> team = employeeRepo.findByManager_EmployeeId(employeeId);
            team.add(employee);

            List<UUID> employeeIds = team.stream()
                    .map(Employee::getEmployeeId)
                    .toList();

            List<UUID> travelIds = travelEmployeeRepo.findByEmployee_EmployeeIdIn(employeeIds, pageable)
                    .stream().map(e -> e.getTravel().getTravelId())
                    .distinct().toList();

            documents = documentRepo.findByTravel_TravelIdIn(travelIds, pageable);
        }
        else {
            throw new BadRequestException("Invalid role");
        }

        return documents.map(travel -> modelMapper.map(travel, TravelDocumentDto.class));
    }

    // Get travel documents By ID
    public TravelDocumentDto getTravelDocumentsById(UUID travelDocumentId) {

        TravelDocument document = documentRepo.findById(travelDocumentId).orElseThrow(() -> new ResolutionException(TRAVEL_DOCUMENT_NOT_FOUND));
        String role = SecurityUtils.getRole();
        UUID userId = SecurityUtils.getCurrentUserId();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();
        UUID travelId = document.getTravel().getTravelId();

        if (role.equals("HR")) {
            return modelMapper.map(document, TravelDocumentDto.class);
        }

        boolean assigned = travelEmployeeRepo.existsByTravel_TravelIdAndEmployee_EmployeeId(travelId, employeeId);

        if(!assigned){

            if (role.equals("MANAGER")) {

                List<Employee> team = employeeRepo.findByManager_EmployeeId(employeeId);

                boolean teamAssigned = team.stream()
                        .anyMatch(e -> travelEmployeeRepo
                                .existsByTravel_TravelIdAndEmployee_EmployeeId(travelId, e.getEmployeeId()));

                if (!teamAssigned) {
                    throw new AccessDeniedException("You have no access of this document");
                }
            }
            else throw new AccessDeniedException("You have no access of it");
        }
        return modelMapper.map(document, TravelDocumentDto.class);
    }

    // Get Travel Document Uploaded by HR
    public List<TravelDocumentDto> getDocumentsByTravelIdAndHR(UUID travelId){
        List<TravelDocument> documents = documentRepo.findByTravel_TravelIdAndOwnerType(travelId, "HR");
        return documents.stream()
                .map(d -> modelMapper.map(d, TravelDocumentDto.class))
                .toList();
    }

    // Get Travel Document By Travel ID
    public Page<TravelDocumentDto> getDocumentsByTravelId(Pageable pageable, UUID travelId){
        Page<TravelDocument> travelDocuments = documentRepo.findByTravel_TravelId(travelId, pageable);
        return travelDocuments.map(td -> modelMapper.map(td, TravelDocumentDto.class));
    }

    // Get Travel Document By Employee ID
    public List<TravelDocumentDto> getDocumentsByTravelIdAndExpenseId(UUID travelId, UUID employeeId){
        List<TravelDocument> documents = documentRepo.findByTravel_TravelIdAndEmployee_EmployeeId(travelId,employeeId);
        return documents.stream()
                .map(d -> modelMapper.map(d, TravelDocumentDto.class))
                .toList();
    }

    // Update travel document details
    public TravelDocumentDto updateTravelDocumentDetails(UUID documentId, TravelDocumentDto documentDto){
        UUID userId = SecurityUtils.getCurrentUserId();
        String role = SecurityUtils.getRole();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

        TravelDocument document = documentRepo.findById(documentId).orElseThrow(() ->
                        new ResourceNotFoundException(TRAVEL_DOCUMENT_NOT_FOUND));

        if (!role.equals("HR")) {
            if (!document.getUploadedBy().equals(employeeId)) {
                throw new AccessDeniedException("Only document uploader can update this travel document");
            }
        }

        document.setDocumentName(documentDto.getDocumentName());

        document.setUpdatedAt(LocalDateTime.now());
        document.setUpdatedBy(employeeId);

        documentRepo.save(document);

        return modelMapper.map(document, TravelDocumentDto.class);
    }

    // Updated travel document file
    public TravelDocumentDto updateTravelDocumentFile(UUID documentId, MultipartFile newFile) throws IOException{

        UUID userId = SecurityUtils.getCurrentUserId();
        String role = SecurityUtils.getRole();
        Employee employee = employeeRepo.findByUser_UserId(userId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
        UUID employeeId = employee.getEmployeeId();

        TravelDocument document = documentRepo.findById(documentId).orElseThrow(() ->
                        new ResourceNotFoundException(TRAVEL_DOCUMENT_NOT_FOUND));

        if (!"HR".equals(role)) {
            if(!document.getUploadedBy().equals(employeeId)) {
                throw new AccessDeniedException("Only uploader or HR can replace document");
            }
        }

        if (newFile == null || newFile.isEmpty()) {
            throw new BadRequestException("File is required");
        }

        String contentType = newFile.getContentType();
        if (contentType == null ||
                !(contentType.startsWith("image/")
                        || contentType.equals("application/pdf"))) {
            throw new BadRequestException("Invalid file type");
        }

        if (newFile.getSize() > 10 * 1024 * 1024) {
            throw new BadRequestException("File size must be less than 10MB");
        }

        cloudinary.uploader().destroy(
                document.getPublicId(),
                ObjectUtils.emptyMap()
        );

        Map<String, Object> uploadResult =
                cloudinary.uploader().upload(
                        newFile.getBytes(),
                        ObjectUtils.asMap("folder", "travel/documents")
                );

        document.setTravelDocumentFileUrl(uploadResult.get("secure_url").toString());
        document.setPublicId(uploadResult.get("public_id").toString());

        document.setUpdatedAt(LocalDateTime.now());
        document.setUpdatedBy(employeeId);

        documentRepo.save(document);

        return modelMapper.map(document, TravelDocumentDto.class);
    }
}
