package com.example.demo.service;

import com.example.demo.model.GameResult;
import com.example.demo.repository.GameResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameResultService {

    private final GameResultRepository gameResultRepository;

    public GameResultService(GameResultRepository gameResultRepository) {
        this.gameResultRepository = gameResultRepository;
    }

    public List<GameResult> getAll() {
        return gameResultRepository.findAll();
    }

    public Optional<GameResult> getById(String gameId) {
        return gameResultRepository.findById(gameId);
    }

    public GameResult save(GameResult gameResult) {
        return gameResultRepository.save(gameResult);
    }

    public void delete(String gameId) {
        gameResultRepository.deleteById(gameId);
    }
}