package com.example.demo.controller;

import com.example.demo.dto.JoinResponse;
import com.example.demo.dto.MoveMessage;
import com.example.demo.game.GameRoom;
import com.example.demo.game.GameState;
import com.example.demo.game.GameStateMapper;
import com.example.demo.service.GameRoomManager;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller // controller handles HTTP requests
public class GameWebSocketController
{
    private final GameRoomManager manager;
    private final GameStateMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;  // Spring messaging class used to send messages to WebSocket-connected clients


    public GameWebSocketController(GameRoomManager manager, GameStateMapper mapper, SimpMessagingTemplate messagingTemplate) {
        this.manager = manager;
        this.mapper = mapper;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/move")
    public void handleMove(MoveMessage msg) {
        GameRoom room = manager.getRoom(msg.getGameId());
        if (room == null) return;

        boolean success = room.makeMove(msg.getPlayerId(), msg.getMove());

        if (!success) return;

        GameState state = mapper.map(room);
        messagingTemplate.convertAndSend("/topic/game/" + msg.getGameId(), state);
    }

    @MessageMapping("/join")
    public void join(Principal principal) {

        GameRoom room = manager.findOrCreateRoom();


        //TODO: temp
        String color;
        if (principal == null) {
            color = room.assignColor("player-0");
            System.out.println("GameWebSocketController - join");
            System.out.println("JOIN CALLED by " + "player-0");
            System.out.println("ROOM ID: " + room.getGameId());

            GameState state = mapper.map(room);

            messagingTemplate.convertAndSend(
                    "/topic/game/" + room.getGameId(),
                    state
            );
            String playerId = (principal != null) ? principal.getName() : "player-0";
            JoinResponse response = new JoinResponse(room.getGameId(), color, playerId);

            messagingTemplate.convertAndSend(
                    "/topic/game/" + room.getGameId() + "/join",
                    response
            );
        }
        else {
            color = room.assignColor(principal.getName());
            System.out.println("GameWebSocketController - join");
            System.out.println("JOIN CALLED by " + principal.getName());
            System.out.println("ROOM ID: " + room.getGameId());

            GameState state = mapper.map(room);

            messagingTemplate.convertAndSend(
                    "/topic/game/" + room.getGameId(),
                    state
            );
            String playerId = (principal != null) ? principal.getName() : "player-0";
            JoinResponse response = new JoinResponse(room.getGameId(), color, playerId);

            messagingTemplate.convertAndSend(
                    "/topic/game/" + room.getGameId() + "/join",
                    response
            );
        }
    }
}
