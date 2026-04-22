package com.example.demo.game;

import com.example.demo.util.MoveValidator;
import org.springframework.stereotype.Component;

@Component
public class GameStateMapper
{
    // Maps GameRoom to GameState DTO for client (WebSocket response)
    // In other words - Builds a GameState DTO from GameRoom obj,
    // including board representation and game status (check, mate, stalemate)
    // for client's frontend
    public GameState map(GameRoom room) {
        MoveValidator.Chessboard board = room.getBoard();
        String[][] array = new String[8][8];

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                MoveValidator.Piece p = board.getPiece(col, row);

                if (p == null) {
                    array[row][col] = null;
                } else {
                    String color = p.color() == MoveValidator.PieceColor.WHITE ? "w" : "b";
                    String type = switch (p.type()) {
                        case KING -> "K";
                        case QUEEN -> "Q";
                        case ROOK -> "R";
                        case BISHOP -> "B";
                        case KNIGHT -> "N";
                        case PAWN -> "P";
                    };
                    array[row][col] = color + type;
                }
            }
        }
        MoveValidator validator = new MoveValidator();

        boolean check = validator.isCheck(board, room.isWhiteToMove());
        boolean checkmate = validator.isCheckMate(board, room.isWhiteToMove());
        boolean stalemate = validator.isStaleMate(board, room.isWhiteToMove());

        String winner = null;
        if (checkmate) {
            winner = room.isWhiteToMove() ? "BLACK" : "WHITE";
        }

        return new GameState(room.getGameId(), array, room.isWhiteToMove(), room.getLastMove(), check, checkmate, stalemate, winner);
    }
}
