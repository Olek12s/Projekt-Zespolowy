package com.example.demo.util;

public class MoveValidator {

    public enum PieceType { KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN }
    public enum PieceColor { WHITE, BLACK }
    public record Square(int col, int row) {}
    public record Move(Square from, Square to, PieceType promotion) {}
    public record Piece(PieceType type, PieceColor color) {}

    public class Chessboard {
        private Piece[][] board = new Piece[8][8];
        public Square enPassantTarget = null;
        public boolean whiteCanCastleKingside = true;
        public boolean whiteCanCastleQueenside = true;
        public boolean blackCanCastleKingside = true;
        public boolean blackCanCastleQueenside = true;

        public Chessboard() {
            init();
        }

        private void init() {
            for (int i = 0; i < 8; i++) {
                board[1][i] = new Piece(PieceType.PAWN, PieceColor.BLACK);
                board[6][i] = new Piece(PieceType.PAWN, PieceColor.WHITE);
            }
            setupMajorPieces(0, PieceColor.BLACK);
            setupMajorPieces(7, PieceColor.WHITE);
        }

        private void setupMajorPieces(int row, PieceColor color) {
            board[row][0] = new Piece(PieceType.ROOK, color);
            board[row][1] = new Piece(PieceType.KNIGHT, color);
            board[row][2] = new Piece(PieceType.BISHOP, color);
            board[row][3] = new Piece(PieceType.QUEEN, color);
            board[row][4] = new Piece(PieceType.KING, color);
            board[row][5] = new Piece(PieceType.BISHOP, color);
            board[row][6] = new Piece(PieceType.KNIGHT, color);
            board[row][7] = new Piece(PieceType.ROOK, color);
        }

        public Piece getPiece(int col, int row) {
            if (col < 0 || col > 7 || row < 0 || row > 7) return null;
            return board[row][col];
        }

        public void setPiece(int col, int row, Piece piece) {
            board[row][col] = piece;
        }

        public void makeMove(Move move) {
            Square from = move.from();
            Square to = move.to();
            Piece piece = getPiece(from.col(), from.row());
            if (piece == null) return;

            if (piece.type() == PieceType.KING && Math.abs(to.col() - from.col()) == 2) {
                boolean kingside = to.col() > from.col();
                int rFrom;
                int rTo;
                if (kingside) {
                    rFrom = 7;
                    rTo = 5;
                } else {
                    rFrom = 0;
                    rTo = 3;
                }
                setPiece(rTo, from.row(), getPiece(rFrom, from.row()));
                setPiece(rFrom, from.row(), null);
            }

            if (piece.type() == PieceType.KING) {
                if (piece.color() == PieceColor.WHITE) {
                    whiteCanCastleKingside = false;
                    whiteCanCastleQueenside = false;
                } else {
                    blackCanCastleKingside = false;
                    blackCanCastleQueenside = false;
                }
            }

            if (from.row() == 7 && from.col() == 0) whiteCanCastleQueenside = false;
            if (from.row() == 7 && from.col() == 7) whiteCanCastleKingside = false;
            if (from.row() == 0 && from.col() == 0) blackCanCastleQueenside = false;
            if (from.row() == 0 && from.col() == 7) blackCanCastleKingside = false;

            if (to.row() == 7 && to.col() == 0) whiteCanCastleQueenside = false;
            if (to.row() == 7 && to.col() == 7) whiteCanCastleKingside = false;
            if (to.row() == 0 && to.col() == 0) blackCanCastleQueenside = false;
            if (to.row() == 0 && to.col() == 7) blackCanCastleKingside = false;

            if (piece.type() == PieceType.PAWN && to.equals(enPassantTarget)) {
                setPiece(to.col(), from.row(), null);
            }

            if (piece.type() == PieceType.PAWN && Math.abs(to.row() - from.row()) == 2) {
                enPassantTarget = new Square(from.col(), (from.row() + to.row()) / 2);
            } else {
                enPassantTarget = null;
            }

            Piece promotedP = piece;
            if (piece.type() == PieceType.PAWN && (to.row() == 0 || to.row() == 7)) {
                PieceType promotionType;
                if (move.promotion() != null) {
                    promotionType = move.promotion();
                } else {
                    promotionType = PieceType.QUEEN;
                }
                promotedP = new Piece(promotionType, piece.color());
            }

            setPiece(to.col(), to.row(), promotedP);
            setPiece(from.col(), from.row(), null);
        }
    }

    public boolean isMoveLegal(Chessboard chessboard, String move, boolean whiteToMove) {
        Move m = parseMove(move);
        if (m == null) return false;
        Piece p = chessboard.getPiece(m.from().col(), m.from().row());
        if (p == null) return false;
        if (whiteToMove && p.color() != PieceColor.WHITE) return false;
        if (!whiteToMove && p.color() != PieceColor.BLACK) return false;
        return isMoveLegalInternal(chessboard, m, whiteToMove);
    }

