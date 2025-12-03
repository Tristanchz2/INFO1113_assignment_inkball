package inkball;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import processing.core.PVector;

public class ReflectiveTest {
    private Ball ball;

    @Before
    public void setUp() {
        ball = new Ball(1, true, 100, 100);
        ball.setVelocity(new PVector(3, 2));
    }

    @Test
    public void testReflectVerticalWall() {
        PVector p1 = new PVector(100, 50);
        PVector p2 = new PVector(100, 150);

        Reflective.reflect(ball, p1, p2);

        PVector expectedVelocity = new PVector(-3, 2);
        assertEquals(expectedVelocity.x, ball.getVelocity().x, 0.01);
        assertEquals(expectedVelocity.y, ball.getVelocity().y, 0.01);
    }
}
