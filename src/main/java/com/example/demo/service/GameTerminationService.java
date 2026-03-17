package com.example.demo.service;

import com.example.demo.model.GameTermination;
import com.example.demo.repository.GameTerminationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameTerminationService {

    private final GameTerminationRepository gameTerminationRepository;

    public GameTerminationService(GameTerminationRepository gameTerminationRepository) {
        this.gameTerminationRepository = gameTerminationRepository;
    }

    public List<GameTermination> getAll() {
        return gameTerminationRepository.findAll();
    }
}