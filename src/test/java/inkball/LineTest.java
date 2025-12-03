package inkball;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import processing.core.PApplet;
import processing.core.PVector;

public class LineTest {
    private Line line1;
    private Line line2;
    private Line line3;

    @Before
    public void setUp() {
        line1 = new Line();
        line2 = new Line();
        line3 = new Line();
    }

    @Test
    public void testConstructor() {
        // can line arr be initialized
        assertNotNull(line1);
        assertNotNull(line2);
        assertNotNull(line3);
    }

    @Test
    public void testAddPoint() {
        // test addPoint method
        line1.addPoint(new PVector(0,1));
        line1.addPoint(new PVector(2,3));
        assertEquals(line1.points.get(0).x, 0, 0);
        assertEquals(line1.points.get(0).y, 1, 0);
        assertEquals(line1.points.get(1).x, 2, 1);
        assertEquals(line1.points.get(1).y, 3, 1);

    }

    @Test
    public void testIsDisable() {
        // if isDisable set and return correctly
        assertFalse(line1.isDisable());
        assertFalse(line2.isDisable());
        line1.disable();
        line2.disable();
        assertTrue(line1.isDisable());
        assertTrue(line2.isDisable());
    }

    @Test
    public void testSize() {
        // can the size of points arr return
        assertEquals(line1.points.size(), 0);
        line1.addPoint(new PVector(0,1));
        line1.addPoint(new PVector(2,3));
        assertEquals(line1.points.size(), 2);
    }

    @Test
    public void testHitDetector() {
        // if it can interact with the ball
        Ball ball = new Ball(1, true, 1, 1);
        line1.addPoint(new PVector(0,1));
        line1.addPoint(new PVector(0,3));
        line1.hitDetector(ball);
        assertFalse(line1.isDisable());

        line2.addPoint(new PVector(32,96));
        line2.addPoint(new PVector(40,110));
        line2.hitDetector(ball);
        assertTrue(line2.isDisable());

    }
}
