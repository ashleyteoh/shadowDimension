import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The type Level.
 */
public abstract class Level {

    private ArrayList<Sinkhole> sinkholes;
    private ArrayList<Barrier> barriers;
    private ArrayList<NormalDemon> demons;
    private Player player;
    private Point topLeft;
    private Point bottomRight;
    private Navec navec;
    protected static final int REFRESH_RATE = 60;
    private final static int TITLE_FONT_SIZE = 75;
    protected static final Font TITLE_FONT = new Font("res/frostbite.ttf", TITLE_FONT_SIZE);
    private final static int INSTRUCTION_FONT_SIZE = 40;
    protected final static Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", INSTRUCTION_FONT_SIZE);
    protected final static String END_MESSAGE = "GAME OVER!";


    /**
     * Instantiates a new Level.
     *
     * @param worldFile the csv file name
     */
    public Level(String worldFile) {
        this.sinkholes = new ArrayList<>();
        this.barriers = new ArrayList<>();
        this.demons = new ArrayList<>();
        readCSV(worldFile);
    }

    /**
     * Method used to read file and create objects
     * @param file the csv filename
     */
    private void readCSV(String file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            /* read each line of type, x, y coordinates */
            String line;
            while((line = reader.readLine()) != null){
                String[] sections = line.split(",");
                int x = Integer.parseInt(sections[1]);
                int y = Integer.parseInt(sections[2]);
                switch (sections[0]) {
                    case "Fae":
                    case "Player":
                        this.player = new Player(x, y);
                        break;
                    case "Wall":
                        barriers.add(new Barrier(x, y, "wall"));
                        break;
                    case "Sinkhole":
                        sinkholes.add(new Sinkhole(x, y));
                        break;
                    case "Tree":
                        barriers.add(new Barrier(x, y, "tree"));
                        break;
                    case "Demon":
                        demons.add(new NormalDemon(x, y));
                        break;
                    case "Navec":
                        navec = new Navec(x, y);
                        break;
                    case "TopLeft":
                        topLeft = new Point(x, y);
                        break;
                    case "BottomRight":
                        bottomRight = new Point(x, y);
                        break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Method that checks for collisions between Fae and the other entities, and performs
     * corresponding actions.
     *
     * @param player the player
     */
    public void checkCollisions(Player player){
        Rectangle faeBox = player.getBoundingBox();
        Health health = player.getPlayerHealth();
        /* barrier collision */
        for (Barrier current : barriers){
            Rectangle barrBox = current.getBoundingBox();
            if (faeBox.intersects(barrBox)){
                player.moveBack();
            }
        }
        /* sinkhole collision */
        for (Sinkhole hole : sinkholes){
            Rectangle holeBox = hole.getBoundingBox();
            if (hole.isActive() && faeBox.intersects(holeBox)){
                health.setCurrentHealth(Math.max(health.getCurrentHealth() - hole.getDamagePoints(), 0));
                player.moveBack();
                hole.setActive(false);
                System.out.println("Sinkhole inflicts " + hole.getDamagePoints() + " damage points on Fae. " +
                        "Fae's current health: " + health.getCurrentHealth() + "/" + health.getMaxHealth());
            }
        }
    }

    /**
     * Check collisions between demon and level barriers.
     *
     * @param demon the demon
     */
    public void checkCollisions(Demon demon){
        Rectangle demonBox = demon.getBoundingBox();

        for (Barrier current : barriers){
            Rectangle barrBox = current.getBoundingBox();
            if (demonBox.intersects(barrBox)){
                demon.changeDir();
            }
        }

        for (Sinkhole current :sinkholes) {
            Rectangle holeBox = current.getBoundingBox();
            if (demonBox.intersects(holeBox) && current.isActive()){
                demon.changeDir();
            }
        }
    }

    /**
     * Method that checks if Fae has gone out-of-bounds and performs corresponding action
     *
     * @param player the player
     */
    public void checkOutOfBounds(Player player){
        Point currentPosition = player.getPosition();
        if ((currentPosition.y > bottomRight.y) || (currentPosition.y < topLeft.y) || (currentPosition.x < topLeft.x)
                || (currentPosition.x > bottomRight.x)){
            player.moveBack();
        }
    }

    /**
     * Method that checks if demon has gone out-of-bounds and performs corresponding action
     *
     * @param demon the demon
     */
    public void checkOutOfBounds(Demon demon){
        Point currentPosition = demon.getPosition();
        if ((currentPosition.y > bottomRight.y) || (currentPosition.y < topLeft.y) || (currentPosition.x < topLeft.x)
                || (currentPosition.x > bottomRight.x)){
            demon.changeDir();
        }
    }

    /**
     * Method used to draw the start screen title and instructions
     */
    public abstract void drawStartScreen();

    /**
     * Method used to draw end screen messages
     *
     * @param message the string to be printed
     */
    public void drawMessage(String message) {
        TITLE_FONT.drawString(message, (Window.getWidth()/2.0 - (TITLE_FONT.getWidth(message)/2.0)),
                (Window.getHeight()/2.0 + (TITLE_FONT_SIZE/2.0)));
    }

    /**
     * Method that performs state update
     *
     * @param input the input
     */
    public abstract void update(Input input);

    /**
     * Gets sinkholes array.
     *
     * @return the sinkholes
     */
    public ArrayList<Sinkhole> getSinkholes() {
        return sinkholes;
    }

    /**
     * Gets barriers array.
     *
     * @return the barriers
     */
    public ArrayList<Barrier> getBarriers() {
        return barriers;
    }

    /**
     * Gets demons array.
     *
     * @return the demons
     */
    public ArrayList<NormalDemon> getDemons() {
        return demons;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets navec.
     *
     * @return the navec
     */
    public Navec getNavec() {
        return navec;
    }
}
