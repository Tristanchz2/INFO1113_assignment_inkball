package inkball;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.core.PImage;

import java.util.*;
import java.io.*;

public class App extends PApplet {
    /**
     * size for a single grid
     */
    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 64;
    public static int WIDTH = 576; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int INITIAL_PARACHUTES = 1;

    public static final int FPS = 30;

    /**
     * images that are going to use in this game
     */
    PImage ball0, ball1, ball2, ball3, ball4, entrypoint, hole0, hole1, hole2, hole3, hole4, inkaball_spritesheet, tile, wall0, wall1, wall2, wall3, wall4;

    // time & score
    /**
     * maximum time for this level
     */
    int time;
    /**
     * time between two balls spawn
     */
    public static int spawnInterval;
    /**
     * real point will be ball's point * modifier
     */
    double ScoreIncreaseModifier;
    /**
     * real point will be ball's point * modifier
     */
    double ScoreDecreaseModifier;
    /**
     * arraylist that contains all the Ball objects
     */
    public static ArrayList<Ball> balls;
    /**
     * Ball that is going to add in the game
     * this list usually modified by Hole object
     */
    public static ArrayList<Ball> addBalls;
    /**
     * arraylist that contains all the Spawner objects
     */
    public static ArrayList<Spawner> spawners;
    /**
     * arraylist that contains all the Hole objects
     */
    public static ArrayList<Hole> holes;
    /**
     * arraylist that contains all the Line objects
     */
    ArrayList<Line> lines = new ArrayList<>();
    /**
     * arraylist that contains all the Wall objects
     */
    ArrayList<Wall> walls = new ArrayList<>();

    /**
     *  array of Score object.
     */
    public static Score[] scores;

    // JSONconfig
    public static int currentLevel;
    /**
     * layout for the game board, usually a .txt file with specific format
     */
    String layout;
    JSONObject config;
    JSONArray levels;
    JSONObject scoreIncreaseFromHoleCapture;
    JSONObject scoreDecreaseFromWrongHole;
    public String configPath;
    File levelFile;

    Line line;

    //user action
    boolean spacePressed;
    boolean ctrlPressed;
    boolean isDrawing;
    boolean isCancelling;

    int gameTimer;
    float spawnTimer;
    int Timer;
    boolean gameWin;
    boolean gameLose;

    //animation
    int counter;
    ArrayList<int[]> traversalWalls;
    int index;

    float remainingTime;

    public static Random random = new Random();

