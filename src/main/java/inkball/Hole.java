package inkball;

import processing.core.PVector;

import java.util.ArrayList;

public class Hole extends Grid{
    /**
     * a specific number of the Hole, refer to the color of it
     */
    private int number;

    /**
     * Constructor of the Hole class, initialize the color, x, y coordinate
     * @param number the color of the Hole
     * @param x coordinate
     * @param y coordinate
     */
    Hole(int number, int x, int y) {
        this.number = number;
        this.x = x;
        this.y = y;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * get the color(number) of the Hole
     * @return the specific number (color)
     */
    public int getNumber() {
        return number;
    }

    /**
     * Get the center point of the Hole
     * @return the center of the hole
     */
    public PVector center(){
        return new PVector((x + 1) * App.CELLSIZE, (y + 3) * App.CELLSIZE);
    }

}

