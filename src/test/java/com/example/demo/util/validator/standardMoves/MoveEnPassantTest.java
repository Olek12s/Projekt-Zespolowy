package com.example.demo.util.validator.standardMoves;

import com.example.demo.util.MoveValidator;
import com.example.demo.util.validator.MoveValidatorTester;
import org.junit.jupiter.api.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveEnPassantTest extends MoveValidatorTester
{
    @Test
    public void testEnPassant() {
        chessboard.setPiece(4, 3, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.WHITE));
        chessboard.setPiece(3, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));

        // black double forward
        assertNotNull(validator.parseMove("d7d5"));
        chessboard.makeMove(validator.parseMove("d7d5"));

        MoveValidator.Move enPassant = new MoveValidator.Move(new MoveValidator.Square(4, 3), new MoveValidator.Square(3, 2), null);

        assertTrue("En passant should be legal", legal("exd6", true));

        chessboard.makeMove(enPassant);

        assertNull("Black pawn should be captured via en passant", chessboard.getPiece(3, 1));
        assertNotNull(chessboard.getPiece(3, 2));
        assertEquals(MoveValidator.PieceType.PAWN, chessboard.getPiece(3, 2).type());
    }

    @Test
    public void testEnPassantExpiresAfterOneMove() {
        reloadChessboard();

        chessboard.setPiece(4, 3, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.WHITE));
        chessboard.setPiece(3, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));

        MoveValidator.Move blackDoublePush = new MoveValidator.Move(new MoveValidator.Square(3, 1), new MoveValidator.Square(3, 3), null);
        chessboard.makeMove(blackDoublePush);

        // any white move
        chessboard.setPiece(0, 0, new MoveValidator.Piece(MoveValidator.PieceType.KNIGHT, MoveValidator.PieceColor.WHITE));

        MoveValidator.Move randomWhiteMove = new MoveValidator.Move(new MoveValidator.Square(0, 0), new MoveValidator.Square(2, 1), null);

        assertTrue("En passant should be legal after one move", legal("exd6", true));
        chessboard.makeMove(randomWhiteMove);
        assertFalse("En passant should NOT be legal after one move", legal("exd6", true));
    }

    @Test
    public void testEnPassantCheck() {
        chessboard.setPiece(4, 3, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.WHITE));
        chessboard.setPiece(3, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));

        MoveValidator.Move blackDoublePush = new MoveValidator.Move(new MoveValidator.Square(3, 1), new MoveValidator.Square(3, 3), null);
        chessboard.makeMove(blackDoublePush);
        assertFalse(validator.isCheck(chessboard, true));
        chessboard.makeMove(new MoveValidator.Move(new MoveValidator.Square(4, 3), new MoveValidator.Square(3, 2), null));

        // move king to c7
        chessboard.setPiece(4, 0, null);
        chessboard.setPiece(2, 1, new MoveValidator.Piece(MoveValidator.PieceType.KING, MoveValidator.PieceColor.BLACK));
        assertTrue(validator.isCheck(chessboard, false));
        assertFalse(validator.isCheckMate(chessboard, false));
        assertFalse(validator.isStaleMate(chessboard, false));
    }

//    @Test
//    public void testEnPassantCheckmate() {
//        chessboard.setPiece(4, 3, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.WHITE));
//        chessboard.setPiece(3, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
//
//        MoveValidator.Move blackDoublePush = new MoveValidator.Move(new MoveValidator.Square(3, 1), new MoveValidator.Square(3, 3), null);
//        chessboard.makeMove(blackDoublePush);
//        assertFalse(validator.isCheck(chessboard, true));
//        chessboard.makeMove(new MoveValidator.Move(new MoveValidator.Square(4, 3), new MoveValidator.Square(3, 2), null));
//
//        // move king to c7 and block him with pawns and set bishop to protect white pawn at d6
//        chessboard.setPiece(4, 0, null);
//        chessboard.setPiece(4, 1, null);
//        chessboard.setPiece(2, 1, new MoveValidator.Piece(MoveValidator.PieceType.KING, MoveValidator.PieceColor.BLACK));
//        chessboard.setPiece(3, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
//        chessboard.setPiece(2, 2, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
//        chessboard.setPiece(1, 2, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
//        chessboard.setPiece(6, 5, new MoveValidator.Piece(MoveValidator.PieceType.BISHOP, MoveValidator.PieceColor.WHITE));
//
//        System.out.println(chessboard);
//        assertTrue(validator.isCheck(chessboard, false));
//        assertTrue(validator.isCheckMate(chessboard, false));
//        assertFalse(validator.isStaleMate(chessboard, false));
//    }

    @Test
    public void testEnPassantCheckmate() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                chessboard.setPiece(c, r, null);
            }
        }

        chessboard.setPiece(4, 3, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.WHITE));
        chessboard.setPiece(3, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
        chessboard.setPiece(2, 1, new MoveValidator.Piece(MoveValidator.PieceType.KING, MoveValidator.PieceColor.BLACK));
        chessboard.setPiece(2, 2, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
        chessboard.setPiece(1, 2, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
        chessboard.setPiece(1, 0, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
        chessboard.setPiece(2, 0, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
        chessboard.setPiece(3, 0, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
        chessboard.setPiece(1, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
        //chessboard.setPiece(3, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
        chessboard.setPiece(6, 5, new MoveValidator.Piece(MoveValidator.PieceType.BISHOP, MoveValidator.PieceColor.WHITE));

        MoveValidator.Move blackDoublePush = new MoveValidator.Move(new MoveValidator.Square(3, 1), new MoveValidator.Square(3, 3), null);
        chessboard.makeMove(blackDoublePush);

        // en passant
        chessboard.setPiece(3, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));
        chessboard.makeMove(new MoveValidator.Move(new MoveValidator.Square(4, 3), new MoveValidator.Square(3, 2), null));


        assertTrue(validator.isCheck(chessboard, false));
        assertTrue(validator.isCheckMate(chessboard, false));
        assertFalse(validator.isStaleMate(chessboard, false));
    }

    @Test
    public void testEnPassantStalemateFalse() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                chessboard.setPiece(c, r, null);
            }
        }

        chessboard.setPiece(4, 3, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.WHITE)); // e5
        chessboard.setPiece(3, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK)); // d7
        chessboard.setPiece(0, 0, new MoveValidator.Piece(MoveValidator.PieceType.KING, MoveValidator.PieceColor.BLACK)); // a8
        chessboard.setPiece(0, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK)); // a7
        chessboard.setPiece(1, 0, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK)); // b8
        chessboard.setPiece(1, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK)); // b7
        chessboard.setPiece(7, 7, new MoveValidator.Piece(MoveValidator.PieceType.KING, MoveValidator.PieceColor.WHITE));

        MoveValidator.Move blackDoublePush = new MoveValidator.Move(new MoveValidator.Square(3, 1), new MoveValidator.Square(3, 3), null);
        chessboard.makeMove(blackDoublePush);

        // en passant
        chessboard.makeMove(new MoveValidator.Move(new MoveValidator.Square(4, 3), new MoveValidator.Square(3, 2), null));

        assertFalse(validator.isCheck(chessboard, false));
        assertFalse(validator.isCheckMate(chessboard, false));
        assertFalse(validator.isStaleMate(chessboard, false));
    }
}
