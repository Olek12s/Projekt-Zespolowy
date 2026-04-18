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
        assertTrue("e2e4 should be legal", legal("e2e4", true));  // white
        assertFalse("e2e4 should be illlegal", legal("e2e4", false));  // black
    }

    @Test
    public void shouldNotAllowPawnMoveTwoSquaresFromStart() {
        //assertFalse(legal("e2e7", true));
    }

    @Test
    public void shouldAllowPawnMoveOneSquareFromStart() {

    }

    @Test
    public void shouldNotAllowPawnMoveOneSquareFromStart() {

    }

    @Test
    public void shouldAllowPawnMoveOneDiagonalSquareFromStart() {

    }

    @Test
    public void shouldAllowPawnMoveTwoDiagonalSquaresFromStart() {

    }


    // ========== FROM PAWN ON MIDDLE LAYOUT, NOT BLOCKED AT ANY TEST ==========

    @Test
    public void shouldAllowPawnMoveTwoSquaresFromMiddle() {
        //assertTrue(legal("e2e4", true));  // white
        //assertTrue(legal("e2e4", false));  // black
    }

    @Test
    public void shouldNotAllowPawnMoveTwoSquaresFromMiddle() {
        //assertFalse(legal("e2e7", true));
    }

    @Test
    public void shouldAllowPawnMoveOneSquareFromMiddle() {

    }

    @Test
    public void shouldNotAllowPawnMoveOneSquareFromMiddle() {

    }

    @Test
    public void shouldAllowPawnMoveOneDiagonalSquareFromMiddle() {

    }

    @Test
    public void shouldAllowPawnMoveTwoDiagonalSquaresFromMiddle() {

    }


    // ========== FROM PAWN ON MIDDLE LAYOUT, BLOCKED AT EVERY TEST ==========

    @Test
    public void shouldAllowPawnMoveTwoSquaresFromMiddleBlocked() {
        //assertTrue(legal("e2e4", true));  // white
        //assertTrue(legal("e2e4", false));  // black
    }

    @Test
    public void shouldNotAllowPawnMoveTwoSquaresFromMiddleBlocked() {
        //assertFalse(legal("e2e7", true));
    }

    @Test
    public void shouldAllowPawnMoveOneSquareFromMiddleBlocked() {

    }

    @Test
    public void shouldNotAllowPawnMoveOneSquareFromMiddleBlocked() {

    }

    @Test
    public void shouldAllowPawnMoveOneDiagonalSquareFromMiddleBlocked() {

    }

    @Test
    public void shouldAllowPawnMoveTwoDiagonalSquaresFromMiddleBlocked() {

    }

    // ========== FROM PAWN TO OTHER PAWN SAME COLOR ON STARTING LAYOUT, BLOCKED AT EVERY TEST ==========

    @Test
    public void shouldAllowPawnMoveToLeftOntoOtherPawnFromStart() {

    }

    @Test
    public void shouldAllowPawnMoveToRightOntoOtherPawnFromStart() {

    }
}
