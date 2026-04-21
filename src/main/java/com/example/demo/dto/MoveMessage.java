package com.example.demo.dto;

// DTO used by clients to send a move to the server (in SAN format)
public class MoveMessage
{
    private String gameId;
    private String move;    // Standard Algebraic Notation (SAN) format

    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }

    public String getMove() { return move; }
    public void setMove(String move) { this.move = move; }
}
