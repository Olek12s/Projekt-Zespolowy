package com.example.demo.util.validator.standardMoves;

import com.example.demo.util.MoveValidator;
import org.junit.jupiter.api.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveEnPassantTest
{
    @Test
    public void testEnPassant() {
        MoveValidator validator = new MoveValidator();
        MoveValidator.Chessboard board = validator.new Chessboard();

        board.setPiece(4, 3, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.WHITE));
        board.setPiece(3, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));

        boolean whiteToMove = false;

        MoveValidator.Move blackDoublePush = validator.parseMove("d7d5");
        assertNotNull(blackDoublePush);

        board.makeMove(blackDoublePush);

        MoveValidator.Move enPassant = new MoveValidator.Move(new MoveValidator.Square(4, 3), new MoveValidator.Square(3, 2), null);

        assertTrue("En passant should be legal", validator.isMoveLegal(board, "exd6", true));
        board.makeMove(enPassant);
        assertNull("Black pawn should be captured via en passant", board.getPiece(3, 1));

        assertNotNull(board.getPiece(3, 2));
        assertEquals(MoveValidator.PieceType.PAWN, board.getPiece(3, 2).type());
    }

    @Test
    public void testEnPassantExpiresAfterOneMove() {
        MoveValidator validator = new MoveValidator();
        MoveValidator.Chessboard board = validator.new Chessboard();

        board.setPiece(4, 3, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.WHITE));
        board.setPiece(3, 1, new MoveValidator.Piece(MoveValidator.PieceType.PAWN, MoveValidator.PieceColor.BLACK));

        boolean whiteToMove = false;

        MoveValidator.Move blackDoublePush = new MoveValidator.Move(new MoveValidator.Square(3, 1), new MoveValidator.Square(3, 3), null);
        board.makeMove(blackDoublePush);
        board.setPiece(0, 0, new MoveValidator.Piece(MoveValidator.PieceType.KNIGHT, MoveValidator.PieceColor.WHITE));

        MoveValidator.Move randomWhiteMove = new MoveValidator.Move(new MoveValidator.Square(0, 0), new MoveValidator.Square(2, 1), null);
        board.makeMove(randomWhiteMove);

        boolean legal = validator.isMoveLegal(board, "exd6", true);
        assertFalse("En passant should NOT be legal after one move", legal);
    }
}
