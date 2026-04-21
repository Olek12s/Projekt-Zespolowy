package com.example.demo.game;

import com.example.demo.util.MoveValidator;

public class GameRoom
{
    private final String gameId;
    private final String whitePlayerId;
    private final String blackPlayerId;

    private final MoveValidator validator = new MoveValidator();
    private final MoveValidator.Chessboard board;

    private boolean whiteToMove = true;
    private String lastMove;

    public GameRoom(String gameId, String whitePlayerId, String blackPlayerId) {
        this.gameId = gameId;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.board = validator.new Chessboard();
    }

    // LEAVE IT SYNCHRONIZED TO PREVENT RACE CONDITIONS BETWEEN 2 REQUESTS AT THE SAME TIME
    public synchronized boolean makeMove(String playerId, String move) {
        boolean isWhite = playerId.equals(whitePlayerId);
        if (whiteToMove != isWhite) return false;
        if (!validator.isMoveLegal(board, move, whiteToMove)) return false;

        MoveValidator.Move m = validator.parseMove(move);
        if (m == null) return false;

        board.makeMove(m);
        lastMove = move;

        whiteToMove = !whiteToMove; // swap move turn

        return true;
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
}
