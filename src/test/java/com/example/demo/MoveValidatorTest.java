package com.example.demo;

import com.example.demo.util.MoveValidator;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MoveValidatorTest {

    private MoveValidator validator = new MoveValidator();

    @Test
    public void testFullGame() {
        MoveValidator.Chessboard board = validator.new Chessboard();
        String moves = "d4 d5 c4 e6 Nc3 Be7 cxd5 exd5 Bf4 Nf6 e3 Bf5 Nge2 O-O Ng3 Be6 Bd3 c5 dxc5 Bxc5 O-O Nc6 Rc1 Bd6 Nh5 Be7 Nb5 Nxh5 Qxh5 g6 Qf3 Rc8 Rfd1 Qd7 h3 Rfd8 Qg3 Nb4 Nc3 Nxd3 Rxd3 Bf5 Rd2 Qe6 Rcd1 h5 h4 Rc5 f3 Qc6 e4 Rxc3 bxc3 Qb6+ Kh2 dxe4 Rxd8+ Bxd8 Be3 Qa5 Qb8 Qc7+ Qxc7 Bxc7+ Kg1 exf3 gxf3 b6 Kf2 Kf8 Rd4 Ke7 Bf4 Bxf4 Rxf4 Kd6 Ke3 Kc5 Rd4 Be6 a3 a5 Ke4 b5 Ke5 a4 f4 Kc6 Kf6 Kc5 Rb4 Bc4 Ke7 Be6 Re4 Kd5 Rd4+ Kc6 Kd8 Bf5 Ke8 Be6 Kf8 Kc5 Kg7 Kc6 Kg8 Kc5 Kf8 Kc6 Kg7 Kc5 Kh8 Kc6 Kh7 Kc5 Kh6 Bf5 Kg5 Be6 Kf6 Kc6 f5 Bxf5 Kxf7 Kc5 Kf6 Bc2 Ke7 Bf5 Kd8 Kc6 Rf4 Kd6 Rb4 Kc5 Kc7 Bd3 Rd4 Be2 Kb7 Bf1 Ka7 Be2 Re4 Bd3 Rb4 Bc4 Ka6 Kd5 Ka5 Ke5 Rb1 g5 hxg5 Kf5 Rg1 Kg6 Kb4 h4 Kc5 h3 Kd4 Be6 Ke5 Bd7 Kf4 Bc6 Kg3 Kxg5 Rd1 h2 Kxh2 Kf4 c4 bxc4 Rd4+ Ke5 Rxc4 Kd5 Rb4 Kc5 Kg3 Bb5 Kf4 Kb6 Ke3 Ka5 Kd4 Be2 Rb1 Bh5 Re1 Bf7 Kc5 Bb3 Re8 Ka6 Kc6 Ka7 Kb5 Kb7 Re7+ Kc8 Kc6 Kd8 Rd7+ Ke8 Kc7 Bc2 Rd2 Bb3 Re2+ Kf7 Kd6 Bc4 Re7+ Kf8 Re4 Bb3 Kd7 Kf7 Rf4+ Kg6 Kd6 Kg5 Ke5 Kg6 Rf3 Kg7 Rf6 Bc4 Kf5 Bb3 Kg5 Bc2";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;

        for (int i = 0; i < allMoves.length; i++) {
            String m = allMoves[i];
            assertTrue(validator.isMoveLegal(board, m, whiteToMove), "Ruch nielegalny: " + m);

            MoveValidator.Move move = validator.fromSAN(board, m, whiteToMove);
            board.makeMove(move);
            System.out.println((i + 1) + ". " + (whiteToMove ? "Biale: " : "Czarne: ") + m);
            whiteToMove = !whiteToMove;

            if (validator.isCheckMate(board, whiteToMove)) {
                System.out.println("Koniec: Mat na " + (whiteToMove ? "Bialych" : "Czarnych"));
                break;
            } else if (validator.isStaleMate(board, whiteToMove)) {
                System.out.println("Koniec: Pat");
                break;
            } else if (validator.isCheck(board, whiteToMove)) {
                System.out.println("Szach na " + (whiteToMove ? "Bialych" : "Czarnych"));
            }
        }
        System.out.println("Koniec: Remis (1/2-1/2)");
    }
}