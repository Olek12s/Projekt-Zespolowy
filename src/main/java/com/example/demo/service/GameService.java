package com.example.demo.service;

import com.example.demo.model.Game;
import com.example.demo.model.GameResult;
import com.example.demo.model.User;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.GameResultRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.EloRating;
import com.example.demo.util.MatchOutcome;
import com.example.demo.util.Pair;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.demo.util.MatchOutcome.resolveOutcome;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameResultRepository gameResultRepository;
    private final UserRepository userRepository;

    public GameService(GameRepository gameRepository, GameResultRepository gameResultRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.gameResultRepository = gameResultRepository;
        this.userRepository = userRepository;
    }

    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    public Optional<Game> getById(String id) {
        return gameRepository.findById(id);
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public void delete(String id) {
        gameRepository.deleteById(id);
    }

    public Map<String, Object> getStatsByPlayer(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Game> allGames = gameRepository.findByWhitePlayerOrBlackPlayer(user, user);
        List<GameResult> wins = gameResultRepository.findByWinner(user);

        int total = allGames.size();
        int won = wins.size();
        int lost = (int) allGames.stream().filter(g -> {
                    Optional<GameResult> result = gameResultRepository.findById(g.getId());
                    return result.isPresent() && result.get().getWinner() !=null && !result.get().getWinner().getId().equals(userId);
                }).count();
        int drawn = total - won - lost;

        Map<String, Object> stats = new HashMap<>();
        stats.put("userId", userId);
        stats.put("total", total);
        stats.put("won", won);
        stats.put("lost", lost);
        stats.put("drawn", drawn);

        return stats;
    }

    @Transactional
    public void finishGame(Game game, GameResult result) {

        MatchOutcome outcome = resolveOutcome(result, game);

        game.setFinishedAt(LocalDateTime.now());

        Pair<Double, Double> newRatings =
                EloRating.getOutcome(
                        game.getWhitePlayer().getElo(),
                        game.getBlackPlayer().getElo(),
                        outcome
                );

        game.getWhitePlayer().setElo(newRatings.first());
        game.getBlackPlayer().setElo(newRatings.second());

        userRepository.save(game.getWhitePlayer());
        userRepository.save(game.getBlackPlayer());
        gameRepository.save(game);
    }

    // TODO: finish
    public void terminateGame(String gameId) {}
}