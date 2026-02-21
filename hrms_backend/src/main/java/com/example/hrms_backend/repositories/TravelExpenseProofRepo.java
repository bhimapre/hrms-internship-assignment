package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.TravelExpenseProof;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TravelExpenseProofRepo extends JpaRepository<TravelExpenseProof, UUID> {
    Optional<TravelExpenseProof> findByTravelExpense_TravelExpenseId(UUID travelExpenseTravelExpenseId);
}
