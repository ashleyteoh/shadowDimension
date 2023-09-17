import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Ashley Teoh
 */
public class ShadowDimension extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private Level0 level0;
    private Level1 level1;


    /**
     * Instantiates a new Shadow dimension.
     */
    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        level0 = new Level0();
        level1 = new Level1();

    }

    /**
     * The entry point for the program.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        if (input.wasPressed(Keys.W)) {
            level0.setLevelDone(true);
        }

        if (!level0.isLevelDone()) {
            level0.update(input);
        }
        else {
            level1.update(input);
        }
    }
}