package com.example.demo.game;

public class GameState
{
    private String gameId;
    private String[][] board;
    private boolean whiteToMove;
    private String lastMove;

    private boolean check;
    private boolean checkmate;
    private boolean stalemate;

    private String winner;

    public GameState() {}

    public GameState(String gameId, String[][] board, boolean whiteToMove,
                     String lastMove, boolean check, boolean checkmate,
                     boolean stalemate, String winner) {
        this.gameId = gameId;
        this.board = board;
        this.whiteToMove = whiteToMove;
        this.lastMove = lastMove;
        this.check = check;
        this.checkmate = checkmate;
        this.stalemate = stalemate;
        this.winner = winner;
    }

    public String getGameId() {return gameId;}
    public void setGameId(String gameId) {this.gameId = gameId;}
    public String[][] getBoard() {return board;}
    public void setBoard(String[][] board) {this.board = board;}
    public boolean isWhiteToMove() {return whiteToMove;}
    public void setWhiteToMove(boolean whiteToMove) {this.whiteToMove = whiteToMove;}
    public String getLastMove() {return lastMove;}
    public void setLastMove(String lastMove) {this.lastMove = lastMove;}
    public boolean isCheck() {return check;}
    public void setCheck(boolean check) {this.check = check;}
    public boolean isCheckmate() {return checkmate;}
    public void setCheckmate(boolean checkmate) {this.checkmate = checkmate;}
    public boolean isStalemate() {return stalemate;}
    public void setStalemate(boolean stalemate) {this.stalemate = stalemate;}
    public String getWinner() {return winner;}
    public void setWinner(String winner) {this.winner = winner;}
}
