package inkball;

import processing.data.JSONObject;

/**
 * This class manages the scoring system for the game, adjusting scores based on various events
 * such as capturing the correct hole or the wrong hole.
 */
public class Score {
    private float score;
    private final double ScoreIncreaseModifier;
    private final double ScoreDecreaseModifier;
    private JSONObject scoreIncreaseFromHoleCapture;
    private JSONObject scoreDecreaseFromWrongHole;

    /**
     * This is a constructor of Score, taking in score modifier and Json array for score discipline
     *
     * @param ScoreIncreaseModifier a modifier applied when increasing the score
     * @param ScoreDecreaseModifier a modifier applied when decreasing the score
     * @param scoreIncreaseFromHoleCapture a JSONObject containing the score increment values for different hole captures
     * @param scoreDecreaseFromWrongHole a JSONObject containing the score decrement values for wrong hole captures
     */
    public Score(double ScoreIncreaseModifier, double ScoreDecreaseModifier,
                 JSONObject scoreIncreaseFromHoleCapture, JSONObject scoreDecreaseFromWrongHole) {
        this.ScoreIncreaseModifier = ScoreIncreaseModifier;
        this.ScoreDecreaseModifier = ScoreDecreaseModifier;
        this.scoreIncreaseFromHoleCapture = scoreIncreaseFromHoleCapture;
        this.scoreDecreaseFromWrongHole = scoreDecreaseFromWrongHole;
    }

    /**
     * Adds points to the score based on the captured hole's color.
     *
     * @param number the number representing the hole color
     */
    public void addPoint(int number) {
        score += (float) (numberStringTranslator(number, scoreIncreaseFromHoleCapture) * ScoreIncreaseModifier);
    }

    /**
     * Subtracts points from the score when the wrong hole is captured.
     *
     * @param number the number representing the hole color
     */
    public void minusPoint(int number) {
        score -= (float) (numberStringTranslator(number, scoreDecreaseFromWrongHole) * ScoreDecreaseModifier);
    }

    /**
     * Translates the given number representing a hole color into a score adjustment value using the provided map.
     *
     * @param number the number representing the hole color
     * @param Map the {@code JSONObject} containing the score values for each color
     * @return the score adjustment value for the given hole color
     */
    private int numberStringTranslator(int number, JSONObject Map) {
        switch (number) {
            case 0:
                return Map.getInt("grey");
            case 1:
                return Map.getInt("orange");
            case 2:
                return Map.getInt("blue");
            case 3:
                return Map.getInt("green");
            case 4:
                return Map.getInt("yellow");
        }
        return number;
    }

    /**
     * Returns the current score.
     *
     * @return the current score
     */
    public float getScore() {
        return score;
    }

    /**
     * Increments the score by 1 point.
     */
    public void increment() {
        score = score + 1;
    }
}
