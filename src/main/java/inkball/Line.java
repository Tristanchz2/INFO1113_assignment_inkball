package inkball;

import processing.core.PVector;

import java.util.ArrayList;

public class Line implements Reflective {
    /**
     * A array contains all the point in this line
     */
    public ArrayList<PVector> points;

    /**
     * if the line is enabled
     */
    private boolean isEnable;

    /**
     * Constructor of the Line calss
     * initialize the points arr, set isEnable to true
     */
    public Line(){
        points = new ArrayList<>();
        isEnable = true;
    }

    /**
     * check if the line is disabled or not
     * @return false if enabled, false else
     */
    public boolean isDisable(){
        return !isEnable;
    }

    /**
     * disable the line
     */
    public void disable(){
        isEnable = false;
    }

    /**
     * add a point to points arr
     * @param point point that is going to be added
     */
    public void addPoint(PVector point){
        points.add(point);
    }

    /**
     * get the size of the points array
     * @return size of hte points array
     */
    public int size(){
        return points.size();
    }

    /**
     * detect if the ball is about to hit the line, if so, reflect it.
     * @param B ball need to detect
     */
    @Override
    public void hitDetector(Ball B) {
        for (int i = 1; i < this.size(); i++){
            PVector p1 = points.get(i-1);
            PVector p2 = points.get(i);

            if (PVector.dist(p1, B.center().add(B.getVelocity())) + PVector.dist(p2, B.center().add(B.getVelocity())) < PVector.dist(p1, p2) + B.getRadius()){
                Reflective.reflect(B, p1, p2);
                this.disable();
            }
        }
    }
}
