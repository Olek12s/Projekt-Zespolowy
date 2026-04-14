package com.example.demo.util;

public class EloRating
{
    private static int K = 30; // this variable tells, how faster ranking changes (higher = faster)


    private static double expectedScore(double ratingWhite, double ratingBlack) {
        return 1 / (1 + Math.pow(10, (ratingWhite-ratingBlack)/400));
    }

    /**
     *
     * @param ratingWhite White player rating
     * @param ratingBlack Black player rating
     * @param matchOutcome who won match: 1 for white, 0 for black, 0.5 for DRAW
     * @return Pair representing new updated ratings as: &lt; white, black &gt;
     */
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
        Pair<Double, Double> outcome1 = getOutcome(1200, 1000, MatchOutcome.WHITE_WIN);
        Pair<Double, Double> outcome2 = getOutcome(1200, 1000, MatchOutcome.BLACK_WIN);
        Pair<Double, Double> outcome3 = getOutcome(1200, 1000, MatchOutcome.DRAW);
        System.out.printf("1) White: %.2f Black: %.2f%n", outcome1.first(), outcome1.second());
        System.out.printf("2) White: %.2f Black: %.2f%n", outcome2.first(), outcome2.second());
        System.out.printf("3) White: %.2f Black: %.2f%n", outcome3.first(), outcome3.second());

        Pair<Double, Double> outcome4 = getOutcome(1000, 1000, MatchOutcome.WHITE_WIN);
        Pair<Double, Double> outcome5 = getOutcome(1000, 1000, MatchOutcome.BLACK_WIN);
        Pair<Double, Double> outcome6 = getOutcome(1000, 1000, MatchOutcome.DRAW);
        System.out.printf("4) White: %.2f Black: %.2f%n", outcome4.first(), outcome4.second());
        System.out.printf("5) White: %.2f Black: %.2f%n", outcome5.first(), outcome5.second());
        System.out.printf("6) White: %.2f Black: %.2f%n", outcome6.first(), outcome6.second());

        Pair<Double, Double> outcome7 = getOutcome(600, 1500, MatchOutcome.WHITE_WIN);
        Pair<Double, Double> outcome8 = getOutcome(200, 1500, MatchOutcome.BLACK_WIN);
        Pair<Double, Double> outcome9 = getOutcome(2800, 2750, MatchOutcome.DRAW);
        Pair<Double, Double> outcome10 = getOutcome(1250, 1200, MatchOutcome.DRAW);
        System.out.printf("7) White: %.2f Black: %.2f%n", outcome7.first(), outcome7.second());
        System.out.printf("8) White: %.2f Black: %.2f%n", outcome8.first(), outcome8.second());
        System.out.printf("9) White: %.2f Black: %.2f%n", outcome9.first(), outcome9.second());
        System.out.printf("10) White: %.2f Black: %.2f%n", outcome10.first(), outcome10.second());
    }
}
