package com.example.demo.service;

import com.example.demo.game.GameRoom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoomManager
{
    // concurrentHashMap for thread-safe access and multi-threading
    private final Map<String, GameRoom> rooms = new ConcurrentHashMap<>();

    public GameRoom getRoom(String gameId) {
        return rooms.get(gameId);
    }

    public GameRoom createRoom(String gameId, String whiteId, String blackId) {
        GameRoom room = new GameRoom(gameId, whiteId, blackId);
        rooms.put(gameId, room);
        return room;
    }

    public void removeRoom(String gameId) {
        rooms.remove(gameId);
    }
}
