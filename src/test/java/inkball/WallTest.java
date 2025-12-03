package inkball;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import processing.core.PVector;
import java.util.ArrayList;

public class WallTest {
    private Wall wall;
    private Ball ball;

    @Before
    public void setUp() {
        wall = new Wall(2, 10, 10);
        ball = new Ball(1, true, 110, 110);
        ball.setVelocity(new PVector(2, 2));
    }

    @Test
    public void testWallInitialization() {
        ArrayList<PVector> points = wall.points;
        assertEquals(4, points.size(), 0.1);

        PVector p1 = new PVector(10 * App.CELLSIZE, (10 + 2) * App.CELLSIZE);
        PVector p2 = new PVector((10 + 1) * App.CELLSIZE, (10 + 2) * App.CELLSIZE);
        PVector p3 = new PVector((10 + 1) * App.CELLSIZE, (10 + 3) * App.CELLSIZE);
        PVector p4 = new PVector(10 * App.CELLSIZE, (10 + 3) * App.CELLSIZE);

        assertEquals(p1, points.get(0));
        assertEquals(p2, points.get(1));
        assertEquals(p3, points.get(2));
        assertEquals(p4, points.get(3));
    }

    @Test
    public void testHitDetectorCollision() {
        // ball should be reflected when hit the wall
        ball = new Ball(1, true, 10, 10);
        ball.setVelocity(new PVector(0, 2));

        wall.hitDetector(ball);

        PVector expectedVelocity = new PVector(0, -2);
        assertEquals(expectedVelocity.x, ball.getVelocity().x, 0.01);
        assertEquals(expectedVelocity.y, ball.getVelocity().y, 0.01);

        assertEquals(2, ball.getNumber());
    }

    @Test
    public void testNoCollision() {
        ball = new Ball(1, true, 110, 110);
        ball.setVelocity(new PVector(10, 10));

        PVector originalVelocity = ball.getVelocity().copy();
        wall.hitDetector(ball);

        assertEquals(originalVelocity, ball.getVelocity());// no collision happens
    }
}
