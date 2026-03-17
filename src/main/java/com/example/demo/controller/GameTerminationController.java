package com.example.demo.controller;

import com.example.demo.model.GameTermination;
import com.example.demo.service.GameTerminationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game-terminations")
public class GameTerminationController {

    private final GameTerminationService gameTerminationService;

    public GameTerminationController(GameTerminationService gameTerminationService) {
        this.gameTerminationService = gameTerminationService;
    }

    @GetMapping
    public ResponseEntity<List<GameTermination>> getAll() {
        return ResponseEntity.ok(gameTerminationService.getAll());
    }
}