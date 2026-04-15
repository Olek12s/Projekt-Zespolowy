package com.example.demo.util;

import com.example.demo.model.Game;
import com.example.demo.model.GameResult;

public enum MatchOutcome
{
    WHITE_WIN(1),
    BLACK_WIN(0),
    DRAW(0.5);

    private final double value;

    MatchOutcome(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static MatchOutcome resolveOutcome(GameResult result, Game game) {

        if (result.getWinner() == null) {
            return MatchOutcome.DRAW;
        }

        if (result.getWinner().getId().equals(game.getWhitePlayer().getId())) {
            return MatchOutcome.WHITE_WIN;
        }

        return MatchOutcome.BLACK_WIN;
    }
}
