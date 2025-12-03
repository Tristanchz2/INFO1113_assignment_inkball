package inkball;

import processing.core.PVector;

import java.util.ArrayList;

import static java.lang.Math.max;


public class Ball {
    private int number;
    private boolean isSpawn;
    private boolean isCatch;
    private PVector velocity;
    private PVector position;
    private float spawnInterval;
    private boolean readyToEmit;
    private float startTime;
    /**
     * if it needs to display on the top left bar
     */
    public boolean NeedAddToPosition;
    private float size;

    /**
     * This constructor used when it is added from Jsonconfig file.
     * @param number specific number(color)
     * @param isSpawn if it is spawn by spawner
     */
    public Ball(int number, boolean isSpawn) {
        this.number = number;
        this.isSpawn = isSpawn;
        this.isCatch = false;
        this.size = 24f;
        readyToEmit = false;
        NeedAddToPosition = true;
    }

    /**
     * This constructor used when ball goes in hole wrongly.
     * @param number specific number(color)
     * @param isSpawn if it is spawned by spawner
     * @param spawnInterval how long will it take to spawn from the last one
     */
    public Ball(int number, boolean isSpawn, int spawnInterval) {
        this.number = number;
        this.isSpawn = isSpawn;
        this.isCatch = false;
        this.size = 24f;
        readyToEmit = false;
        this.spawnInterval = spawnInterval;
        NeedAddToPosition = true;
    }

    /**
     * This constructor used when ball is spawned by a spawner
     * @param number specific number(color)
     * @param isSpawn if it is spawned
     * @param x x coordinate of the ball
     * @param y y coordinate of the ball
     */
    public Ball(int number, boolean isSpawn, int x, int y) {
        this.number = number;
        this.isSpawn = isSpawn;
        velocity = new PVector((Math.random() < 0.5) ? -2 : 2, (Math.random() < 0.5) ? -2 : 2);
        position = new PVector(x * App.CELLSIZE, (y + 2) * App.CELLSIZE);
        this.isCatch = false;
        this.size = 24f;
        readyToEmit = false;
        NeedAddToPosition = false;
    }

    /**
     * Called by a spawner, ball spawned with spawner's position
     * @param x coordinate of the spawner
     * @param y coordinate of the spawner
     */
    public void spawn(int x, int y){
        isSpawn = true;
        velocity = new PVector((Math.random() < 0.5) ? -2 : 2, (Math.random() < 0.5) ? -2 : 2);
        position = new PVector(x * App.CELLSIZE, (y + 2) * App.CELLSIZE);
    }

    /**
     * Change the color of the ball
     * @param number color that is going to change
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Called in every frame, move the ball by its velocity
     */
    public void move() {
        position.add(velocity);
    }

    /**
     * Get the current position of the ball. Usually used to draw the ball or hit-detection
     * @return position
     */
    public PVector getPosition() {
        return position;
    }

    /**
     * Get the velocity of the ball. Usually used to hit-detection
     * @return velocity of the ball
     */
    public PVector getVelocity() {
        return velocity;
    }

    /**
     * Change the velocity of the ball
     * @param velocity velocity that is going to change
     */
    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    /**
     * Get the status of the ball
     * if it's not spawned yet or has already caught, return false
     * @return current status
     */
    public boolean isEnable() {
        return !isCatch && isSpawn;
    }

    /**
     * call when the ball is caught by hole
     */
    public void setCatch(){
        isCatch = true;
    }

    /**
     * Get the number(color)
     * @return specific number(color)
     */
    public int getNumber() {
        return number;
    }

    /**
     * calculate the center of the ball
     * @return center of the ball
     */
    public PVector center(){
        return new PVector(position.x + size / 2, position.y + size / 2);
    }

    /**
     * Check if the ball is spawned
     * @return ture if it is spawned
     */
    public boolean isSpawn(){
        return isSpawn;
    }

    /**
     * Check if the ball is caught by hole
     * @return true if it is caught
     */
    public boolean isCatch() {
        return isCatch;
    }

    /**
     * Get the size of the ball (how many pixel when draw)
     * @return size of the ball
     */
    public float getSize(){
        return this.size;
    }

    /**
     * Set the ball size when approaching the hole
     * @param size size that is going to change
     */
    public void setSize(float size) {
        this.size = size;
    }

    /**
     * Get the radius of the ball
     * @return radius of the ball
     */
    public float getRadius() {
        return this.size / 2;
    }

    /**
     * Set spawnInterval (how long will it take to spawn from the last one)
     * @param spawnInterval time between two balls spawn
     */
    public void setSpawnInterval(float spawnInterval) {
        this.spawnInterval = spawnInterval;
    }

    /**
     * Call this when this is the next ball that going to be spawn
     * calculate the reaming spawn time
     * @param millis current time of the app executing (in millisecond)
     * @return reaming time
     */
    public float readyToEmit(int millis){
        if (!this.readyToEmit){
            this.readyToEmit = true;
            startTime = millis;
            return spawnInterval;
        }else {
            float elapsedTime = (float) ((millis - startTime) / 1000.0);
            float remainingTime = max(0, spawnInterval - elapsedTime);
            if (remainingTime == 0){
                Spawner.spawn(this);
            }
            return remainingTime;
        }

    }

    /**
     * Given that the ball is within the middle range of hole, check if the ball reach the centre of the hole
     * if so, return true
     * else, apply an attractive force to ball
     * @param H Hole
     * @param distance distance between hole and ball (need to be smaller than 32)
     * @return true if it's right above the hole, else false
     */
    public boolean interactWithHole(Hole H, float distance){
        if (distance < 8){
            this.setCatch();
            return true;
        }else{
            velocity.add(PVector.sub(H.center(), this.center()).normalize().mult(distance * 0.05f));
        }
        return false;
    }

    /**
     * detect if the ball is in the middle range of the hole(distance < 32)
     * @param holes Holes' list that contains all balls
     */
    public void holeDetector(ArrayList<Hole> holes){
        boolean isCatching = false;
        for (Hole H : holes){
            if (this.isEnable()) {
                float distance = PVector.dist(this.center(), H.center());
                if (distance <= 32){
                    isCatching = true;
                    this.setSize(24 * distance / 32);
                    boolean result = this.interactWithHole(H, distance);
                    if (result){
                        if (!(H.getNumber() == 0 || H.getNumber() == this.getNumber())) {
                            App.scores[App.currentLevel - 1].minusPoint(this.getNumber());
                            App.addBalls.add(new Ball(this.getNumber(), false, App.spawnInterval));
                        }else{
                            App.scores[App.currentLevel - 1].addPoint(this.getNumber());
                        }
                    }
                }
            }
        }
        if (!isCatching && isEnable()){
            this.setSize(24);
            this.setVelocity(this.velocity.normalize().mult((float) Math.sqrt(8)));
        }
    }


}