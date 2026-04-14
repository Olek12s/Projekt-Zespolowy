package com.example.demo.util;

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
}
