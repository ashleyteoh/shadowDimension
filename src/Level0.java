import bagel.*;

/**
 * The type Level 0.
 */
public class Level0 extends Level {

    private final static int TITLE_X = 260;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 90;
    private final static int INS_Y_OFFSET = 190;
    private final static int WIN_MS = 3000;
    private final static String INSTRUCTION_MESSAGE = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String WORLD_FILE = "res/level0.csv";
    private final static Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final static String WIN_MESSAGE = "LEVEL COMPLETE!";
    private boolean hasStarted;
    private boolean gameOver;
    private boolean playerWin;
    private boolean levelDone;
    private int winCounter = 0;

    /**
     * Instantiates a new Level 0.
     */
    public Level0() {
        super(WORLD_FILE);
        hasStarted = false;
        gameOver = false;
        playerWin = false;
        levelDone = false;
    }

    /**
     * Method used to draw the start screen title and instructions
     */
    @Override
    public void drawStartScreen(){
        TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
        INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE,TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);
    }


    /**
     * Checks if level is done
     *
     * @return the boolean
     */
    public boolean isLevelDone() {
        return levelDone;
    }

    /**
     * Sets level done.
     *
     * @param levelDone whether the level is done
     */
    public void setLevelDone(boolean levelDone) {
        this.levelDone = levelDone;
    }

    @Override
    public void update(Input input) {
        winCounter++;

        if(!hasStarted){
            drawStartScreen();
            if (input.wasPressed(Keys.SPACE)){
                hasStarted = true;
            }
        }

        if (gameOver){
            drawMessage(END_MESSAGE);
        } else if (playerWin) {
            drawMessage(WIN_MESSAGE);
        }
        /* display the complete message for 3 seconds */
        if (playerWin && winCounter * 1000 / REFRESH_RATE >= WIN_MS) {
            levelDone = true;
        }
        // game is running
        if (hasStarted && !gameOver && !playerWin){
            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

            for(Barrier current: getBarriers()){
                current.update();
            }
            for(Sinkhole current: getSinkholes()){
                current.update();
            }
            getPlayer().update(input, this);

            if (getPlayer().isDead()){
                gameOver = true;
            }

            if (getPlayer().reachedGate()){
                playerWin = true;
                winCounter = 0;
            }

        }
    }
}
