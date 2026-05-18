package com.example.demo.dto;

public class ChatMessage {
    private String gameId;
    private String sender;
    private String content;

    public ChatMessage() {}

    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}