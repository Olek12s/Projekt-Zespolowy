package com.example.demo.controller;

import com.example.demo.model.Game;
import com.example.demo.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
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


}