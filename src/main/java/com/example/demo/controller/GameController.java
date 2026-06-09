package com.example.demo.controller;

import com.example.demo.model.Game;
import com.example.demo.model.GameResult;
import com.example.demo.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.GameHistoryEntry;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import java.security.Principal;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping("/{id}/pgn")
    public ResponseEntity<String> getPgnById(@PathVariable String id) {
        return gameService.getById(id)
                .map(game -> ResponseEntity.ok(game.getPgn()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Game>> getAll() {
        return ResponseEntity.ok(gameService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return gameService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/player/{userId}/stats")
    public ResponseEntity<?> getStatsByPlayer(@PathVariable String userId) {
        try {
            Map<String, Object> stats = gameService.getStatsByPlayer(userId);
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/player/{userId}")
    public ResponseEntity<List<Game>> getGamesByUser(@PathVariable String userId) {
        return ResponseEntity.ok(gameService.getGamesByUser(userId));
    }

    @GetMapping("/recent/{count}")
    public ResponseEntity<List<Game>> getRecentGames(@PathVariable int count) {
        return ResponseEntity.ok(gameService.getLastGames(count));
    }

    @GetMapping("/player/{userId}/recent/{count}")
    public ResponseEntity<List<Game>> getRecentGamesByUser(@PathVariable String userId, @PathVariable int count) {
        return ResponseEntity.ok(gameService.getLastGamesByUser(userId, count));
    }

    @GetMapping("/player/{userId}/won")
    public ResponseEntity<List<GameResult>> getWonGames(@PathVariable String userId) {
        return ResponseEntity.ok(gameService.getWonGames(userId));
    }

    @GetMapping("/player/{userId}/lost")
    public ResponseEntity<List<GameResult>> getLostGames(@PathVariable String userId) {
        return ResponseEntity.ok(gameService.getLostGames(userId));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Game game) {
        try {
            Game saved = gameService.save(game);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            gameService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam(defaultValue = "10") int n, Principal principal) {
        User user = userService.findByLogin(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        List<GameHistoryEntry> history = gameService.getHistoryForUser(user, n)
                .stream()
                .map(g -> new GameHistoryEntry(
                        g.getId(),
                        g.getWhitePlayer().getLogin(),
                        g.getBlackPlayer().getLogin(),
                        g.getPgn(),
                        g.getCreatedAt(),
                        g.getFinishedAt()
                ))
                .toList();

        return ResponseEntity.ok(history);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> getGameStatus(@PathVariable String id) {
        if (!gameService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of(
                "gameId", id,
                "finished", gameService.isGameFinished(id)
        ));
    }

    @GetMapping("/{id}/active")
    public ResponseEntity<?> isActive(@PathVariable String id) {
        return gameService.getById(id)
                .map(game -> ResponseEntity.ok(game.getFinishedAt() == null))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/state")
    public ResponseEntity<?> getGameState(@PathVariable String id) {

        return gameService.getById(id)
                .map(game -> {
                    boolean finished = game.getFinishedAt() != null;

                    return ResponseEntity.ok(Map.of(
                            "exists", true,
                            "gameId", game.getId(),
                            "status", finished ? "FINISHED" : "IN_PROGRESS",
                            "finished", finished,
                            "active", !finished
                    ));
                })
                .orElse(ResponseEntity.ok(Map.of(
                        "exists", false,
                        "gameId", id,
                        "status", "NOT_FOUND",
                        "finished", false,
                        "active", false
                )));
    }
}