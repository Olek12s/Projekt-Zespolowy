package com.example.demo.util.validator;

import com.example.demo.util.MoveValidator;
import org.junit.jupiter.api.BeforeEach;


public abstract class MoveValidatorTester {
    protected MoveValidator validator;
    protected MoveValidator.Chessboard chessboard;

    @BeforeEach
    public void init() {
        validator = new MoveValidator();
        chessboard = validator.new Chessboard();
    }

    protected boolean legal(String move, boolean white) {
        return validator.isMoveLegal(chessboard, move, white);
    }
}