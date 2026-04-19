package com.example.demo.util.validator.standardMoves;


import com.example.demo.util.MoveValidator;
import com.example.demo.util.validator.MoveValidatorTester;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

// Games reference: https://lichess.org/study/oFKUk5p9/jYsmWUpR
public class MoveValidatorSixteenMostPopularChessGamesTest extends MoveValidatorTester
{
    @Test
    public void gameByrneVsFischer1956() {
        String moves = "Nf3 Nf6 c4 g6 Nc3 Bg7 d4 O-O Bf4 d5 Qb3 dxc4 Qxc4 c6 e4 Nbd7 Rd1 Nb6 Qc5 Bg4 Bg5";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameBotvinnikVsTal1960() {
        String moves = "c4 Nf6 Nf3 g6 g3 Bg7 Bg2 O-O d4 d6 Nc3 Nbd7 O-O e5 e4 c6 h3 Qb6 d5 cxd5 cxd5 Nc5 Ne1 Bd7 Nd3 Nxd3 Qxd3 Rfc8 Rb1 Nh5 Be3 Qb4 Qe2 Rc4 Rfc1 Rac8 Kh2 f5 exf5 Bxf5 Ra1 Nf4 gxf4 exf4 Bd2 Qxb2 Rab1 f3 Rxb2 fxe2 Rb3 Rd4 Be1 Be5+ Kg1 Bf4 Nxe2 Rxc1 Nxd4 Rxe1+ Bf1 Be4 Ne2 Be5 f4 Bf6 Rxb7 Bxd5 Rc7 Bxa2 Rxa7 Bc4 Ra8+ Kf7 Ra7+ Ke6 Ra3 d5 Kf2 Bh4+ Kg2 Kd6 Ng3 Bxg3 Bxc4 dxc4 Kxg3 Kd5 Ra7 c3 Rc7 Kd4 Rd7+";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameMorphyVsChamouillet1858() {
        String moves = "e4 e6 d4 d5 exd5 exd5 Nf3 Nf6 Bd3 Bd6 O-O O-O Nc3 c5 dxc5 Bxc5 Bg5 Be6 Qd2 Nc6 Rad1 Be7 Rfe1 a6 Qf4 Nh5 Qh4 g6 g4 Nf6 h3 Rc8 a3 Re8 Ne2 h5 Nf4 Nh7 Nxe6 fxe6 Rxe6 Bxg5 Rxg6+ Kf8 Qxh5 Rc7 Nxg5 Ree7 Qh6+ Ke8 Rg8+";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameShortVsTimman1991() {
        String moves = "e4 Nf6 e5 Nd5 d4 d6 Nf3 g6 Bc4 Nb6 Bb3 Bg7 Qe2 Nc6 O-O O-O h3 a5 a4 dxe5 dxe5 Nd4 Nxd4 Qxd4 Re1 e6 Nd2 Nd5 Nf3 Qc5 Qe4 Qb4 Bc4 Nb6 b3 Nxc4 bxc4 Re8 Rd1 Qc5 Qh4 b6 Be3 Qc6 Bh6 Bh8 Rd8 Bb7 Rad1 Bg7 R8d7 Rf8 Bxg7 Kxg7 R1d4 Rae8 Qf6+ Kg8 h4 h5 Kh2 Rc8 Kg3 Rce8 Kf4 Bc8 Kg5";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameAnderssenVsBagration1851() {
        String moves = "e4 e5 f4 exf4 Bc4 Qh4+ Kf1 b5 Bxb5 Nf6 Nf3 Qh6 d3 Nh5 Nh4 Qg5 Nf5 c6 g4 Nf6 Rg1 cxb5 h4 Qg6 h5 Qg5 Qf3 Ng8 Bxf4 Qf6 Nc3 Bc5 Nd5 Qxb2 Bd6 Bxg1 e5 Qxa1+ Ke2 Na6 Nxg7+ Kd8 Qf6+ Nxf6 Be7#";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameAronianVsAnand2013() {
        String moves = "d4 d5 c4 c6 Nf3 Nf6 Nc3 e6 e3 Nbd7 Bd3 dxc4 Bxc4 b5 Bd3 Bd6 O-O O-O Qc2 Bb7 a3 Rc8 Ng5 c5 Nxh7 Ng4 f4 cxd4 exd4 Bc5 Be2 Nde5 Bxg4 Bxd4+ Kh1 Nxg4 Nxf8 f5 Ng6 Qf6 h3 Qxg6 Qe2 Qh5 Qd3 Be3";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameBotvinnikVsCapablanca1938() {
        String moves = "d4 Nf6 c4 e6 Nc3 Bb4 e3 d5 a3 Bxc3+ bxc3 c5 cxd5 exd5 Bd3 O-O Ne2 b6 O-O Ba6 Bxa6 Nxa6 Bb2 Qd7 a4 Rfe8 Qd3 c4 Qc2 Nb8 Rae1 Nc6 Ng3 Na5 f3 Nb3 e4 Qxa4 e5 Nd7 Qf2 g6 f4 f5 exf6 Nxf6 f5 Rxe1 Rxe1 Re8 Re6 Rxe6 fxe6 Kg7 Qf4 Qe8 Qe5 Qe7 Ba3 Qxa3 Nh5+ gxh5 Qg5+ Kf8 Qxf6+ Kg8 e7 Qc1+ Kf2 Qc2+ Kg3 Qd3+ Kh4 Qe4+ Kxh5 Qe2+ Kh4 Qe4+ g4 Qe1+ Kh5";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameLaskerVsAlanThomas1912() {
        String moves = "d4 e6 Nf3 f5 Nc3 Nf6 Bg5 Be7 Bxf6 Bxf6 e4 fxe4 Nxe4 b6 Ne5 O-O Bd3 Bb7 Qh5 Qe7 Qxh7+ Kxh7 Nxf6+ Kh6 Neg4+ Kg5 h4+ Kf4 g3+ Kf3 Be2+ Kg2 Rh2+ Kg1 Kd2#";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameKasparovVsTopalov1999() {
        String moves = "e4 d6 d4 Nf6 Nc3 g6 Be3 Bg7 Qd2 c6 f3 b5 Nge2 Nbd7 Bh6 Bxh6 Qxh6 Bb7 a3 e5 O-O-O Qe7 Kb1 a6 Nc1 O-O-O Nb3 exd4 Rxd4 c5 Rd1 Nb6 g3 Kb8 Na5 Ba8 Bh3 d5 Qf4+ Ka7 Rhe1 d4 Nd5 Nbxd5 exd5 Qd6 Rxd4 cxd4 Re7+ Kb6 Qxd4+ Kxa5 b4+ Ka4 Qc3 Qxd5 Ra7 Bb7 Rxb7 Qc4 Qxf6 Kxa3 Qxa6+ Kxb4 c3+ Kxc3 Qa1+ Kd2 Qb2+ Kd1 Bf1 Rd2 Rd7 Rxd7 Bxc4 bxc4 Qxh8 Rd3 Qa8 c3 Qa4+ Ke1 f4 f5 Kc1 Rd2 Qa7";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameCarlsenVsErnst2004() {
        String moves = "e4 c6 d4 d5 Nc3 dxe4 Nxe4 Bf5 Ng3 Bg6 h4 h6 Nf3 Nd7 h5 Bh7 Bd3 Bxd3 Qxd3 e6 Bf4 Ngf6 O-O-O Be7 Ne4 Qa5 Kb1 O-O Nxf6+ Nxf6 Ne5 Rad8 Qe2 c5 Ng6 fxg6 Qxe6+ Kh8 hxg6 Ng8 Bxh6 gxh6 Rxh6+ Nxh6 Qxe7 Nf7 gxf7 Kg7 Rd3 Rd6 Rg3+ Rg6 Qe5+ Kxf7 Qf5+ Rf6 Qd7#";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gamePolugaevskyVsNezhmetdinov1958() {
        String moves = "d4 Nf6 c4 d6 Nc3 e5 e4 exd4 Qxd4 Nc6 Qd2 g6 b3 Bg7 Bb2 O-O Bd3 Ng4 Nge2 Qh4 Ng3 Nge5 O-O f5 f3 Bh6 Qd1 f4 Nge2 g5 Nd5 g4 g3 fxg3 hxg3 Qh3 f4 Be6 Bc2 Rf7 Kf2 Qh2+ Ke3 Bxd5 cxd5 Nb4 Rh1 Rxf4 Rxh2 Rf3+ Kd4 Bg7 a4 c5+ dxc6 bxc6 Bd3 Nexd3+ Kc4 d5+ exd5 cxd5+ Kb5 Rb8+ Ka5 Nc6+";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameBardelebenVsSteinitz1895() {
        String moves = "e4 e5 Nf3 Nc6 Bc4 Bc5 c3 Nf6 d4 exd4 cxd4 Bb4+ Nc3 d5 exd5 Nxd5 O-O Be6 Bg5 Be7 Bxd5 Bxd5 Nxd5 Qxd5 Bxe7 Nxe7 Re1 f6 Qe2 Qd7 Rac1 c6 d5 cxd5 Nd4 Kf7 Ne6 Rhc8 Qg4 g6 Ng5+ Ke8 Rxe7+ Kf8 Rf7+ Kg8 Rg7+ Kh8 Rxh7+";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameRotlewiVsRubinstein1907() {
        String moves = "d4 d5 Nf3 e6 e3 c5 c4 Nc6 Nc3 Nf6 dxc5 Bxc5 a3 a6 b4 Bd6 Bb2 O-O Qd2 Qe7 Bd3 dxc4 Bxc4 b5 Bd3 Rd8 Qe2 Bb7 O-O Ne5 Nxe5 Bxe5 f4 Bc7 e4 Rac8 e5 Bb6+ Kh1 Ng4 Be4 Qh4 g3 Rxc3 gxh4 Rd2 Qxd2 Bxe4+ Qg2 Rh3";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameBogoljubovVsAlekhine1922() {
        String moves = "d4 f5 c4 Nf6 g3 e6 Bg2 Bb4+ Bd2 Bxd2+ Nxd2 Nc6 Ngf3 O-O O-O d6 Qb3 Kh8 Qc3 e5 e3 a5 b3 Qe8 a3 Qh5 h4 Ng4 Ng5 Bd7 f3 Nf6 f4 e4 Rfd1 h6 Nh3 d5 Nf1 Ne7 a4 Nc6 Rd2 Nb4 Bh1 Qe8 Rg2 dxc4 bxc4 Bxa4 Nf2 Bd7 Nd2 b5 Nd1 Nd3 Rxa5 b4 Rxa8 bxc3 Rxe8 c2 Rxf8+ Kh7 Nf2 c1=Q+ Nf1 Ne1 Rh2 Qxc4 Rb8 Bb5 Rxb5 Qxb5 g4 Nf3+ Bxf3 exf3 gxf5 Qe2 d5 Kg8 h5 Kh7 e4 Nxe4 Nxe4 Qxe4 d6 cxd6 f6 gxf6 Rd2 Qe2 Rxe2 fxe2 Kf2 exf1=Q+ Kxf1 Kg7 Ke2 Kf7 Ke3 Ke6 Ke4 d5+";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameKarpovVsKasparov1985() {
        String moves = "e4 c5 Nf3 e6 d4 cxd4 Nxd4 Nc6 Nb5 d6 c4 Nf6 N1c3 a6 Na3 d5 cxd5 exd5 exd5 Nb4 Be2 Bc5 O-O O-O Bf3 Bf5 Bg5 Re8 Qd2 b5 Rad1 Nd3 Nab1 h6 Bh4 b4 Na4 Bd6 Bg3 Rc8 b3 g5 Bxd6 Qxd6 g3 Nd7 Bg2 Qf6 a3 a5 axb4 axb4 Qa2 Bg6 d6 g4 Qd2 Kg7 f3 Qxd6 fxg4 Qd4+ Kh1 Nf6 Rf4 Ne4 Qxd3 Nf2+ Rxf2 Bxd3 Rfd2 Qe3 Rxd3 Rc1 Nb2 Qf2 Nd2 Rxd1+ Nxd1 Re1+";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }

    @Test
    public void gameLarsenVsSpassky1970() {
        String moves = "b3 e5 Bb2 Nc6 c4 Nf6 Nf3 e4 Nd4 Bc5 Nxc6 dxc6 e3 Bf5 Qc2 Qe7 Be2 O-O-O f4 Ng4 g3 h5 h3 h4 hxg4 hxg3 Rg1 Rh1 Rxh1 g2 Rf1 Qh4+ Kd1 gxf1=Q+";
        String[] allMoves = moves.split(" ");
        boolean whiteToMove = true;
        int i = 1;
        for (String move : allMoves) {
            MoveValidator.Move m = validator.fromSAN(chessboard, move, whiteToMove);

            assertNotNull("Move #" + i + ": " + move + " could not be parsed", m);
            assertTrue("Move #" + i + ": " + move + " should be legal", legal(move, whiteToMove));

            chessboard.makeMove(m);

            whiteToMove = !whiteToMove;
            i++;
        }
    }
}
