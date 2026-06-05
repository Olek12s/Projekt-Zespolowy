package com.example.demo.dto;

import java.time.LocalDateTime;

public class GameHistoryEntry {
    private String id;
    private String whitePlayer;
    private String blackPlayer;
    private String pgn;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;

    public GameHistoryEntry(String id, String whitePlayer, String blackPlayer, String pgn, LocalDateTime createdAt, LocalDateTime finishedAt) {
        this.id = id;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.pgn = pgn;
        this.createdAt = createdAt;
        this.finishedAt = finishedAt;
    }

    public String getId() { return id; }
    public String getWhitePlayer() { return whitePlayer; }
    public String getBlackPlayer() { return blackPlayer; }
    public String getPgn() { return pgn; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
}