    public Move parseMove(String move) {
        if (move == null || move.length() < 4) return null;
        try {
            int fC = move.charAt(0) - 'a';
            int fR = 7 - (move.charAt(1) - '1');
            int tC = move.charAt(2) - 'a';
            int tR = 7 - (move.charAt(3) - '1');
            PieceType promo = null;
            if (move.length() == 5) {
                promo = switch (move.charAt(4)) {
                    case 'q' -> PieceType.QUEEN;
                    case 'r' -> PieceType.ROOK;
                    case 'b' -> PieceType.BISHOP;
                    case 'n' -> PieceType.KNIGHT;
                    default -> PieceType.QUEEN;
                };
            }
            return new Move(new Square(fC, fR), new Square(tC, tR), promo);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isMoveLegalInternal(Chessboard chessboard, Move m, boolean whiteToMove) {
        Square from = m.from();
        Square to = m.to();
        Piece piece = chessboard.getPiece(from.col(), from.row());
        if (!isValidPieceMove(chessboard, from, to, piece)) return false;

        Piece captured = chessboard.getPiece(to.col(), to.row());
        boolean ep = piece.type() == PieceType.PAWN && to.equals(chessboard.enPassantTarget);
        Square epSq = null;
        Piece epCap = null;
        if (ep) {
            epSq = new Square(to.col(), from.row());
            epCap = chessboard.getPiece(epSq.col(), epSq.row());
        }

        chessboard.setPiece(to.col(), to.row(), piece);
        chessboard.setPiece(from.col(), from.row(), null);
        if (ep) chessboard.setPiece(epSq.col(), epSq.row(), null);

        boolean inCheck = isCheck(chessboard, whiteToMove);

        chessboard.setPiece(from.col(), from.row(), piece);
        chessboard.setPiece(to.col(), to.row(), captured);
        if (ep) chessboard.setPiece(epSq.col(), epSq.row(), epCap);

        return !inCheck;
    }

    public boolean isCheck(Chessboard chessboard, boolean whiteToMove) {
        PieceColor playerColor;
        PieceColor enemyColor;
        if (whiteToMove) {
            playerColor = PieceColor.WHITE;
            enemyColor = PieceColor.BLACK;
        } else {
            playerColor = PieceColor.BLACK;
            enemyColor = PieceColor.WHITE;
        }

        Square kingSq = null;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = chessboard.getPiece(col, row);
                if (p != null && p.type() == PieceType.KING && p.color() == playerColor) {
                    kingSq = new Square(col, row);
                    break;
                }
            }
        }
        if (kingSq == null) return false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = chessboard.getPiece(col, row);
                if (p != null && p.color() == enemyColor) {
                    if (isValidPieceMove(chessboard, new Square(col, row), kingSq, p)) return true;
                }
            }
        }
        return false;
    }

    public boolean isCheckMate(Chessboard chessboard, boolean whiteToMove) {
        if (!isCheck(chessboard, whiteToMove)) return false;
        PieceColor playerColor;
        if (whiteToMove) {
            playerColor = PieceColor.WHITE;
        } else {
            playerColor = PieceColor.BLACK;
        }

        for (int fR = 0; fR < 8; fR++) {
            for (int fC = 0; fC < 8; fC++) {
                Piece p = chessboard.getPiece(fC, fR);
                if (p == null || p.color() != playerColor) continue;
                for (int tR = 0; tR < 8; tR++) {
                    for (int tC = 0; tC < 8; tC++) {
                        Move m = new Move(new Square(fC, fR), new Square(tC, tR), null);
                        if (isMoveLegalInternal(chessboard, m, whiteToMove)) return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isStaleMate(Chessboard chessboard, boolean whiteToMove) {
        if (isCheck(chessboard, whiteToMove)) return false;
        PieceColor playerColor;
        if (whiteToMove) {
            playerColor = PieceColor.WHITE;
        } else {
            playerColor = PieceColor.BLACK;
        }

        for (int fR = 0; fR < 8; fR++) {
            for (int fC = 0; fC < 8; fC++) {
                Piece p = chessboard.getPiece(fC, fR);
                if (p == null || p.color() != playerColor) continue;
                for (int tR = 0; tR < 8; tR++) {
                    for (int tC = 0; tC < 8; tC++) {
                        Move m = new Move(new Square(fC, fR), new Square(tC, tR), null);
                        if (isMoveLegalInternal(chessboard, m, whiteToMove)) return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isValidPieceMove(Chessboard board, Square from, Square to, Piece piece) {
        if (piece == null) return false;
        Piece target = board.getPiece(to.col(), to.row());
        if (target != null && target.color() == piece.color()) return false;
        return switch (piece.type()) {
            case KING -> isValidKingMove(board, from, to, piece.color());
            case KNIGHT -> isValidKnightMove(from, to);
            case BISHOP -> isValidBishopMove(board, from, to);
            case ROOK -> isValidRookMove(board, from, to);
            case QUEEN -> isValidQueenMove(board, from, to);
            case PAWN -> isValidPawnMove(board, from, to, piece.color());
        };
    }

    private boolean isValidKingMove(Chessboard board, Square from, Square to, PieceColor color) {
        int dx = Math.abs(to.col() - from.col());
        int dy = Math.abs(to.row() - from.row());

        if (dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0)) return true;

        if (dy == 0 && dx == 2) {
            boolean kingside = to.col() > from.col();

            if (color == PieceColor.WHITE) {
                if (kingside && !board.whiteCanCastleKingside) return false;
                if (!kingside && !board.whiteCanCastleQueenside) return false;
            } else {
                if (kingside && !board.blackCanCastleKingside) return false;
                if (!kingside && !board.blackCanCastleQueenside) return false;
            }

            if (isSquareAttacked(board, from, color)) return false;

            int rookCol;
            if (kingside) {
                rookCol = 7;
            } else {
                rookCol = 0;
            }
            if (!isPathClear(board, from, new Square(rookCol, from.row()))) return false;

            int middleCol;
            if (kingside) {
                middleCol = from.col() + 1;
            } else {
                middleCol = from.col() - 1;
            }
            Square middle = new Square(middleCol, from.row());
            if (isSquareAttacked(board, middle, color)) return false;
            if (isSquareAttacked(board, to, color)) return false;

            return true;
        }

        return false;
    }

    private boolean isSquareAttacked(Chessboard board, Square square, PieceColor color) {
        PieceColor enemyColor;
        if (color == PieceColor.WHITE) {
            enemyColor = PieceColor.BLACK;
        } else {
            enemyColor = PieceColor.WHITE;
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = board.getPiece(col, row);
                if (p != null && p.color() == enemyColor) {
                    if (isValidPieceMove(board, new Square(col, row), square, p)) return true;
                }
            }
        }
        return false;
    }

    private boolean isValidKnightMove(Square from, Square to) {
        int dx = Math.abs(to.col() - from.col());
        int dy = Math.abs(to.row() - from.row());
        return (dx == 1 && dy == 2) || (dx == 2 && dy == 1);
    }

    private boolean isValidBishopMove(Chessboard board, Square from, Square to) {
        int dx = Math.abs(to.col() - from.col());
        int dy = Math.abs(to.row() - from.row());
        if (dx != dy || dx == 0) return false;
        return isPathClear(board, from, to);
    }

    private boolean isValidRookMove(Chessboard board, Square from, Square to) {
        boolean sameCol = from.col() == to.col();
        boolean sameRow = from.row() == to.row();
        if (!sameCol && !sameRow) return false;
        if (sameCol && sameRow) return false;
        return isPathClear(board, from, to);
    }

    private boolean isValidQueenMove(Chessboard board, Square from, Square to) {
        return isValidBishopMove(board, from, to) || isValidRookMove(board, from, to);
    }

    private boolean isValidPawnMove(Chessboard board, Square from, Square to, PieceColor color) {
        int dir;
        if (color == PieceColor.WHITE) {
            dir = -1;
        } else {
            dir = 1;
        }
        int dx = to.col() - from.col();
        int dy = to.row() - from.row();

        if (dx == 0 && dy == dir) {
            return board.getPiece(to.col(), to.row()) == null;
        }

        int startRow;
        if (color == PieceColor.WHITE) {
            startRow = 6;
        } else {
            startRow = 1;
        }
        if (dx == 0 && dy == 2 * dir && from.row() == startRow) {
            return board.getPiece(from.col(), from.row() + dir) == null && board.getPiece(to.col(), to.row()) == null;
        }

        if (Math.abs(dx) == 1 && dy == dir) {
            Piece target = board.getPiece(to.col(), to.row());
            if (target != null && target.color() != color) return true;
            return to.equals(board.enPassantTarget);
        }

        return false;
    }

    private boolean isPathClear(Chessboard board, Square from, Square to) {
        int dx = Integer.compare(to.col(), from.col());
        int dy = Integer.compare(to.row(), from.row());
        int col = from.col() + dx;
        int row = from.row() + dy;
        while (col != to.col() || row != to.row()) {
            if (board.getPiece(col, row) != null) return false;
            col += dx;
            row += dy;
        }
        return true;
    }
}