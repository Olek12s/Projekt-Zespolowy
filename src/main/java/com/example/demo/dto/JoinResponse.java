package com.example.demo.dto;

public class JoinResponse {
    private String gameId;
    private String color;

    public JoinResponse(String gameId, String color) {
        this.gameId = gameId;
        this.color = color;
    }

    public String getGameId() {return gameId;}
    public void setGameId(String gameId) {this.gameId = gameId;}
    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}
}