package com.example.demo.controller;

import com.example.demo.dto.JoinResponse;
import com.example.demo.dto.MoveMessage;
import com.example.demo.game.GameRoom;
import com.example.demo.game.GameState;
import com.example.demo.game.GameStateMapper;
import com.example.demo.model.Game;
import com.example.demo.model.GameResult;
import com.example.demo.model.GameTermination;
import com.example.demo.model.User;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.GameTerminationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.GameResultService;
import com.example.demo.service.GameRoomManager;
import com.example.demo.service.MatchmakingQueue;
import com.example.demo.util.MoveValidator;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller // controller handles HTTP requests
public class GameWebSocketController {
    private final GameResultService gameResultService;
    private final GameTerminationRepository gameTerminationRepository;
    private final GameRepository gameRepository;

    private final GameRoomManager manager;
    private final GameStateMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;  // Spring messaging class used to send messages to WebSocket-connected clients
    private final MatchmakingQueue matchmakingQueue;
    private final UserRepository userRepository;

    public GameWebSocketController(
            GameRoomManager manager,
            GameStateMapper mapper,
            SimpMessagingTemplate messagingTemplate,
            MatchmakingQueue matchmakingQueue,
            UserRepository userRepository,
            GameResultService gameResultService,
            GameTerminationRepository gameTerminationRepository,
            GameRepository gameRepository
    ) {
        this.manager = manager;
        this.mapper = mapper;
        this.messagingTemplate = messagingTemplate;
        this.matchmakingQueue = matchmakingQueue;
        this.userRepository = userRepository;
        this.gameResultService = gameResultService;
        this.gameTerminationRepository = gameTerminationRepository;
        this.gameRepository = gameRepository;
    }

    @MessageMapping("/move")
    public void handleMove(MoveMessage msg, Principal principal) {
        String username = principal.getName();

        GameRoom room = manager.getRoom(msg.getGameId());
        if (room == null) return;

        //boolean success = room.makeMove(msg.getPlayerId(), msg.getMove());
        boolean success = room.makeMove(username, msg.getMove());

        if (!success) return;

//        GameState state = mapper.map(room);
//        messagingTemplate.convertAndSend("/topic/game/" + msg.getGameId(), state);

        GameState state = mapper.map(room);

        if (state.isCheckmate() || state.isStalemate()) {
            saveGameResult(room, state, username);
        }
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

    private void saveGameResult(GameRoom room, GameState state, String username) {
        if (gameRepository.existsById(room.getGameId())) {
            return;
        }

        Game game = new Game();
        game.setId(room.getGameId());

        User white = userRepository.findByLogin(room.getWhitePlayerId()).orElseThrow();
        User black = userRepository.findByLogin(room.getBlackPlayerId()).orElseThrow();

        game.setWhitePlayer(white);
        game.setBlackPlayer(black);

        // String pgn = String.join(" ", room.getMoves());  // Universal Chess Interface (UCI)
        // game.setPgn(pgn);

        // SAN
        MoveValidator validator = new MoveValidator();
        MoveValidator.Chessboard board = validator.new Chessboard();

        List<String> moves = room.getMoves();

        StringBuilder pgn = new StringBuilder();

        int fullMove = 1;

        for (int i = 0; i < moves.size(); i++) {
            String uci = moves.get(i);

            MoveValidator.Move move = validator.parseMove(uci);
            if (move == null) continue;

            String san = validator.toSAN(board, move, i % 2 == 0);

            board.makeMove(move);

            if (i % 2 == 0) {
                pgn.append(fullMove).append(". ").append(san).append(" ");
            } else {
                pgn.append(san).append(" ");
                fullMove++;
            }
        }

        game.setPgn(pgn.toString().trim());
        // SAN


        game.setCreatedAt(room.getCreatedAt());
        game.setFinishedAt(LocalDateTime.now());

        GameResult result = new GameResult();
        result.setGameId(game.getId());

        User terminatedBy = userRepository.findByLogin(username).orElse(null);

        result.setTerminatedBy(terminatedBy);

        if (state.isCheckmate()) {
            System.out.println("CHECKMATE");
            GameTermination termination = gameTerminationRepository
                    .findByTermination("CHECKMATE")
                    .orElseThrow();

            result.setTermination(termination);

            User winner = state.getWinner().equals("WHITE") ? game.getWhitePlayer() : game.getBlackPlayer();

            result.setWinner(winner);

        } else if (state.isStalemate()) {
            System.out.println("STALEMATE");
            GameTermination termination = gameTerminationRepository
                    .findByTermination("STALEMATE")
                    .orElseThrow();

            result.setTermination(termination);
            result.setWinner(null);
        }

        gameRepository.save(game);
        gameResultService.save(result);

        // cleanup
        manager.removeRoom(room.getGameId());
        manager.removePlayer(room.getWhitePlayerId());
        manager.removePlayer(room.getBlackPlayerId());
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
}
