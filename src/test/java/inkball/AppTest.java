package inkball;

import org.junit.Before;
import org.junit.Test;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class AppTest {

    App app;

    @Before
    public void setUp(){
        app = new App();
        App.currentLevel = 0;
        loadLevel();
    }

    public void loadLevel(){
        // create own Jsonconfig file and load level
        JSONObject levelData = new JSONObject();
        levelData.setString("layout", "level1.txt");
        levelData.setInt("time", 120);
        levelData.setInt("spawn_interval", 10);
        levelData.setFloat("score_increase_from_hole_capture_modifier", 1.0f);
        levelData.setFloat("score_decrease_from_wrong_hole_modifier", 1.0f);

        JSONArray ballArr = new JSONArray();
        ballArr.append("blue");
        ballArr.append("orange");
        ballArr.append("grey");
        ballArr.append("blue");
        ballArr.append("green");
        ballArr.append("yellow");

        levelData.setJSONArray("balls", ballArr);

        JSONObject scoreIncrease = new JSONObject();
        scoreIncrease.setInt("grey", 70);
        scoreIncrease.setInt("orange", 50);
        scoreIncrease.setInt("blue", 50);
        scoreIncrease.setInt("green", 50);
        scoreIncrease.setInt("yellow", 100);

        JSONObject scoreDecrease = new JSONObject();
        scoreDecrease.setInt("grey", 0);
        scoreDecrease.setInt("orange", 25);
        scoreDecrease.setInt("blue", 25);
        scoreDecrease.setInt("green", 25);
        scoreDecrease.setInt("yellow", 100);

        JSONArray levelsArray = new JSONArray();
        levelsArray.append(levelData);
        app.levels = levelsArray;
        App.scores = new Score[1];

        app.loadLevel();
    }

    public void simulateJSON(){
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
    public void testSetUp(){
        simulateJSON();
        app.wholeGameSetup();
        assertEquals(0, App.currentLevel);
        assertNotNull(app.levels);
        assertNotNull(App.addBalls);
        assertNotNull(App.scores);
        assertNotNull(app.scoreDecreaseFromWrongHole);
        assertNotNull(app.scoreIncreaseFromHoleCapture);
        assertFalse(app.spacePressed);
        assertFalse(app.ctrlPressed);

    }

    @Test
    public void testLoadLevel() {
        // create own Josnconfig file
        // assert part
        assertEquals(7, App.balls.size()); // verify balls size
        assertEquals(2, App.balls.get(0).getNumber()); // verify the color of the first ball
        assertEquals(1.0f, app.ScoreIncreaseModifier, 0.01); // verify score modifier system
        assertEquals(120, app.time); // check game time
        assertEquals(10, App.spawnInterval); // check spawn interval
        assertEquals(82, app.walls.size()); // check walls size
        assertEquals(1, App.spawners.size()); // check spawners size
        assertEquals(4, App.holes.size()); // check hole
    }

    @Test
    public void testGetScore() {
        //create a list contain few Score instance
        Score[] scores = new Score[2];
        scores[0] = new Score(1, 1, null, null);
        scores[1] = new Score(2, 1, null, null);
        App.scores = scores;
        assertEquals(0, app.getScore());
        // 1 point for level2
        scores[1].increment();
        assertEquals(1, app.getScore());
        scores[0].increment();
        // 1 point for level1
        assertEquals(2, app.getScore());
    }

    @Test
    public void testLevelWinProcedure(){
        app.gameTimer = 0;
        app.levelWinProcedure();
        // level win should be false, game win should be true
        assertFalse(app.levelWin);
        assertTrue(app.gameWin);
    }

    @Test
    public void testLevelWinCheck(){
        App.balls = new ArrayList<>();
        App.balls.add(new Ball(1, true, 1, 2));
        App.balls.add(new Ball(2, true, 1, 2));
        assertFalse(app.levelWin); // level win should be false without detection
        // catch the ball
        for (Ball B : App.balls){
            B.setCatch();
        }
        app.levelWinCheck();
        assertTrue(app.levelWin); // all ball is caught, level win
        assertNotNull(app.traversalWalls); // if the arr is set up correctly
    }

    @Test
    public void testDecTime(){
        app.gameTimer = 10;
        app.Timer = 0;
        app.decTime();
        assertEquals(1, app.Timer);
        assertEquals(10, app.gameTimer);
        // run the decTime for 30 times make 1s
        for (int i = 0; i < 29 ; i++){
            app.decTime();
        }
        assertEquals(9, app.gameTimer); // every 30 frame, gameTimer - 1
        // test the if-statement
        app.gameTimer = 0;
        app.Timer = 0;
        app.gameWin = false;
        app.decTime();
        assertFalse(app.spacePressed);
        assertTrue(app.gameLose);
    }

    @Test
    public void testSpawnTimeCalculator(){
        App.balls = new ArrayList<>();
        // initialize ball with 10s spawn interval
        App.balls.add(new Ball(1, false, 10));
        assertEquals(10, app.spawnTimeCalculator(), 0.1);
    }

    @Test
    public void testHitDetectorWALL(){
        App.balls = new ArrayList<>();
        App.balls.add(new Ball(1, true, 1, 1));
        app.move();
        app.walls = new ArrayList<>();
        app.walls.add(new Wall(2, 1, 1));

        PVector initialVelocity = App.balls.get(0).getVelocity().copy();
        app.hitDetector();
        PVector newVelocity = App.balls.get(0).getVelocity();
        // compare x and y coordinate, we just need one difference
        boolean notEqual = (initialVelocity.x != newVelocity.x) || (initialVelocity.y != newVelocity.y);
        assertTrue("Vectors should not be equal", notEqual);
    }

    @Test
    public void testHitDetectorEDGE(){
        App.balls = new ArrayList<>();
        App.balls.add(new Ball(1, true, 0, 1));
        PVector initialVelocity = new PVector(-2, 0);
        App.balls.get(0).setVelocity(initialVelocity);
        app.move();
        app.walls = new ArrayList<>();

        app.hitDetector();
        PVector newVelocity = App.balls.get(0).getVelocity();
        // compare x and y coordinate, we just need one difference
        boolean notEqual = (initialVelocity.x != newVelocity.x) || (initialVelocity.y != newVelocity.y);
        assertTrue("Vectors should not be equal", notEqual);
    }

    @Test
    public void testHitDetectorLINE(){
        App.balls = new ArrayList<>();
        App.balls.add(new Ball(1, true, 0, 1));
        PVector initialVelocity = new PVector(-2, 0);
        App.balls.get(0).setVelocity(initialVelocity);
        app.lines.add(new Line());
        app.lines.get(0).addPoint(new PVector(0, 64));
        app.lines.get(0).addPoint(new PVector(0, 200));
        app.move();
        app.walls = new ArrayList<>();

        app.hitDetector();
        PVector newVelocity = App.balls.get(0).getVelocity();
        // compare x and y coordinate, we just need one difference
        boolean notEqual = (initialVelocity.x != newVelocity.x) || (initialVelocity.y != newVelocity.y);
        assertTrue("Vectors should not be equal", notEqual);
    }



    @Test
    public void testMove(){
        App.balls = new ArrayList<>();
        App.balls.add(new Ball(1, false, 10));
        App.balls.add(new Ball(2, true, 1, 2));
        App.balls.add(new Ball(3, true, 1, 2));
        PVector ball0Position = App.balls.get(0).getPosition();
        float ball1PositionX = App.balls.get(1).getPosition().x;
        float ball1PositionY = App.balls.get(1).getPosition().y;
        float ball2PositionX = App.balls.get(2).getPosition().x;
        float ball2PositionY = App.balls.get(2).getPosition().y;
        app.move();
        // ball0 is not spawn, therefore it doesn't move
        assertEquals(ball0Position, App.balls.get(0).getPosition());
        // ball1 and ball2 should move
        assertNotEquals(ball1PositionX, App.balls.get(1).getPosition().x, 0.1);
        assertNotEquals(ball1PositionY, App.balls.get(1).getPosition().y, 0.1);
        assertNotEquals(ball2PositionX, App.balls.get(2).getPosition().x, 0.1);
        assertNotEquals(ball2PositionY, App.balls.get(2).getPosition().y, 0.1);
    }

    @Test
    public void testHoleDetector(){
        simulateJSON();
        app.wholeGameSetup();
        app.loadLevel();
        App.currentLevel = 1;
        App.holes = new ArrayList<>();
        App.holes.add(new Hole(1, 1, 1));
        Ball ball1 = new Ball(1, true, 1, 1);
        ball1.setVelocity(new PVector(2, 2));
        for (int i = 0; i < 10; i++){
            ball1.holeDetector(App.holes);
            if (ball1.isEnable()) {
                ball1.move();
            }
        }
        assertTrue(ball1.isCatch());
        assertNotEquals(0, app.getScore());
    }
}
