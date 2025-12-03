package inkball;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;


public class BallTest {
    private Ball ball;
    private Ball ball1;
    private Ball ball2;
    private Ball ball3;

    @Before
    public void setUp() {
        ball = new Ball(1, false);
        ball1 = new Ball(1, false, 10);
        ball2 = new Ball(1, true, 10);
        ball3 = new Ball(1, false, 2, 2);
    }

    @Test
    public void testConstructor() {
        //make sure ball is initialized correctly
        assertNotEquals(ball, null);
        assertNotEquals(ball1, null);
        assertNotEquals(ball2, null);
        assertNotEquals(ball3, null);
    }

    @Test
    public void testSpawn() {
        ball1.spawn(2, 2);
        assertTrue(ball1.isSpawn());
        PVector expectedPosition = new PVector(2 * 32, (2 + 2) * 32);
        assertEquals(ball1.getPosition().x, expectedPosition.x, 0.1);
        assertEquals(ball1.getPosition().y, expectedPosition.y, 0.1);
        assertNotEquals(ball1.getVelocity(), null);
    }

    @Test
    public void testSetNumber() {
        // number is set correctly
        ball1.setNumber(1);
        ball2.setNumber(2);
        ball3.setNumber(3);
        assertEquals(ball1.getNumber(), 1);
        assertEquals(ball2.getNumber(), 2);
        assertEquals(ball3.getNumber(), 3);
    }

    @Test
    public void testMove() {
        // ball can move
        PVector initialPosition = ball3.getPosition().copy();
        ball3.move();
        PVector newPosition = ball3.getPosition();
        assertNotEquals(initialPosition, newPosition);
    }

    @Test
    public void testGetPosition() {
        // if position is return correctly
        ball1 = new Ball(1, true, 2, 2);
        PVector expectedPosition = new PVector(2 * 32, (2 + 2) * 32);
        assertEquals(ball1.getPosition().x, expectedPosition.x, 0.1);
        assertEquals(ball1.getPosition().y, expectedPosition.y, 0.1);
    }

    @Test
    public void testGetVelocity() {
        // if velocity is return correctly
        ball1 = new Ball(1, true, 2, 2);
        ball2 = new Ball(1, false, 10);
        assertNotEquals(ball1.getVelocity(), null);
        assertNull(ball2.getVelocity());
    }
    @Test
    public void testSetVelocity() {
        // velocity is correctly set
        PVector newVelocity = new PVector(5, 5);
        ball.setVelocity(newVelocity);
        assertEquals(newVelocity, ball.getVelocity());
    }

    @Test
    public void isEnable(){
        // can this boolean type return in different situation
        ball1 = new Ball(1, true, 2, 2);
        assertTrue(ball1.isEnable());
        ball1.setCatch();
        assertFalse(ball1.isEnable());
        ball2 = new Ball(1, false, 10, 2);
        assertFalse(ball2.isEnable());
    }

    @Test
    public void testSetCatch() {
        // can isCatch set correctly
        ball.setCatch();
        assertTrue(ball.isCatch());
    }

    @Test
    public void testCenter() {
        // if it can return center of the ball
        Ball ball = new Ball(1, true, 1, 1);
        PVector center = ball.center();
        assertEquals(44, center.x, 0.01);
        assertEquals(44 + 32 * 2, center.y, 0.01);
    }

    @Test
    public void testIsSpawn() {
        // if it can spawn by a spawner and position/velocity is set correctly
        ball1 = new Ball(1, true, 2, 2);
        ball2 = new Ball(1, false, 10, 2);
        assertTrue(ball1.isSpawn());
        assertFalse(ball2.isSpawn());
        ball2.spawn(2, 2);
        assertTrue(ball2.isSpawn());
    }

    @Test
    public void testIsCatch(){
        // can be caught by a hole
        ball1 = new Ball(1, true, 2, 2);
        assertFalse(ball1.isCatch());
        ball1.setCatch();
        assertTrue(ball1.isCatch());
    }

    @Test
    public void testGetSize(){
        // get the size correctly
        ball1 = new Ball(1, true, 2, 2);
        assertEquals(ball1.getSize(), 24, 0.1);
        ball1.setSize(11);
        assertEquals(ball1.getSize(), 11, 0.1);
    }

    @Test
    public void testSetSize(){
        // can size be set
        ball1 = new Ball(1, true, 2, 2);
        ball1.setSize(11);
        assertEquals(ball1.getSize(), 11, 0.1);
    }

    @Test
    public void testGetRadius(){
        // can return the radius of the ball
        ball1 = new Ball(1, true, 2, 2);
        assertEquals(ball1.getRadius(), 12, 0.1);
        ball1.setSize(5);
        assertEquals(ball1.getRadius(), 2.5f, 0.1);
    }

    @Test
    public void testSetSpawnIntervalANDReadyToEmit(){
        ball1 = new Ball(1, true, 2, 2);
        ball1.setSpawnInterval(1);
        float time = ball1.readyToEmit(1);
        assertEquals(time, 1, 0.1);
    }

    @Test
    public void testInteractWithHole(){
        ball1 = new Ball(1, true, 2, 2);
        Hole h1 = new Hole(1, 2, 2);
        ball1.interactWithHole(h1, 0);
        assertTrue(ball1.isCatch());
    }


}
