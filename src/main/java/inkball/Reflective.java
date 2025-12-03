package inkball;

import processing.core.PVector;

/**
 * This interface defines behavior for objects that can reflect balls in the game.
 * It contains a static method for reflecting a ball off a line segment and an abstract method to detect a hit.
 */
public interface Reflective {

    /**
     * Reflects the ball off a line segment defined by two points.
     * Calculates two possible normal vectors for the line and reflects the ball along the closer normal.
     *
     * @param B the ball to be reflected
     * @param p1 the first point of the line segment
     * @param p2 the second point of the line segment
     */
    static void reflect(Ball B, PVector p1, PVector p2) {
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;
        PVector N1 = new PVector(-dy, dx);
        PVector N2 = new PVector(dy, -dx);
        N1.normalize();
        N2.normalize();
        PVector midPoint = new PVector((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        PVector midN1 = midPoint.add(N1);
        PVector midN2 = midPoint.add(N2);
        if (PVector.dist(B.getPosition(), midN1) < PVector.dist(B.getPosition(), midN2)){
            B.setVelocity(B.getVelocity().sub(N1.mult(PVector.dot(B.getVelocity(), N1)).mult(2)));
        } else {
            B.setVelocity(B.getVelocity().sub(N2.mult(PVector.dot(B.getVelocity(), N2)).mult(2)));
        }
    }

    /**
     * Detects whether a ball hits the reflective surface or object.
     *
     * @param B the ball to check for collision
     */
    void hitDetector(Ball B);
}
