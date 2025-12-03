package inkball;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import processing.data.JSONObject;

public class ScoreTest {
    private Score score;
    private JSONObject increaseMap;
    private JSONObject decreaseMap;

    @Before
    public void setUp() {
        // create a fake jsonconfig file (test only)
        increaseMap = new JSONObject();
        increaseMap.setInt("grey", 10);
        increaseMap.setInt("orange", 20);
        increaseMap.setInt("blue", 30);
        increaseMap.setInt("green", 40);
        increaseMap.setInt("yellow", 50);

        decreaseMap = new JSONObject();
        decreaseMap.setInt("grey", 5);
        decreaseMap.setInt("orange", 10);
        decreaseMap.setInt("blue", 15);
        decreaseMap.setInt("green", 20);
        decreaseMap.setInt("yellow", 25);

        score = new Score(1.0, 1.0, increaseMap, decreaseMap);
    }

    @Test
    public void testAddPoint() {
        // points can be correctly added when different balls is caught
        score.addPoint(0);
        assertEquals(10.0f, score.getScore(), 0.1);

        score.addPoint(1);
        assertEquals(30.0f, score.getScore(), 0.1);
    }

    @Test
    public void testMinusPoint() {
        // points can be correctly added when different balls is caught
        score.addPoint(0); // grey +10
        assertEquals(10.0f, score.getScore(), 0.1);

        score.minusPoint(0);
        assertEquals(5.0f, score.getScore(), 0.1);

        score.minusPoint(1);
        assertEquals(-5.0f, score.getScore(), 0.1);
    }

    @Test
    public void testGetScore(){
        // can it return current score
        score.addPoint(1);
        assertEquals(score.getScore(), 20, 0.1);
    }

    @Test
    public void testIncrement() {
        // simply add one points
        score.increment();
        assertEquals(1.0f, score.getScore(), 0.1);

        score.increment();
        assertEquals(2.0f, score.getScore(), 0.1);
    }
}
