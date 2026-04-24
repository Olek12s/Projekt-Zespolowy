package com.example.demo.game;

import com.example.demo.util.MoveValidator;

import java.util.HashSet;
import java.util.Set;

public class GameRoom
{
    private final String gameId;
    private String whitePlayerId;
    private String blackPlayerId;
    private final Set<String> observers = new HashSet<>();

    private final MoveValidator validator = new MoveValidator();
    private final MoveValidator.Chessboard board = validator.new Chessboard();

    private boolean whiteToMove = true;
    private String lastMove;

    public GameRoom(String gameId) {
        this.gameId = gameId;
        System.out.println("Game Room Initialized with ID: " + gameId);
    }

    // LEAVE IT SYNCHRONIZED TO PREVENT RACE CONDITIONS BETWEEN 2 REQUESTS AT THE SAME TIME
    public synchronized boolean makeMove(String playerId, String move) {
        boolean isWhite = playerId.equals(whitePlayerId);
        if (whiteToMove != isWhite) return false;
        if (!validator.isMoveLegalUCI(board, move, whiteToMove)) return false;

        MoveValidator.Move m = validator.parseMove(move);
        if (m == null) return false;

        board.makeMove(m);
        lastMove = move;

        whiteToMove = !whiteToMove; // swap move turn
        System.out.println("MOVE by " + playerId);
        return true;
    }

    public boolean hasFreeSlot() {
        return whitePlayerId == null || blackPlayerId == null;
    }

    public String getGameId() {return gameId;}
    public String getWhitePlayerId() {return whitePlayerId;}
    public String getBlackPlayerId() {return blackPlayerId;}
    public MoveValidator getValidator() {return validator;}
    public MoveValidator.Chessboard getBoard() {return board;}
    public boolean isWhiteToMove() {return whiteToMove;}
    public void setWhiteToMove(boolean whiteToMove) {this.whiteToMove = whiteToMove;}
    public String getLastMove() {return lastMove;}
    public void setLastMove(String lastMove) {this.lastMove = lastMove;}

    public String assignColor(String playerId) {
        if (whitePlayerId == null) {
            whitePlayerId = playerId;
            System.out.println("Assigned white");
            return "WHITE";
        }
        if (blackPlayerId == null) {
            blackPlayerId = playerId;
            System.out.println("Assigned black");
            return "BLACK";
        }
        observers.add(playerId);
        System.out.println("Assigned observer");
        return "OBSERVER";
    }
}
