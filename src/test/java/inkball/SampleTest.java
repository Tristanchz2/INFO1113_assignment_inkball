package inkball;

import processing.core.PApplet;

import static java.awt.Event.LEFT;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * This test class just for launching the game and simulating basic action.
 * For more detail, it should be on the AppTest.java
 */
public class SampleTest {

    public void simulateJSON(App app){
        JSONObject gameData;
        gameData = new JSONObject();

        JSONArray levels = new JSONArray();

        // level1
        JSONObject level1 = new JSONObject();
        level1.setString("layout", "level1.txt");
        level1.setInt("time", 120);
        level1.setInt("spawn_interval", 10);
        level1.setFloat("score_increase_from_hole_capture_modifier", 1.0f);
        level1.setFloat("score_decrease_from_wrong_hole_modifier", 1.0f);

        JSONArray balls1 = new JSONArray();
        balls1.append("blue");
        balls1.append("orange");
        balls1.append("grey");
        balls1.append("blue");
        balls1.append("green");
        balls1.append("yellow");
        level1.setJSONArray("balls", balls1);

        levels.append(level1);

        // level2
        JSONObject level2 = new JSONObject();
        level2.setString("layout", "level2.txt");
        level2.setInt("time", 180);
        level2.setInt("spawn_interval", 6);
        level2.setFloat("score_increase_from_hole_capture_modifier", 1.2f);
        level2.setFloat("score_decrease_from_wrong_hole_modifier", 1.1f);

        JSONArray balls2 = new JSONArray();
        balls2.append("green");
        balls2.append("grey");
        balls2.append("grey");
        balls2.append("blue");
        balls2.append("yellow");
        balls2.append("orange");
        balls2.append("blue");
        balls2.append("grey");
        level2.setJSONArray("balls", balls2);

        levels.append(level2);

        // level3
        JSONObject level3 = new JSONObject();
        level3.setString("layout", "level3.txt");
        level3.setInt("time", 180);
        level3.setInt("spawn_interval", 5);
        level3.setFloat("score_increase_from_hole_capture_modifier", 1.3f);
        level3.setFloat("score_decrease_from_wrong_hole_modifier", 1.3f);

        JSONArray balls3 = new JSONArray();
        for (int i = 0; i < 8; i++) {
            balls3.append("grey");
        }
        level3.setJSONArray("balls", balls3);

        levels.append(level3);

        // add levels to gameData
        gameData.setJSONArray("levels", levels);

        // score_increase_from_hole_capture
        JSONObject scoreIncrease = new JSONObject();
        scoreIncrease.setInt("grey", 70);
        scoreIncrease.setInt("orange", 50);
        scoreIncrease.setInt("blue", 50);
        scoreIncrease.setInt("green", 50);
        scoreIncrease.setInt("yellow", 100);

        gameData.setJSONObject("score_increase_from_hole_capture", scoreIncrease);

        // score_decrease_from_wrong_hole
        JSONObject scoreDecrease = new JSONObject();
        scoreDecrease.setInt("grey", 0);
        scoreDecrease.setInt("orange", 25);
        scoreDecrease.setInt("blue", 25);
        scoreDecrease.setInt("green", 25);
        scoreDecrease.setInt("yellow", 100);

        gameData.setJSONObject("score_decrease_from_wrong_hole", scoreDecrease);

        app.config = gameData;

    }


    @Test
    public void simpleTest() {
        App app = new App();
        simulateJSON(app);
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(500);
        app.setup();
        app.delay(2500); // delay is to give time to initialise stuff before drawing begins
        app.mouseButton = LEFT;
        app.mouseX = 10;
        app.mouseY = 80;
        app.mousePressed();
        for (int i = 0; i < 100; i++) {
            MouseEvent pressEvent = new MouseEvent(app, System.currentTimeMillis(),
                    MouseEvent.PRESS, 0, 10 + i, 80 + i, LEFT, 1);
            app.mouseDragged(pressEvent);
            app.delay(5);
        }
        app.delay(500);

        //display different balls
        App.balls.get(6).setNumber(0);
        app.delay(100);
        App.balls.get(6).setNumber(1);
        app.delay(100);
        App.balls.get(6).setNumber(2);
        app.delay(100);
        App.balls.get(6).setNumber(3);
        app.delay(100);
        App.balls.get(6).setNumber(4);
        app.delay(100);

        //display different walls
        app.walls.get(0).setNumber(0);
        app.delay(100);
        app.walls.get(0).setNumber(1);
        app.delay(100);
        app.walls.get(0).setNumber(2);
        app.delay(100);
        app.walls.get(0).setNumber(3);
        app.delay(100);
        app.walls.get(0).setNumber(4);
        app.delay(100);

        //display different holes
        App.holes.get(0).setNumber(0);
        app.delay(100);
        App.holes.get(0).setNumber(1);
        app.delay(100);
        App.holes.get(0).setNumber(2);
        app.delay(100);
        App.holes.get(0).setNumber(3);
        app.delay(100);
        App.holes.get(0).setNumber(4);
        app.delay(100);

        //various status of the game
        app.gameWin = true;
        app.delay(500);
        app.gameWin = false;

        app.spacePressed = true;
        app.delay(500);
        app.spacePressed = false;

        app.gameLose = true;
        app.delay(500);
        app.gameLose = false;

        app.gameTimer = 50;
        for (Ball B : App.balls){
            B.setCatch();
        }
        app.delay(7000);

        // "r" pressed
        KeyEvent keyEvent = new KeyEvent(app, System.currentTimeMillis(), 0, KeyEvent.PRESS, 'r', 82);
        app.keyPressed(keyEvent);
    }
}

// gradle run						Run the program
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
