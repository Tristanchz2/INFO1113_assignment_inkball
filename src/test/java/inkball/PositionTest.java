package inkball;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class PositionTest {
    private ArrayList<Ball> balls;

    @Before
    public void setUp() {
        balls = new ArrayList<>();
        balls.add(new Ball(1, true, 1, 1));
        balls.add(new Ball(2, true, 2, 2));
        balls.add(new Ball(3,  true, 3, 3));
        balls.add(new Ball(4, true, 4, 4));
        balls.add(new Ball(5, true, 5, 5));
    }

    @Test
    public void testDefaultAction() {
        // can each enum constant reset to default
        Position.P0.defaultAction();
        assertEquals(Position.P0.getX(), Position.P0.xStart);
        assertEquals(Position.P0.getNumber(), -1);
    }

    @Test
    public void testSetAndGetNumber() {
        // can number be set and got
        Position.P0.setNumber(10);
        assertEquals(10, Position.P0.getNumber());
    }

    @Test
    public void testMove() {
        // each enum constant is movable
        int startX = Position.P0.getX();
        Position.P0.move();
        assertEquals(startX - 1, Position.P0.getX());
    }

    @Test
    public void testReset() {
        // can each enum constant reset to default
        Position.P0.setNumber(10);
        Position.P0.reset();
        assertEquals(Position.P0.xEnd, Position.P0.getX());
        assertEquals(-1, Position.P0.getNumber());
    }

    @Test
    public void testSetup() {
        // the initial setup works fine
        Position.setup(balls);

        assertEquals(1, Position.P0.getNumber());
        assertEquals(2, Position.P1.getNumber());
        assertEquals(3, Position.P2.getNumber());
        assertEquals(4, Position.P3.getNumber());
        assertEquals(5, Position.P4.getNumber());
    }

    @Test
    public void testOneFrame() {
        // test the action in one frame
        Position.setup(balls);

        Position.oneFrame();

        assertEquals(5, Position.P4.getNumber());
        assertEquals(4, Position.P3.getNumber());
        assertFalse(Position.P2.getX() < Position.P2.xStart);
    }

    @Test
    public void testSetSpawn() {
        // does it work good when a ball spawn
        Position.setup(balls);
        Position.setSpawn();
        assertTrue(Position.spawn);
        Position.oneFrame();
        assertNotEquals(Position.P0.getNumber(), -1);


    }
}
