package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "game_results")
public class GameResult {

    @Id
    private String gameId;

    @ManyToOne
    @JoinColumn(name = "termination_id", nullable = false)
    private GameTermination termination;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;

    @ManyToOne
    @JoinColumn(name = "terminated_by")
    private User terminatedBy;

    public GameResult() {}

    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }

    public GameTermination getTermination() { return termination; }
    public void setTermination(GameTermination termination) { this.termination = termination; }

    public User getWinner() { return winner; }
    public void setWinner(User winner) { this.winner = winner; }

    public User getTerminatedBy() { return terminatedBy; }
    public void setTerminatedBy(User terminatedBy) { this.terminatedBy = terminatedBy; }
}