    boolean levelWin;
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);
		//See PApplet javadoc:
        // the image is loaded from relative path: "src/main/resources/inkball/..."
        ball0 = this.loadImage(this.getClass().getResource("ball0.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        ball1 = this.loadImage(this.getClass().getResource("ball1.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        ball2 = this.loadImage(this.getClass().getResource("ball2.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        ball3 = this.loadImage(this.getClass().getResource("ball3.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        ball4 = this.loadImage(this.getClass().getResource("ball4.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        entrypoint = this.loadImage(this.getClass().getResource("entrypoint.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        hole0 = this.loadImage(this.getClass().getResource("hole0.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        hole1 = this.loadImage(this.getClass().getResource("hole1.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        hole2 = this.loadImage(this.getClass().getResource("hole2.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        hole3 = this.loadImage(this.getClass().getResource("hole3.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        hole4 = this.loadImage(this.getClass().getResource("hole4.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        inkaball_spritesheet = this.loadImage(this.getClass().getResource("inkball_spritesheet.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        tile = this.loadImage(this.getClass().getResource("tile.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        wall0 = this.loadImage(this.getClass().getResource("wall0.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        wall1 = this.loadImage(this.getClass().getResource("wall1.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        wall2 = this.loadImage(this.getClass().getResource("wall2.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        wall3 = this.loadImage(this.getClass().getResource("wall3.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        wall4 = this.loadImage(this.getClass().getResource("wall4.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        config = loadJSONObject(this.configPath);
        wholeGameSetup();
        loadLevel();

    }

    public void wholeGameSetup(){
        currentLevel = 0;
        levels = config.getJSONArray("levels");
        addBalls = new ArrayList<>();
        scores = new Score[levels.size()];
        scoreIncreaseFromHoleCapture = config.getJSONObject("score_increase_from_hole_capture");
        scoreDecreaseFromWrongHole = config.getJSONObject("score_decrease_from_wrong_hole");

        //user action
        spacePressed = false;
        ctrlPressed = false;



    }

    /**
     * call at the start of each level. Initialize all the level-changing elements
     */
    public void loadLevel(){

        currentLevel++;
        balls = new ArrayList<>();
        lines = new ArrayList<>();
        spawners = new ArrayList<>();
        holes = new ArrayList<>();
        walls = new ArrayList<>();
        JSONObject level = levels.getJSONObject(currentLevel - 1);
        layout = level.getString("layout");
        time = level.getInt("time");
        spawnInterval = level.getInt("spawn_interval");
        ScoreIncreaseModifier = level.getDouble("score_increase_from_hole_capture_modifier");
        ScoreDecreaseModifier = level.getDouble("score_decrease_from_wrong_hole_modifier");
        scores[currentLevel - 1] = new Score(ScoreIncreaseModifier, ScoreDecreaseModifier,
                scoreIncreaseFromHoleCapture ,scoreDecreaseFromWrongHole);
        JSONArray ballArr = level.getJSONArray("balls");

        levelWin = false;

        // full in balls arr
        for (int i = 0; i < ballArr.size(); i++){
            switch (ballArr.getString(i)){
                case "grey":
                    balls.add(new Ball(0, false));
                    break;
                case "orange":
                    balls.add(new Ball(1, false));
                    break;
                case "blue":
                    balls.add(new Ball(2, false));
                    break;
                case "green":
                    balls.add(new Ball(3, false));
                    break;
                case "yellow":
                    balls.add(new Ball(4, false));
                    break;
            }
        }
        for (Ball B : balls){
            B.setSpawnInterval(spawnInterval);
        }

        // read the file
        levelFile = new File(layout);
        try{
            Scanner scan = new Scanner(levelFile);
            for (int i = 0; i < 18; i++){
                String[] temp = scan.nextLine().split("");
                List<String> list = Arrays.asList(temp);
                Iterator<String> iterator = list.iterator();
                int j = 0;
                while (iterator.hasNext()){
                    switch (iterator.next()){
                        case "X":
                            walls.add(new Wall(0, j, i));
                            break;
                        case "1":
                            walls.add(new Wall(1, j, i));
                            break;
                        case "2":
                            walls.add(new Wall(2, j, i));
                            break;
                        case "3":
                            walls.add(new Wall(3, j, i));
                            break;
                        case "4":
                            walls.add(new Wall(4, j, i));
                            break;
                        case "S":
                            Spawner s = new Spawner(j, i);
                            spawners.add(s);
                            break;
                        case "H":
                            holes.add(new Hole(Integer.parseInt(iterator.next()), j, i));
                            j++;
                            break;
                        case "B":
                            balls.add(new Ball(Integer.parseInt(iterator.next()), true, j, i));
                            j++;
                            break;
                    }
                    j++;
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        // timer
        Timer = 0;
        spawnTimer = spawnInterval;
        gameTimer = time;

        //ball display
        Position.setup(balls);
        gameLose = false;
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        if (event.getKeyCode() == CONTROL){
            ctrlPressed = true;
        }
        if (event.getKey() == 'r' || event.getKey() == 'R'){
            if (gameWin){
                currentLevel = 0;
                loadLevel();
                gameWin = false;
                gameLose = false;
                spacePressed = false;
                scores = new Score[levels.size()];
            }else{
                currentLevel--;
                loadLevel();
            }
        }

        if (event.getKey() == ' ' && !gameLose){
            spacePressed = !spacePressed;
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
        ctrlPressed = false;

    }

    /**
     * detect mouse pressed action
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        boolean ctrlPressed = e.isControlDown();

        if (button == LEFT && !ctrlPressed) {
            line = new Line();
            lines.add(line);
            isDrawing = true;
        } else if (button == RIGHT) {
            isCancelling = true;
        } else if (button == LEFT && ctrlPressed) {
            isCancelling = true;
        }
    }

    /**
     * Handles mouse drag events to modify the player-drawn line.
     * @param e The MouseEvent triggered by the dragging of the mouse.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        // add line segments to player-drawn line object if left mouse button is held
		if (isDrawing) {
            line.addPoint(new PVector(mouseX, mouseY));
        }
		// remove player-drawn line object if right mouse button is held 
		// and mouse position collides with the line
        else if (isCancelling) {
            for (Line line : lines){
                for (int i = 0; i < line.size(); i++){
                    PVector point = line.points.get(i);
                    double threshold = 5.0;
                    if (PVector.dist(point, new PVector(mouseX, mouseY)) < threshold) {
                        line.disable();
                        break;
                    }
                }
            }
        }
    }

    /**
     * Handles mouse release events to stop any active drawing or cancelling actions.
     * @param e The MouseEvent triggered by the release of the mouse button.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        isDrawing = false;
        isCancelling = false;
    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        background(255);
        //---------------------------------
        //draw elements
        //---------------------------------

        drawBackground();
        drawElements();
        drawBalls();
        if (!gameLose){
            drawLines();
        }
        hitDetector();

        if (!levelWin && !spacePressed && !gameLose && !gameWin) {
            decTime();
        }
        if (!spacePressed && !gameLose){
            remainingTime = spawnTimeCalculator();
            move();
        }
        catchDetector();
        addBalls();
        ballsDisplay();
        //----------------------------------
        //display score and time
        //----------------------------------
        //TODO
        this.fill(0);
        this.textSize(28.0F);
        this.textAlign(LEFT);
        this.text("Time: " + gameTimer, 420.0F, 60.0F);
        this.text("Score: " + getScore(), 420.0F, 30.0F);
        this.text(String.format("%.1f", Math.round(remainingTime * 10) / 10.0f), 177.0F, 45.0F);

		//----------------------------------
        //----------------------------------
		//display game end message
        if (spacePressed){
            this.textAlign(LEFT);
            this.textSize(28.0F);
            this.text("***PAUSED***", 225.0F, 55.0F);
        }
        if(gameLose){
            this.textAlign(LEFT);
            this.textSize(23.0F);
            this.text("===TIME’S UP=== ", 223.0F, 55.0F);
        }

        if (gameWin){
            this.textAlign(LEFT);
            this.textSize(28.0F);
            this.text("===ENDED===", 225.0F, 55.0F);
        } else if (!levelWin){
            levelWinCheck();
        }
        else{
            levelWinProcedure();
        }


    }

    /**
     * add balls from addBalls to balls(current balls' list)
     */
    private void addBalls() {
        balls.addAll(addBalls);
        addBalls = new ArrayList<>();
    }

    /**
     * get the current score in this frame
     * the total score should be the sum of all previous levels
     * @return total score
     */
    public int getScore(){
        float total = 0;
        for (Score s : scores){
            if (s != null){
                total += s.getScore();
            }
        }
        return (int) total;
    }

    /**
     * Executes the procedure when a level is won.
     * This method checks if the `gameTimer` has reached 0. If so, it resets the
     * `levelWin` flag and either ends the game (if the current level is the last one)
     * by setting `gameWin` to true, or loads the next level.
     * If the timer has not yet reached 0, it displays a moving wall effect on the screen
     * by rendering an image of `wall4` at positions based on the `traversalWalls` array.
     * The positions change based on the current value of `index` and are updated
     * periodically as the `counter` increments. Every second counter tick reduces
     * the `gameTimer` and increments the score for the current level.
     */
    public void levelWinProcedure(){
        if (gameTimer == 0){
            levelWin = false;
            if (currentLevel == levels.size()){
                gameWin = true;
            }else{
                loadLevel();
            }
            return;
        }
        this.image(wall4, traversalWalls.get(index % traversalWalls.size())[0] * CELLSIZE,
                    (traversalWalls.get(index % traversalWalls.size())[1] + 2) * CELLSIZE);
        this.image(wall4, traversalWalls.get((index + 35) % traversalWalls.size())[0] * CELLSIZE,
                    (traversalWalls.get((index + 35) % traversalWalls.size())[1] + 2) * CELLSIZE);
        if (counter % 2 == 0) {
            gameTimer--;
            scores[currentLevel - 1].increment();
            index++;
        }
        counter++;
    }

    public void levelWinCheck() {
        for (Ball B : balls){
            if (!B.isCatch()){
                return;
            }
        }
        levelWin = true;
        //setup
        counter = 0;
        index = 0;
        ArrayList<int[]> up = new ArrayList<>();
        ArrayList<int[]> down = new ArrayList<>();
        ArrayList<int[]> left = new ArrayList<>();
        ArrayList<int[]> right = new ArrayList<>();

        for (int i = 0; i < 18; i++) {
            up.add(new int[]{i, 0});
            down.add(new int[]{17 - i, 17});
        }
        for (int j = 1; j < 17; j++) {
            left.add(new int[]{0, 17 - j});
            right.add(new int[]{17, j});
        }

        traversalWalls = new ArrayList<>();
        traversalWalls.addAll(up);
        traversalWalls.addAll(right);
        traversalWalls.addAll(down);
        traversalWalls.addAll(left);

    }

    /**
     * display the next 5 balls that are going to spawn at the top left bar with its specific color
     */
    public void ballsDisplay(){
        fill(0);
        noStroke();
        rect(10, 15, 160, 40);
        Position.oneFrame();

        for (Position P : Position.values()) {
            if (P.getNumber() == -1){
                continue;
            }
            switch (P.getNumber()){
                case 0:
                    this.image(ball0, P.getX(), P.getY());
                    break;
                case 1:
                    this.image(ball1, P.getX(), P.getY());
                    break;
                case 2:
                    this.image(ball2, P.getX(), P.getY());
                    break;
                case 3:
                    this.image(ball3, P.getX(), P.getY());
                    break;
                case 4:
                    this.image(ball4, P.getX(), P.getY());
                    break;
            }
        }
        fill(255);
        rect(170, 15, 160, 40);

    }

    /**
     * decrement the time in frame,
     * calculate the real time
     */
    public void decTime(){
        Timer++;
        if (Timer % 30 == 0){
            gameTimer--;
        }

        if (gameTimer == 0 && !gameWin){
            spacePressed = false;
            gameLose = true;
        }
    }

    public float spawnTimeCalculator(){
        float remainingTime = 0;
        for (Ball B : balls){
            if (!B.isSpawn()){
                remainingTime = B.readyToEmit(millis());
                break;
            }
        }
        return remainingTime;
    }

    /**
     * move all the ball that is enabled
     */
    public void move(){
        for (Ball B : balls){
            if (B.isEnable()) {
                B.move();
            }
        }
    }

    /**
     * draw the background, it should be executed firstly
     */
    public void drawElements(){
        for(Hole H : holes){
            switch (H.getNumber()){
                case 0:
                    this.image(hole0, H.getX() * CELLSIZE, (H.getY() + 2) * CELLSIZE);
                    break;
                case 1:
                    this.image(hole1, H.getX() * CELLSIZE, (H.getY() + 2) * CELLSIZE);
                    break;
                case 2:
                    this.image(hole2, H.getX() * CELLSIZE, (H.getY() + 2) * CELLSIZE);
                    break;
                case 3:
                    this.image(hole3, H.getX() * CELLSIZE, (H.getY() + 2) * CELLSIZE);
                    break;
                case 4:
                    this.image(hole4, H.getX() * CELLSIZE, (H.getY() + 2) * CELLSIZE);
                    break;
            }
        }

        for(Spawner s : spawners){
            this.image(entrypoint, s.getX() * CELLSIZE, (s.getY() + 2) * CELLSIZE);
        }

        for (Wall w : walls){
            switch (w.getNumber()){
                case 0:
                    this.image(wall0, w.getX() * CELLSIZE, (w.getY() + 2) * CELLSIZE);
                    break;
                case 1:
                    this.image(wall1, w.getX() * CELLSIZE, (w.getY() + 2) * CELLSIZE);
                    break;
                case 2:
                    this.image(wall2, w.getX() * CELLSIZE, (w.getY() + 2) * CELLSIZE);
                    break;
                case 3:
                    this.image(wall3, w.getX() * CELLSIZE, (w.getY() + 2) * CELLSIZE);
                    break;
                case 4:
                    this.image(wall4, w.getX() * CELLSIZE, (w.getY() + 2) * CELLSIZE);
            }
        }
    }

    /**
     * draw all the ball above the background
     */
    public void drawBalls(){
        for(Ball B : balls){
            if (B.isEnable()){
                switch (B.getNumber()){
                    case 0:
                        this.image(ball0, B.getPosition().x, B.getPosition().y, B.getSize(), B.getSize());
                        break;
                    case 1:
                        this.image(ball1, B.getPosition().x, B.getPosition().y, B.getSize(), B.getSize());
                        break;
                    case 2:
                        this.image(ball2, B.getPosition().x, B.getPosition().y, B.getSize(), B.getSize());
                        break;
                    case 3:
                        this.image(ball3, B.getPosition().x, B.getPosition().y, B.getSize(), B.getSize());
                        break;
                    case 4:
                        this.image(ball4, B.getPosition().x, B.getPosition().y, B.getSize(), B.getSize());
                        break;
                }
            }
        }
    }

    /**
     * check all the balls with all line and wall
     * if the seems like they are going to hit,
     * call related method
     */
    public void hitDetector(){
        for (Ball B : balls){
            if (!B.isEnable()){
                continue;
            }
            // edge detector
            PVector position = B.getPosition();
            if (position.x <= 5 || position.x >= WIDTH - CELLSIZE){
                PVector temp = new PVector(-B.getVelocity().x, B.getVelocity().y);
                B.setVelocity(temp);
            }
            if (position.y <= TOPBAR + 5 || position.y >= HEIGHT - CELLSIZE){
                PVector temp = new PVector(B.getVelocity().x, -B.getVelocity().y);
                B.setVelocity(temp);
            }

            // wall detector
            for (Wall W : walls){
                W.hitDetector(B);
            }

            // line detector
            for (Line L : lines){
                if (L.isDisable()){
                    continue;
                }
                for (int i = 0; i < L.size(); i++){
                    L.hitDetector(B);
                }
            }
        }
    }

    /**
     * check all the balls with hole
     * if the seems like ball are going to be caught
     * call related method
     */
    public void catchDetector(){
        for (Ball B : balls){
            B.holeDetector(holes);
        }
    }

    /**
     * draw all the lines that is enabled
     */
    public void drawLines(){
        stroke(0);
        strokeWeight(10);
        for (Line line : lines){
            if (line.isDisable()){
                continue;
            }
            for (int i = 1; i < line.size(); i++){
                PVector p1 = line.points.get(i);
                PVector p2 = line.points.get(i-1);
                line(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    /**
     * full the game board with tile
     */
    public void drawBackground(){
        for (int i = 0; i < 18; i++){
            for (int j = 0; j < 18; j++){
                this.image(this.tile, (float)(32 * j), (float)(32 * (i + 2)));
            }
        }
    }


    public static void main(String[] args) {
        PApplet.main("inkball.App");
    }

}
