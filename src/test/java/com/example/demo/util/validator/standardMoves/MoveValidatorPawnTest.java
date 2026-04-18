package com.example.demo.util.validator.standardMoves;

import com.example.demo.util.MoveValidator;
import com.example.demo.util.validator.MoveValidatorTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class MoveValidatorPawnTest extends MoveValidatorTester {

    // ========== FROM STARTING LAYOUT ==========

    @Test
    public void shouldAllowPawnMoveTwoSquaresFromStart() {
        char square = 'a';
        for (int i = 0; i < 8; i++) {
            String move = "" + square + "2" + square + "4";

            assertTrue(move + " should be legal", legal(move, true));
            assertFalse(move + " should be illegal for black", legal(move, false));

            square++;
        }
    }

    @Test
    public void shouldNotAllowPawnMoveTwoSquaresFromStart() {
        char square = 'a';
        for (int i = 0; i < 8; i++) {
            String move = "" + square + "2" + square + "4";

            assertFalse(move + " should be legal", legal(move, false));
            assertTrue(move + " should be illegal for black", legal(move, true));

            square++;
        }
    }

    @Test
    public void shouldAllowPawnMoveOneSquareFromStart() {
        char square = 'a';
        for (int i = 0; i < 8; i++) {
            String move = "" + square + "2" + square + "3";

            assertTrue(move + " should be legal", legal(move, true));
            assertFalse(move + " should be illegal for black", legal(move, false));

            square++;
        }
    }

    @Test
    public void shouldNotAllowPawnMoveOneSquareFromStart() {
        char square = 'a';
        for (int i = 0; i < 8; i++) {
            String move = "" + square + "2" + square + "3";

            assertFalse(move + " should be legal", legal(move, false));
            assertTrue(move + " should be illegal for black", legal(move, true));

            square++;
        }
    }

    @Test
    public void shouldNotAllowDiagonalPawnMovesFromStart() {

        // for white
        for (char file = 'a'; file <= 'h'; file++) {

            // left diagonal (b2 -> a3)
            if (file > 'a') {
                String moveLeft = "" + file + "2" + (char)(file - 1) + "3";
                assertFalse(moveLeft + " should be illegal", legal(moveLeft, true));
            }

            // right diagonal (b2 -> c3)
            if (file < 'h') {
                String moveRight = "" + file + "2" + (char)(file + 1) + "3";
                assertFalse(moveRight + " should be illegal", legal(moveRight, true));
            }
        }

        // for black
        for (char file = 'a'; file <= 'h'; file++) {

            // left diagonal (b7 -> a6)
            if (file > 'a') {
                String moveLeft = "" + file + "7" + (char)(file - 1) + "6";
                assertFalse(moveLeft + " should be illegal", legal(moveLeft, false));
            }

            // right diagonal (b7 -> c6)
            if (file < 'h') {
                String moveRight = "" + file + "7" + (char)(file + 1) + "6";
                assertFalse(moveRight + " should be illegal", legal(moveRight, false));
            }
        }
    }

    @Test
    public void shouldNotAllowPawnMoveTwoDiagonalSquaresFromStart() {

        // for white
        for (char file = 'a'; file <= 'h'; file++) {
            if (file > 'a') {
                String moveLeftTwo = "" + file + "2" + (char)(file - 1) + "4";
                assertFalse(moveLeftTwo + " should be illegal", legal(moveLeftTwo, true));
            }

            if (file < 'h') {
                String moveRightTwo = "" + file + "2" + (char)(file + 1) + "4";
                assertFalse(moveRightTwo + " should be illegal", legal(moveRightTwo, true));
            }
        }

        // for black
        for (char file = 'a'; file <= 'h'; file++) {
            if (file > 'a') {
                String moveLeftTwo = "" + file + "7" + (char)(file - 1) + "5";
                assertFalse(moveLeftTwo + " should be illegal", legal(moveLeftTwo, false));
            }

            if (file < 'h') {
                String moveRightTwo = "" + file + "7" + (char)(file + 1) + "5";
                assertFalse(moveRightTwo + " should be illegal", legal(moveRightTwo, false));
            }
        }
    }
//
//
//    // ========== FROM PAWN ON MIDDLE LAYOUT, NOT BLOCKED AT ANY TEST ==========
//
    @Test
    public void shouldAllowPawnMoveTwoSquaresFromMiddle() {
        // for whites (set them on row 4)
        for (int col = 0; col < 8; col++) {

            chessboard.setPiece(col, 4, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.WHITE));

            char file = (char) ('a' + col);

            assertFalse(legal("" + file + "4" + file + "6", true));
            assertFalse(legal("" + file + "4" + file + "7", true));
        }

        // for blacks (set them on row 5)
        for (int col = 0; col < 8; col++) {

            // reset board AFTER whites
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    chessboard.setPiece(c, r, null);
                }
            }

            chessboard.setPiece(col, 5, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));

            char file = (char) ('a' + col);

            assertFalse(legal("" + file + "5" + file + "3", false));
            assertFalse(legal("" + file + "5" + file + "2", false));
        }
    }

    @Test
    public void shouldAllowPawnMoveOneSquareFromMiddleWhite() {
        // for whites (set them on row 4)
        for (int col = 0; col < 8; col++) {
            chessboard.setPiece(col, 4, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.WHITE));

            char file = (char) ('a' + col);

            assertTrue(legal("" + file + "4" + file + "5", true));
            assertFalse(legal("" + file + "4" + file + "6", true));

            assertFalse(legal("" + file + "4" + file + "5", false));
            assertFalse(legal("" + file + "4" + file + "6", false));
        }
    }

    @Test
    public void shouldAllowPawnMoveOneSquareFromMiddleBlack() {
        for (int col = 0; col < 8; col++) {
            reloadChessboard();
            chessboard.setPiece(col, 3, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
            char file = (char) ('a' + col);

            assertTrue(legal(file + "4", false));   // x4
            assertFalse(legal(file + "5", false));  // x5

        }
    }

    @Test
    public void shouldAllowPawnMoveOneDiagonalSquareFromMiddle() {
        reloadChessboard();
        chessboard.setPiece(2, 4, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
        chessboard.setPiece(1, 1, null);
        chessboard.setPiece(3, 1, null);

        assertFalse(legal("b5", false));   // left backwards
        assertFalse(legal("d5", false));   // right backwards
        assertFalse(legal("b3", false));   // left forward
        assertFalse(legal("d3", false));   // right forward

        assertFalse(legal("c4b5", false));   // left backwards
        assertFalse(legal("c4d5", false));   // right backwards
        assertFalse(legal("c4b3", false));   // left forward
        assertFalse(legal("c4d3", false));   // right forward

    }

    @Test
    public void shouldAllowPawnMoveTwoDiagonalSquaresFromMiddle() {
        reloadChessboard();
        chessboard.setPiece(2, 3, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
        assertFalse(legal("a3", false));   // left forward
        assertFalse(legal("e3", false));   // right forward

        reloadChessboard();
        chessboard.setPiece(2, 4, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
        chessboard.setPiece(0, 1, null);
        chessboard.setPiece(4, 1, null);
        assertFalse(legal("a6", false));   // left backwards
        assertFalse(legal("e6", false));   // right backwards
    }

//    // ========== FROM PAWN TO OTHER PAWN SAME COLOR ON STARTING LAYOUT ==========

    @Test
    public void shouldAllowPawnMoveOntoOtherPawnFromStart() {
        char code = 'a';
        for (int i = 0; i < 8; i++) {
            assertFalse(legal(code + "7", false));
            code++;
        }

        code = 'a';
        for (int i = 0; i < 8; i++) {
            assertFalse(legal(code + "2", true));
            code++;
        }
    }
}
