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

    public synchronized GameRoom findOrCreateRoom() {

//        return rooms.values().stream()
//                .filter(GameRoom::hasFreeSlot)
//                .findFirst()
//                .orElseGet(() -> {
//                    //String id = UUID.randomUUID().toString();
//                    GameRoom room = new GameRoom(id);
//                    rooms.put(id, room);
//                    return room;
//                });


        // TODO: temporary debug solution. Only one instance of GameRoom exists on the server
        return rooms.computeIfAbsent("game-1", GameRoom::new);
    }
}
