package inkball;

public class Grid {
    protected int x;
    protected int y;

    /**
     * Constructor of the Grid
     * @param x x-coordinate
     * @param y y-coordinate
     */
    Grid(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * blank constructor
     */
    Grid(){}

    /**
     * get x-coordinate
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * get y-coordinate
     * @return y
     */
    public int getY() {
        return y;
    }

}