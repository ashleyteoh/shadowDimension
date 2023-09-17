import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;

/**
 * The type Level 1.
 */
public class Level1 extends Level {
    private final static String TITLE = "PRESS SPACE TO START";
    private final static String INSTRUCTION_MESSAGE = "PRESS A TO ATTACK\nDEFEAT NAVEC TO WIN";
    private final static int TITLE_X = 350;
    private final static int TITLE_Y = 350;
    private final static int INS_Y_OFFSET = 40;
    private final static Image BACKGROUND_IMAGE = new Image("res/background1.png");
    private final static String WORLD_FILE = "res/level1.csv";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";
    private int timescale = 0;
    private final int MAX_TIMESCALE = 3;
    private final double TIME_CHANGE = 1.5;
    private boolean hasStarted;
    private boolean gameOver;
    private boolean playerWin;

    /**
     * Instantiates a new Level 1.
     */
    public Level1() {
        super(WORLD_FILE);

        hasStarted = false;
        gameOver = false;
        playerWin = false;
    }

    @Override
    public void drawStartScreen() {
        INSTRUCTION_FONT.drawString(TITLE, TITLE_X, TITLE_Y);
        INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE,TITLE_X, TITLE_Y + INS_Y_OFFSET);
    }

    @Override
    public void update(Input input) {
        updateTime(input);

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

        // game is running
        if (hasStarted && !gameOver && !playerWin){
            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

            for(Barrier current: getBarriers()){
                current.update();
            }
            for(Sinkhole current: getSinkholes()){
                current.update();
            }
            for (NormalDemon current: getDemons()) {
                if (current.getHealth().getCurrentHealth() > 0) {
                    current.update(this, getPlayer());
                }
            }
            getPlayer().update(input, this);
            getNavec().update(this, getPlayer());

            if (getPlayer().isDead()){
                gameOver = true;
            }

            if (getNavec().getHealth().getCurrentHealth() <= 0){
                playerWin = true;
            }

        }
    }

    /**
     * Changes the speed of demons based on level timescale
     * @param input keyboard input
     */
    private void updateTime(Input input) {
        /* Increase speed */
        if (input.wasPressed(Keys.L) && timescale != MAX_TIMESCALE) {
            timescale++;
            for (NormalDemon current : getDemons()) {
                double newSpeed = current.getSpeed() * TIME_CHANGE;
                current.setSpeed(newSpeed);
            }

            getNavec().setSpeed(getNavec().getSpeed() * TIME_CHANGE);
            System.out.println("Sped up, Speed: " + timescale);
        }
        /* Decrease speed */
        if (input.wasPressed(Keys.K) && timescale != -MAX_TIMESCALE) {
            timescale--;
            for (NormalDemon current : getDemons()) {
                double newSpeed = current.getSpeed() / TIME_CHANGE;
                current.setSpeed(newSpeed);
            }
            getNavec().setSpeed(getNavec().getSpeed() / TIME_CHANGE);
            System.out.println("Slowed down, Speed: " + timescale);
        }
    }
}
