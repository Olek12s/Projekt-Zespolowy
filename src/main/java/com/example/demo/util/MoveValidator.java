package com.example.demo.util;

import java.util.Arrays;

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
                if (kingside) {
                    rFrom = 7;
                } else {
                    rFrom = 0;
                }
                int rTo;
                if (kingside) {
                    rTo = 5;
                } else {
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

        private String pieceToChar(Piece piece) {
            if (piece == null) return ".";

            return switch (piece.type()) {
                case KING -> piece.color() == PieceColor.WHITE ? "K" : "k";
                case QUEEN -> piece.color() == PieceColor.WHITE ? "Q" : "q";
                case ROOK -> piece.color() == PieceColor.WHITE ? "R" : "r";
                case BISHOP -> piece.color() == PieceColor.WHITE ? "B" : "b";
                case KNIGHT -> piece.color() == PieceColor.WHITE ? "N" : "n";
                case PAWN -> piece.color() == PieceColor.WHITE ? "P" : "p";
            };
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("  a b c d e f g h\n");

            for (int row = 0; row < 8; row++)
            {
                int displayRow = 8 - row;
                sb.append(displayRow + " ");

                for (int col = 0; col < 8; col++) {
                    sb.append(pieceToChar(board[row][col]) + " ");
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    //główna funkcja
    public boolean isMoveLegal(Chessboard chessboard, String move, boolean whiteToMove) {
        Move m = fromSAN(chessboard, move, whiteToMove);
        if (m == null) return false;
        Piece p = chessboard.getPiece(m.from().col(), m.from().row());
        if (p == null) return false;
        if (whiteToMove && p.color() != PieceColor.WHITE) return false;
        if (!whiteToMove && p.color() != PieceColor.BLACK) return false;
        return isMoveLegalInternal(chessboard, m, whiteToMove);
    }

    //dla uci
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
        if (whiteToMove) {
            playerColor = PieceColor.WHITE;
        } else {
            playerColor = PieceColor.BLACK;
        }

        PieceColor enemyColor;
        if (whiteToMove) {
            enemyColor = PieceColor.BLACK;
        } else {
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

            if (isSquareAttacked(board, new Square(middleCol, from.row()), color)) return false;
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
        if (from.col() != to.col() && from.row() != to.row()) return false;
        if (from.col() == to.col() && from.row() == to.row()) return false;
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

        if (dx == 0 && dy == dir) return board.getPiece(to.col(), to.row()) == null;

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


    //uci -> SAN
    public String toSAN(Chessboard board, Move move, boolean whiteToMove) {
        Piece piece = board.getPiece(move.from().col(), move.from().row());
        if (piece == null) return "";

        if (piece.type() == PieceType.KING && Math.abs(move.to().col() - move.from().col()) == 2) {
            if (move.to().col() > move.from().col()) {
                return "O-O";
            } else {
                return "O-O-O";
            }
        }

        StringBuilder sb = new StringBuilder();
        Piece target = board.getPiece(move.to().col(), move.to().row());
        boolean isCapture = target != null || (piece.type() == PieceType.PAWN && move.to().equals(board.enPassantTarget));

        if (piece.type() != PieceType.PAWN) {
            sb.append(getCharFromPieceType(piece.type()));
            sb.append(getDisambiguation(board, move, piece, whiteToMove));
        } else if (isCapture) {
            sb.append((char) ('a' + move.from().col()));
        }

        if (isCapture) sb.append("x");
        sb.append((char) ('a' + move.to().col()));
        sb.append(8 - move.to().row());

        if (move.promotion() != null) sb.append("=").append(getCharFromPieceType(move.promotion()));

        if (isCheckMateAfterMove(board, move, whiteToMove)) sb.append("#");
        else if (isCheckAfterMove(board, move, whiteToMove)) sb.append("+");

        return sb.toString();
    }

    private String getDisambiguation(Chessboard board, Move move, Piece piece, boolean whiteToMove) {
        Square from = move.from();
        Square to = move.to();
        boolean sameFile = false;
        boolean sameRank = false;
        int ambiguousCount = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (col == from.col() && row == from.row()) continue;
                Piece p = board.getPiece(col, row);
                if (p != null && p.type() == piece.type() && p.color() == piece.color()) {
                    if (isMoveLegalInternal(board, new Move(new Square(col, row), to, null), whiteToMove)) {
                        ambiguousCount++;
                        if (col == from.col()) sameFile = true;
                        if (row == from.row()) sameRank = true;
                    }
                }
            }
        }

        if (ambiguousCount == 0) return "";
        if (!sameFile) return String.valueOf((char) ('a' + from.col()));
        if (!sameRank) return String.valueOf(8 - from.row());
        return "" + (char) ('a' + from.col()) + (8 - from.row());
    }

    private boolean isCheckAfterMove(Chessboard board, Move move, boolean whiteToMove) {
        Piece piece = board.getPiece(move.from().col(), move.from().row());
        Piece captured = board.getPiece(move.to().col(), move.to().row());

        board.setPiece(move.to().col(), move.to().row(), piece);
        board.setPiece(move.from().col(), move.from().row(), null);

        boolean check = isCheck(board, !whiteToMove);

        board.setPiece(move.from().col(), move.from().row(), piece);
        board.setPiece(move.to().col(), move.to().row(), captured);

        return check;
    }

    private boolean isCheckMateAfterMove(Chessboard board, Move move, boolean whiteToMove) {
        Piece piece = board.getPiece(move.from().col(), move.from().row());
        Piece captured = board.getPiece(move.to().col(), move.to().row());

        board.setPiece(move.to().col(), move.to().row(), piece);
        board.setPiece(move.from().col(), move.from().row(), null);

        boolean mate = isCheckMate(board, !whiteToMove);

        board.setPiece(move.from().col(), move.from().row(), piece);
        board.setPiece(move.to().col(), move.to().row(), captured);

        return mate;
    }

    private char getCharFromPieceType(PieceType type) {
        return switch (type) {
            case KING -> 'K';
            case QUEEN -> 'Q';
            case ROOK -> 'R';
            case BISHOP -> 'B';
            case KNIGHT -> 'N';
            default -> ' ';
        };
    }

    public Move fromSAN(Chessboard board, String san, boolean whiteToMove) {
        if (san == null || san.isEmpty()) return null;
        PieceColor color;
        if (whiteToMove) {
            color = PieceColor.WHITE;
        } else {
            color = PieceColor.BLACK;
        }

        if (san.equals("O-O") || san.equals("O-O+") || san.equals("O-O#")) {
            int row;
            if (whiteToMove) {
                row = 7;
            } else {
                row = 0;
            }
            return new Move(new Square(4, row), new Square(6, row), null);
        }
        if (san.equals("O-O-O") || san.equals("O-O-O+") || san.equals("O-O-O#")) {
            int row;
            if (whiteToMove) {
                row = 7;
            } else {
                row = 0;
            }
            return new Move(new Square(4, row), new Square(2, row), null);
        }

        String s = san.replaceAll("[+#]$", "");

        PieceType promotion = null;
        if (s.contains("=")) {
            char promoChar = s.charAt(s.indexOf('=') + 1);
            promotion = switch (promoChar) {
                case 'Q' -> PieceType.QUEEN;
                case 'R' -> PieceType.ROOK;
                case 'B' -> PieceType.BISHOP;
                case 'N' -> PieceType.KNIGHT;
                default -> PieceType.QUEEN;
            };
            s = s.substring(0, s.indexOf('='));
        }

        PieceType pieceType;
        if (Character.isUpperCase(s.charAt(0)) && s.charAt(0) != 'O') {
            pieceType = switch (s.charAt(0)) {
                case 'K' -> PieceType.KING;
                case 'Q' -> PieceType.QUEEN;
                case 'R' -> PieceType.ROOK;
                case 'B' -> PieceType.BISHOP;
                case 'N' -> PieceType.KNIGHT;
                default -> PieceType.PAWN;
            };
            s = s.substring(1);
        } else {
            pieceType = PieceType.PAWN;
        }

        s = s.replace("x", "");

        int toCol = s.charAt(s.length() - 2) - 'a';
        int toRow = 7 - (s.charAt(s.length() - 1) - '1');
        Square to = new Square(toCol, toRow);

        String disambig = s.substring(0, s.length() - 2);
        Integer disambigCol = null;
        Integer disambigRow = null;
        for (char c : disambig.toCharArray()) {
            if (c >= 'a' && c <= 'h') disambigCol = c - 'a';
            else if (c >= '1' && c <= '8') disambigRow = 7 - (c - '1');
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = board.getPiece(col, row);
                if (p == null || p.type() != pieceType || p.color() != color) continue;
                if (disambigCol != null && col != disambigCol) continue;
                if (disambigRow != null && row != disambigRow) continue;
                Move candidate = new Move(new Square(col, row), to, promotion);
                if (isMoveLegalInternal(board, candidate, whiteToMove)) return candidate;
            }
        }
        return null;
    }
}