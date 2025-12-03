package inkball;

import processing.core.PVector;
import java.util.ArrayList;

public class Wall extends Grid implements Reflective {
    private int number;

    /**
     * an arraylist that contain 4 vertex of the wall
     */
    ArrayList<PVector> points;

    /**
     * This is a constructor with the specified number, x-coordinate, and y-coordinate.
     * Initializes the wall's points that represent its four corners on the grid.
     *
     * @param number the number assigned to this wall
     * @param x the x-coordinate of the wall's position
     * @param y the y-coordinate of the wall's position
     */
    public Wall(int number, int x, int y) {
        this.number = number;
        this.x = x;
        this.y = y;
        points = new ArrayList<>();
        points.add(new PVector(x * App.CELLSIZE, (y + 2) * App.CELLSIZE));
        points.add(new PVector((x + 1) * App.CELLSIZE, (y + 2) * App.CELLSIZE));
        points.add(new PVector((x + 1) * App.CELLSIZE, (y + 3) * App.CELLSIZE));
        points.add(new PVector(x * App.CELLSIZE, (y + 3) * App.CELLSIZE));
    }

    /**
     * Returns the number assigned to this wall.
     *
     * @return the wall's number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets the number assigned to this wall.
     *
     * @param number the new number to assign to this wall
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Detects if the given ball has collided with this wall. If a collision is detected,
     * the ball's velocity is reflected based on the wall's edges, and the ball's number is set
     * to the wall's number if it's non-zero.
     *
     * @param B the ball to check for a collision
     */
    @Override
    public void hitDetector(Ball B) {
        for (int i = 0; i < 4; i++) {
            PVector p1;
            PVector p2;
            if (i != 0) {
                p1 = this.points.get(i - 1);
                p2 = this.points.get(i);
            } else {
                p1 = this.points.get(0);
                p2 = this.points.get(3);
            }
            // Collision detection with the ball
            if (PVector.dist(p1, B.center().add(B.getVelocity())) + PVector.dist(p2, B.center().add(B.getVelocity())) < PVector.dist(p1, p2) + B.getRadius()) {
                Reflective.reflect(B, p1, p2);
                if (this.number != 0) {
                    B.setNumber(this.number);
                }
            }
        }
    }
}
