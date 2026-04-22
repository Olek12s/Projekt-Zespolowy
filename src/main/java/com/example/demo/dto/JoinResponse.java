package com.example.demo.dto;

public class JoinResponse {
    private String gameId;
    private String color;
    private String playerId;

    public JoinResponse(String gameId, String color, String playerId) {
        this.gameId = gameId;
        this.color = color;
        this.playerId = playerId;
    }

    public String getGameId() {return gameId;}
    public void setGameId(String gameId) {this.gameId = gameId;}
    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}