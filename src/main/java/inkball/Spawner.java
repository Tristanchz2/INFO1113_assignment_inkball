package inkball;

import java.util.Random;

public class Spawner extends Grid{

    /**
     * This is a constructor that initialize a spawner with its x, y coordinate
     *
     * @param x the x-coordinate of the spawner
     * @param y the y-coordinate of the spawner
     */
    public Spawner(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Spawn the ball
     * @param B ball that is going to be spawned
     */
    public void emit(Ball B){
        B.spawn(x, y);
    }


    /**
     * chose a spawner from the spawners list randomly and then spawn tha ball
     * @param B ball that is going to be spawned
     */
    public static void spawn(Ball B) {
        Random rand = new Random();
        Position.setSpawn();
        int randomIndex = rand.nextInt(App.spawners.size());
        Spawner s = App.spawners.get(randomIndex);
        s.emit(B);
    }





}