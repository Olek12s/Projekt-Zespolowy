package com.example.demo;

import com.example.demo.util.MoveValidator;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

public class MoveValidatorTest {

    private MoveValidator validator = new MoveValidator();

    @Test
    public void testMoves() {
        MoveValidator.Chessboard board = validator.new Chessboard();

        String moves = "d2d4 c7c5 c2c3 c5d4 c3d4 d7d5 b1c3 g8f6 g1f3 g7g6 e2e3 f8g7 f1e2 e8g8 e1g1 e7e6 b2b3 a7a6 c1a3 f8e8 f3e5 b8c6 e5c6 b7c6 c3a4 f6d7 a1c1 c8b7 f2f4 g7f8 a3f8 e8f8 d1d2 d8e7 a4c5 d7c5 d4c5 a6a5 d2c3 f8b8 f1f3 f7f6 e2d3 e6e5 e3e4 d5d4 c3c2 g8h8 c1f1 b8f8 f4f5 g6g5 f3h3 b7a6 d3a6 a8a6 f1c1 a5a4 b3b4 a4a3 c2e2 a6a7 b4b5 c6b5 h3b3 a7c7 e2b5 f8d8 c5c6 e7d6 b5d5 d6d5 e4d5 d8d5 b3b8 h8g7 b8b6 d5d6 g1f2 g7f7 b6b7 c7b7 c6b7 d6b6 c1c7 f7e8 c7c8 e8d7 b7b8q b6b8 c8b8 d7d6 b8h8 d6d5 h8h7 e5e4 h7e7 d5c4 e7e4 c4d3 e4e6 d3c2 e6f6 d4d3 f2e3 d3d2 f6c6 c2b2 e3d2 b2a2 f5f6 a2a1 f6f7 g5g4 f7f8q g4g3 f8a3";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;

        for (String move : allMoves) {
            System.out.println("Ruch poprawny: " + move);
            assertTrue(validator.isMoveLegal(board, move, whiteToMove), "Ruch nielegalny: " + move);
            MoveValidator.Move m = validator.parseMove(move);
            board.makeMove(m);
            whiteToMove = !whiteToMove;
        }
    }
}