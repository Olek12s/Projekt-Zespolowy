package com.example.demo.util;

public class EloRating
{
    private static int K = 30; // this variable tells, how faster ranking changes (higher = faster)


    private static double expectedScore(double ratingWhite, double ratingBlack) {
        return 1 / (1 + Math.pow(10, (ratingWhite-ratingBlack)/400));
    }


    public static Pair<Double, Double> getOutcome(double ratingWhite, double ratingBlack, MatchOutcome matchOutcome) {
        // calc winning probabilities
        double expectedWhite = expectedScore(ratingBlack, ratingWhite);
        double expectedBlack = expectedScore(ratingWhite, ratingBlack);

        // new ratings
        Double ratingWhiteNew = ratingWhite + K * (matchOutcome.getValue() - expectedWhite);
        Double ratingBlackNew = ratingBlack + K * ((1 - matchOutcome.getValue()) - expectedBlack);

        return new Pair<>(ratingWhiteNew, ratingBlackNew);
    }

    // tests
    public static void main(String[] args) {

    }
}
