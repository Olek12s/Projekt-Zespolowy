package com.example.demo.controller;

import com.example.demo.dto.ChatMessage;
import com.example.demo.dto.JoinResponse;
import com.example.demo.dto.MoveMessage;
import com.example.demo.game.GameRoom;
import com.example.demo.game.GameState;
import com.example.demo.game.GameStateMapper;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.GameResultService;
import com.example.demo.service.GameRoomManager;
import com.example.demo.service.MatchmakingQueue;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Optional;

@Controller // controller handles HTTP requests
public class GameWebSocketController {
    private final GameRoomManager manager;
    private final GameStateMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;  // Spring messaging class used to send messages to WebSocket-connected clients
    private final MatchmakingQueue matchmakingQueue;
    private final UserRepository userRepository;


    public GameWebSocketController(GameRoomManager manager, GameStateMapper mapper, SimpMessagingTemplate messagingTemplate, MatchmakingQueue matchmakingQueue, UserRepository userRepository, GameResultService gameResultService) {
        this.manager = manager;
        this.mapper = mapper;
        this.messagingTemplate = messagingTemplate;
        this.matchmakingQueue = matchmakingQueue;
        this.userRepository = userRepository;
    }

    @MessageMapping("/move")
    public void handleMove(MoveMessage msg, Principal principal) {
        String username = principal.getName();

        GameRoom room = manager.getRoom(msg.getGameId());
        if (room == null) return;

        //boolean success = room.makeMove(msg.getPlayerId(), msg.getMove());
        boolean success = room.makeMove(username, msg.getMove());

        if (!success) return;

        GameState state = mapper.map(room);
        messagingTemplate.convertAndSend("/topic/game/" + msg.getGameId(), state);
    }

    @MessageMapping("/join")
    public void join(Principal principal) {
        String username = principal.getName();

        if (matchmakingQueue.isInQueue(username)) return;

        User user = userRepository.findByLogin(username).orElse(null);
        if (user == null) return;

        double elo = user.getElo();
        Optional<MatchmakingQueue.QueueEntry> opponent = matchmakingQueue.findMatch(elo);

        if (opponent.isPresent()) {
            matchmakingQueue.remove(opponent.get().username());
            GameRoom room = manager.createRoomForTwo(username, opponent.get().username());
            GameState state = mapper.map(room);
            String gameId = room.getGameId();

            messagingTemplate.convertAndSendToUser(
                    username, "/queue/game",
                    new JoinResponse(gameId, room.getWhitePlayerId().equals(username) ? "WHITE" : "BLACK", username)
            );
            messagingTemplate.convertAndSendToUser(
                    opponent.get().username(), "/queue/game",
                    new JoinResponse(gameId, room.getWhitePlayerId().equals(opponent.get().username()) ? "WHITE" : "BLACK", opponent.get().username())
            );
            messagingTemplate.convertAndSend("/topic/game/" + gameId, state);

        } else {
            matchmakingQueue.add(new MatchmakingQueue.QueueEntry(username, elo, System.currentTimeMillis()));
            messagingTemplate.convertAndSendToUser(
                    username, "/queue/game",
                    new JoinResponse(null, "WAITING", username)
            );
        }
    }

    @MessageMapping("/state")
    public void getState(Principal principal) {
        String username = principal.getName();

        String gameId = manager.getRoomIdForPlayer(username);
        if (gameId == null) return;

        GameRoom room = manager.getRoom(gameId);
        if (room == null) return;

        GameState state = mapper.map(room);

        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/state",
                state
        );
    }

    @MessageMapping("/chat")
    public void handleChat(ChatMessage msg, Principal principal) {
        msg.setSender(principal.getName());
        messagingTemplate.convertAndSend("/topic/game/" + msg.getGameId() + "/chat", msg);
    }
}
