package com.example.demo.controller;

import com.example.demo.model.GameResult;
import com.example.demo.service.GameResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game-results")
public class GameResultController {

    private final GameResultService gameResultService;


    public GameResultController(GameResultService gameResultService) {
        this.gameResultService = gameResultService;
    }

    @GetMapping
    public ResponseEntity<List<GameResult>> getAll() {
        return ResponseEntity.ok(gameResultService.getAll());
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<?> getById(@PathVariable String gameId) {
        return gameResultService.getById(gameId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GameResult gameResult) {
        try {
            GameResult saved = gameResultService.save(gameResult);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<?> delete(@PathVariable String gameId) {
        try {
            gameResultService.delete(gameId);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}