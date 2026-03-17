package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "games")
public class Game {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "white_player_id", nullable = false)
    private User whitePlayer;

    @ManyToOne
    @JoinColumn(name = "black_player_id", nullable = false)
    private User blackPlayer;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String pgn;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    public Game() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getWhitePlayer() { return whitePlayer; }
    public void setWhitePlayer(User whitePlayer) { this.whitePlayer = whitePlayer; }

    public User getBlackPlayer() { return blackPlayer; }
    public void setBlackPlayer(User blackPlayer) { this.blackPlayer = blackPlayer; }

    public String getPgn() { return pgn; }
    public void setPgn(String pgn) { this.pgn = pgn; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }
}