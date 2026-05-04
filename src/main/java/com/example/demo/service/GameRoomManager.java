package com.example.demo.service;

import com.example.demo.game.GameRoom;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameRoomManager
{
    // concurrentHashMap for thread-safe access and multi-threading
    private final Map<String, GameRoom> rooms = new ConcurrentHashMap<>();
    private final Map<String, String> playerToRoom = new ConcurrentHashMap<>();

    public GameRoom getRoom(String gameId) {
        return rooms.get(gameId);
    }

    public GameRoom createRoom(String gameId) {
        GameRoom room = new GameRoom(gameId);
        rooms.put(gameId, room);
        return room;
    }

    public void removeRoom(String gameId) {
        rooms.remove(gameId);
    }
    public void removePlayer(String username) { playerToRoom.remove(username); }

    public synchronized GameRoom createRoomForTwo(String username1, String username2) {
        String gameId = UUID.randomUUID().toString();
        GameRoom room = createRoom(gameId);
        room.assignColor(username1);
        room.assignColor(username2);
        playerToRoom.put(username1, gameId);
        playerToRoom.put(username2, gameId);
        return room;
    }
}
