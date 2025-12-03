package inkball;


import org.junit.Before;
import org.junit.Test;
import processing.core.PVector;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestHole {
    private Hole hole1;
    private Hole hole2;

    @Before
    public void setUp() {
        hole1 = new Hole(1, 3, 4);
        hole2 = new Hole(3, 2, 5);
    }

    @Test
    public void testGetNumber(){
        // return correct number
        assertEquals(hole1.getNumber(), 1);
        assertEquals(hole2.getNumber(), 3);
    }

    @Test
    public void testCenter(){
        //return correct center
        PVector center = hole1.center();
        assertEquals(center.x, 128, 0);
        assertEquals(center.y, 224, 0);
    }

}
