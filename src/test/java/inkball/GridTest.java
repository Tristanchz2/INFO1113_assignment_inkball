package inkball;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.xml.ws.BindingType;

public class GridTest {
    private Grid grid1;
    private Grid grid2;

    @Before
    public void setUp() {
        grid1 = new Grid(1, 2);
        grid2 = new Grid(3, 4);
    }

    @Test
    public void testGetX() {
        assert grid1.getX() == 1;
        assert grid2.getX() == 3;
    }

    @Test
    public void testGetY() {
        assert grid1.getY() == 2;
        assert grid2.getY() == 4;
    }

}
