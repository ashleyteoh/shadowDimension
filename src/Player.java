import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The type Player.
 */
public class Player {
    private final static String FAE_LEFT = "res/fae/faeLeft.png";
    private final static String FAE_RIGHT = "res/fae/faeRight.png";
    private final static String FAE_ATTACK_LEFT = "res/fae/faeAttackLeft.png";
    private final static String FAE_ATTACK_RIGHT = "res/fae/faeAttackRight.png";
    private final static int MAX_HEALTH_POINTS = 100;
    private final static int DAMAGE = 20;
    private final static double MOVE_SIZE = 2;
    private final static int WIN_X = 950;
    private final static int WIN_Y = 670;
    private final static int HEALTH_X = 20;
    private final static int HEALTH_Y = 25;
    private final static int FONT_SIZE = 30;

    private Point position;
    private Point prevPosition;
    private Image currentImage;
    private boolean facingRight;
    private Health playerHealth;
    private boolean attackState;
    private boolean invisState;
    private boolean cooldown = false;
    private int attackCounter;
    private int invisCounter;
    private final int ATTACK_MS = 1000;
    private final int INVIS_MS = 3000;
    private final int COOLDOWN_MS = 3000;
    private final int REFRESH_RATE = 60;
    private int atttackDamage;

    /**
     * Instantiates a new Player.
     *
     * @param startX the start x
     * @param startY the start y
     */
    public Player(int startX, int startY){
        this.position = new Point(startX, startY);
        this.currentImage = new Image(FAE_RIGHT);
        this.facingRight = true;
        this.playerHealth = new Health(MAX_HEALTH_POINTS, FONT_SIZE);
        this.atttackDamage = DAMAGE;
    }

    /**
     * Method that performs state update
     *
     * @param input the input
     * @param level the level
     */
    public void update(Input input, Level level){
        attackCounter++;
        invisCounter++;

        this.makeMove(input);
        this.stateCheck(input);
        this.currentImage.drawFromTopLeft(position.x, position.y);
        level.checkCollisions(this);
        this.playerHealth.update(HEALTH_X, HEALTH_Y);
        level.checkOutOfBounds(this);
    }

    /**
     * Method that stores Fae's previous position
     */
    private void setPrevPosition(){
        this.prevPosition = new Point(position.x, position.y);
    }

    /**
     * Method that moves Fae back to previous position
     */
    public void moveBack(){
        this.position = prevPosition;
    }

    /**
     * Method that moves Fae given the direction
     */
    private void move(double xMove, double yMove){
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        this.position = new Point(newX, newY);
    }

    /**
     * Is attack state boolean.
     *
     * @return the boolean
     */
    public boolean isAttackState() {
        return attackState;
    }

    /**
     * Gets atttack damage.
     *
     * @return the atttack damage
     */
    public int getAtttackDamage() {
        return atttackDamage;
    }

    /**
     * Method that checks if Fae's health has depleted
     *
     * @return the boolean
     */
    public boolean isDead() {
        return playerHealth.getCurrentHealth() <= 0;
    }

    /**
     * Method that checks if Fae has found the gate
     *
     * @return the boolean
     */
    public boolean reachedGate(){
        return (this.position.x >= WIN_X) && (this.position.y >= WIN_Y);
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Gets current image.
     *
     * @return the current image
     */
    public Image getCurrentImage() {
        return currentImage;
    }

    /**
     * Gets player health.
     *
     * @return the player health
     */
    public Health getPlayerHealth() {
        return playerHealth;
    }

    /**
     * Get bounding box rectangle.
     *
     * @return the rectangle
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(position, this.currentImage.getWidth(), this.currentImage.getHeight());
    }

    /**
     * Make player move.
     *
     * @param input the keyboard input
     */
    public void makeMove(Input input) {
        if (input.isDown(Keys.UP)){
            setPrevPosition();
            move(0, -MOVE_SIZE);
        } else if (input.isDown(Keys.DOWN)){
            setPrevPosition();
            move(0, MOVE_SIZE);
        } else if (input.isDown(Keys.LEFT)){
            setPrevPosition();
            move(-MOVE_SIZE,0);
            if (facingRight) {
                this.currentImage = attackState ? new Image(FAE_ATTACK_LEFT) : new Image(FAE_LEFT);
                facingRight = !facingRight;
            }
        } else if (input.isDown(Keys.RIGHT)){
            setPrevPosition();
            move(MOVE_SIZE,0);
            if (!facingRight) {
                this.currentImage = attackState ? new Image(FAE_ATTACK_RIGHT) : new Image(FAE_RIGHT);
                facingRight = !facingRight;
            }
        }
    }

    /**
     * Performs state actions of the player
     *
     * @param input the input
     */
    public void stateCheck(Input input) {
        this.attackStateCheck(input);
        this.invisCheck();
    }

    /**
     * Check attack state of the player
     *
     * @param input the input
     */
    public void attackStateCheck(Input input) {
        /* player attack */
        if (input.wasPressed(Keys.A) && !cooldown) {
            attackState = true;
            attackCounter = 0;
            this.currentImage = facingRight ? new Image(FAE_ATTACK_RIGHT) : new Image(FAE_ATTACK_LEFT);
        }
        /* player cooldown */
        if (attackState && (attackCounter * 1000 / REFRESH_RATE) >= ATTACK_MS) {
            attackState = false;
            attackCounter = 0;
            cooldown = true;
            this.currentImage = facingRight ? new Image(FAE_RIGHT) : new Image(FAE_LEFT);
        }
        /* player cooldown ended */
        if (cooldown && (attackCounter * 1000 / REFRESH_RATE) >= COOLDOWN_MS) {
            cooldown = false;
        }
    }

    /**
     * Check player invincible cooldown
     */
    public void invisCheck() {
        /* player invincible state ended */
        if (invisState && (invisCounter * 1000 / REFRESH_RATE) >= INVIS_MS) {
            invisState = false;
        }
    }

    /**
     * Check if player is attacked
     *
     * @param fireBox the Rectangle box of demon's fire
     * @param damage  the demon's damage value
     * @param type    the type of demon
     */
    public void checkAttack(Rectangle fireBox, int damage, String type) {
        if (fireBox.intersects(this.getBoundingBox()) && !this.invisState) {
            this.playerHealth.setCurrentHealth(this.playerHealth.getCurrentHealth() - damage);
            this.invisState = true;
            invisCounter = 0;
            System.out.println(type + " inflicts " + damage + " points on Fae. Fae's current health: "
                        + this.playerHealth.getCurrentHealth() + "/" + MAX_HEALTH_POINTS);
        }
    }

}