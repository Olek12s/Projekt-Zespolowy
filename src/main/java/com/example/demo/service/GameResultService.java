package com.example.demo.service;

import com.example.demo.model.Game;
import com.example.demo.model.GameResult;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.GameResultRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.EloRating;
import com.example.demo.util.MatchOutcome;
import com.example.demo.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.demo.util.MatchOutcome.resolveOutcome;

@Service
public class GameResultService {

    private final GameResultRepository gameResultRepository;
    private final GameRepository gameRepository;
    private final GameService gameService;

    public GameResultService(GameResultRepository gameResultRepository, GameRepository gameRepository, GameService gameService) {
        this.gameResultRepository = gameResultRepository;
        this.gameRepository = gameRepository;
        this.gameService = gameService;
    }

    public List<GameResult> getAll() {
        return gameResultRepository.findAll();
    }

    public Optional<GameResult> getById(String gameId) {
        return gameResultRepository.findById(gameId);
    }

//    public GameResult save(GameResult gameResult) {
//        return gameResultRepository.save(gameResult);
//    }

    @Transactional
    public GameResult save(GameResult result) {
        Game game = gameRepository.findById(result.getGameId())
                .orElseThrow();

        if (game.getFinishedAt() != null) { // double finish protection (double elo gain/loss)
            return gameResultRepository.save(result);
        }

        GameResult saved = gameResultRepository.save(result);
        gameService.finishGame(game, result);

        return saved;
    }

    public void delete(String gameId) {
        gameResultRepository.deleteById(gameId);
    }
}