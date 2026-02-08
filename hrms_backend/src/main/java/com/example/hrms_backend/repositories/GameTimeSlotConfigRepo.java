package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.Game;
import com.example.hrms_backend.entities.GameTimeSlotConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GameTimeSlotConfigRepo extends JpaRepository<GameTimeSlotConfig, UUID> {

    Optional<GameTimeSlotConfig> findByGame_GameIdAndActiveTrue(UUID gameId);
    Optional<GameTimeSlotConfig> findByGame_GameId(UUID gameId);
}
