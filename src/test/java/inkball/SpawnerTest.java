package inkball;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;

public class SpawnerTest {

    private Spawner spawner;
    private Ball ball;

    @Before
    public void setUp() {
        spawner = new Spawner(3, 3);
        ball = new Ball(1, true, 3, 5);
    }

    @Test
    public void testEmit() {
        Ball ball = new Ball(1, false);
        spawner.emit(ball);
        PVector position = ball.getPosition();
        // ball should have the exact same position as the spawner does
        assertEquals(position.x, spawner.getX() * 32, 0.1);
        assertEquals(position.y, (spawner.getY() + 2) * 32, 0.1);
    }

    @Test
    public void testSpawn() {
        // if the status of the ball is set correctly
        App.spawners = new ArrayList<>();
        App.spawners.add(spawner);
        Spawner.spawn(ball);
        assertTrue(ball.isSpawn());
    }

}