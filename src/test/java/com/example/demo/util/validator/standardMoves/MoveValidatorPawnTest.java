package com.example.demo.util.validator.standardMoves;



import com.example.demo.util.MoveValidator;
import com.example.demo.util.validator.MoveValidatorTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MoveValidatorPawnTest extends MoveValidatorTester {
    @Test
    public void shouldAllowPawnMoveE2E4FromStart() {
        assertTrue(legal("e2e4", true));
    }

    @Test
    public void shouldNotAllowPawnMoveE2E7FromStart() {
        assertFalse(legal("e2e7", true));
    }
}
