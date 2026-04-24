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
public class GameWebSocketController {
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

        String playerId = principal.getName();
        String color = room.assignColor(playerId);
        GameState state = mapper.map(room);

        System.out.println("join");


        messagingTemplate.convertAndSend(
                "/topic/game/" + room.getGameId(),
                state
        );

        JoinResponse response = new JoinResponse(room.getGameId(), color, playerId);

        messagingTemplate.convertAndSendToUser(
                playerId,
                "/queue/game",
                response
        );
    }
}
