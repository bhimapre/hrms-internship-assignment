package com.example.hrms_backend.services;

import com.example.hrms_backend.dto.GameDto;
import com.example.hrms_backend.entities.Game;
import com.example.hrms_backend.repositories.GameRepo;
import com.example.hrms_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepo gameRepo;
    private final ModelMapper modelMapper;
    private static final String GAME_NOT_FOUND = "Game not found";

    // Get All Games
    public List<GameDto> getAllGames(){
        List<Game> games = gameRepo.findAll();
        return games.stream()
                .map(emp -> modelMapper.map(emp, GameDto.class))
                .toList();
    }

    // Get Game By ID
    public GameDto getGameById(UUID gameId) {
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new ResolutionException(GAME_NOT_FOUND));
        return modelMapper.map(game, GameDto.class);
    }

    // Add Gamme
    public GameDto createGame(GameDto gameDto){
        Game game = modelMapper.map(gameDto, Game.class);
        game.setCreatedAt(LocalDateTime.now());
        game.setCreatedBy(SecurityUtils.getCurrentUserId());

        game = gameRepo.save(game);
        return modelMapper.map(game, GameDto.class);
    }
}
