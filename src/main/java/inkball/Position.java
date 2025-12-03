package inkball;

import java.util.ArrayList;

/**
 * Enum representing different positions for balls, with coordinates and numbers.
 */
public enum Position {
    P0(18, 23),
    P1(48, 23),
    P2(78, 23),
    P3(108, 23),
    P4(138, 23);

    public final int xStart;
    public final int xEnd;
    public final int y;
    public int x;
    public int number;
    public static boolean spawn = false;

    /**
     * Constructor for Position.
     *
     * @param x the starting x coordinate
     * @param y the y coordinate
     */
    Position(int x, int y) {
        this.xStart = x;
        this.xEnd = x + 29;
        this.y = y;
    }

    /**
     * Resets the position to its default state.
     * Sets x to xStart and number to -1.
     */
    public void defaultAction() {
        this.x = xStart;
        this.number = -1;
    }

    /**
     * Gets the x coordinate of the position.
     *
     * @return the current x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the position.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the spawn flag to true.
     */
    public static void setSpawn() {
        spawn = true;
    }

    /**
     * Sets up the positions for a list of balls.
     * Assigns the first five balls to the respective positions and resets all others.
     *
     * @param balls the list of balls to set up
     */
    public static void setup(ArrayList<Ball> balls) {
        for (Position P : Position.values()) {
            P.defaultAction();
        }
        for (int i = 0; i < balls.size() && i < 5; i++) {
            balls.get(i).NeedAddToPosition = false;
            switch (i) {
                case 0:
                    P0.setNumber(balls.get(i).getNumber());
                    break;
                case 1:
                    P1.setNumber(balls.get(i).getNumber());
                    break;
                case 2:
                    P2.setNumber(balls.get(i).getNumber());
                    break;
                case 3:
                    P3.setNumber(balls.get(i).getNumber());
                    break;
                case 4:
                    P4.setNumber(balls.get(i).getNumber());
            }
        }
    }

    /**
     * Sets the number associated with the position.
     *
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Gets the number associated with the position.
     *
     * @return the current number associated with the position
     */
    public int getNumber() {
        return number;
    }

    /**
     * Moves the position by decrementing the x coordinate.
     */
    public void move() {
        x--;
    }

    /**
     * Resets the position by setting the x coordinate to xEnd and the number to -1.
     */
    public void reset() {
        this.x = xEnd;
        this.number = -1;
    }

    /**
     * Performs one frame of updates for all positions.
     * If the last position is empty, assigns a ball to it.
     * If spawning is triggered, resets the first position.
     */
    public static void oneFrame() {
        if (P4.getNumber() == -1) {
            for (Ball B : App.balls) {
                if (B.NeedAddToPosition) {
                    B.NeedAddToPosition = false;
                    P4.setNumber(B.getNumber());
                    break;
                }
            }
        }

        if (spawn) {
            P0.reset();
            spawn = false;
        }

        for (int i = 0; i < Position.values().length; i++) {
            if (Position.values()[i].getNumber() == -1) {
                continue;
            }

            if (Position.values()[i].getX() == Position.values()[i].xStart) {
                if (i != 0 && Position.values()[i - 1].getNumber() == -1) {
                    Position.values()[i - 1].setNumber(Position.values()[i].getNumber());
                    Position.values()[i].reset();
                }
            } else {
                Position.values()[i].move();
            }
        }
    }
}
