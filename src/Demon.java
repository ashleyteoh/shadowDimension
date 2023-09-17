import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.Random;

/**
 * The type Demon.
 */
public abstract class Demon {
    private Point position;
    private final static int INVINCIBLE_MS = 3000;
    private final static int REFRESH_RATE = 60;
    private boolean attackState = true;
    private int invisCounter = 0;
    private boolean invisState = false;
    private Point firePoint;
    private DrawOptions rotation;
    protected final static double MIN_SPEED = 0.2;
    protected final static double MAX_SPEED = 0.7;
    private Health health;
    protected boolean facingLeft;
    private final static int HEALTH_OFFSET = 6;
    private Image currentImage;
    private static final int HEALTH_FONT = 15;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private static final int NW = 0;
    private static final int SW = 1;
    private static final int NE = 2;
    private static final int SE = 3;
    private static final double NW_ROTATION = 0;
    private static final double SW_ROTATION = Math.PI * 3 / 2;
    private static final double NE_ROTATION = Math.PI / 2;
    private static final double SE_ROTATION = Math.PI;
    private static final int DIRECTIONS = 4;


    protected Random rand = new Random();
    private int direction;

    /**
     * Instantiates a new Demon.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param maxHealth the max health
     */
    public Demon(int x, int y, int maxHealth) {
        this.position = new Point(x, y);
        this.health = new Health(maxHealth, HEALTH_FONT);
        direction = rand.nextInt(DIRECTIONS);
    }

    /**
     * Sets position.
     *
     * @param position the position point
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Gets position.
     *
     * @return the position point
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
     * Changes demon direction.
     */
    public void changeDir() {
        switch (this.direction) {
            case LEFT:
                this.setDirection(RIGHT);
                break;
            case RIGHT:
                this.setDirection(LEFT);
                break;
            case UP:
                this.setDirection(DOWN);
                break;
            case DOWN:
                this.setDirection(UP);
                break;
        }
    }

    /**
     * Sets direction.
     *
     * @param direction the direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Gets bounding box.
     *
     * @return the bounding box
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(this.position, currentImage.getWidth(), currentImage.getHeight());
    }

    /**
     * Move.
     *
     * @param xMove the x step size
     * @param yMove the y step sizee
     */
    /* new position from demon movement */
    public void move(double xMove, double yMove) {
        double newX = getPosition().x + xMove;
        double newY = getPosition().y + yMove;
        this.setPosition(new Point(newX, newY));
    }

    /**
     * Attack.
     *
     * @param player       the player
     * @param ATTACK_RANGE the attack range
     * @param currFire     the fire image
     * @param DAMAGE       the damage value
     * @param type         the demon type
     */
    public void attack(Player player, int ATTACK_RANGE, Image currFire, int DAMAGE, String type) {
        Point faePos = player.getPosition();
        Point demonPos = this.position;

        Point faeCenter = new Point(faePos.x + (player.getCurrentImage().getWidth() / 2),
                faePos.y - player.getCurrentImage().getHeight() / 2);
        Point demonCenter = new Point(demonPos.x + (this.currentImage.getWidth() / 2),
                demonPos.y - this.currentImage.getHeight() / 2);

        double fireWidth = currFire.getWidth();
        double fireHeight = currFire.getHeight();

        /* Fae within attack radius of demon */
        if (demonCenter.distanceTo(faeCenter) <= ATTACK_RANGE ) {
            /* get location of fae relative to demon */
            int loc = fireLocation(faeCenter, demonCenter);

            /* set fire rotation based on location */
            switch (loc) {
                case NW:
                    firePoint = new Point(demonPos.x - fireWidth, demonPos.y - fireHeight);
                    rotation = new DrawOptions().setRotation(NW_ROTATION);
                    break;
                case SW:
                    firePoint = new Point(demonPos.x - fireWidth, demonPos.y + fireHeight);
                    rotation = new DrawOptions().setRotation(SW_ROTATION);
                    break;
                case NE:
                    firePoint = new Point(demonPos.x + this.getCurrentImage().getWidth(), demonPos.y - fireHeight);
                    rotation = new DrawOptions().setRotation(NE_ROTATION);
                    break;
                case SE:
                    firePoint = new Point(demonPos.x + this.getCurrentImage().getWidth(), demonPos.y + fireHeight);
                    rotation = new DrawOptions().setRotation(SE_ROTATION);
                    break;
            }
            currFire.drawFromTopLeft(firePoint.x, firePoint.y, rotation);

            /* check for fae intersection with fire */
            Rectangle fireBox = new Rectangle(firePoint.x, firePoint.y, fireWidth, fireHeight);
            player.checkAttack(fireBox, DAMAGE, type);
        }
    }

    /**
     * Determine location of demon fire
     *
     * @param faeCenter   the fae center point
     * @param demonCenter the demon center point
     * @return the int
     */
    public int fireLocation(Point faeCenter, Point demonCenter) {
        if (faeCenter.x <= demonCenter.x) {
            if (faeCenter.y <= demonCenter.y) return NW;
            return SW;
        }
        else {
            if (faeCenter.y <= demonCenter.y) return NE;
            return SE;
        }
    }

    /**
     * Method that performs demon state update
     *
     * @param level the level
     * @param left  the left image
     * @param right the right image
     * @param invL  the invincible left image
     * @param invR  the invincible right image
     * @param speed the speed
     */
    public void update(Level level, Image left, Image right, Image invL, Image invR, double speed) {
        invisCounter++;

        this.makeMove(left, right, speed);
        this.checkInvis(invL, invR);
        level.checkOutOfBounds(this);
        level.checkCollisions(this);
        this.checkAttack(level.getPlayer());
        this.health.update(position.x, position.y - HEALTH_OFFSET);
        currentImage.drawFromTopLeft(this.position.x, this.position.y);
    }

    /**
     * Check if the demon is attacked.
     *
     * @param player the player
     */
    public void checkAttack(Player player) {
        Rectangle demonBox = this.getBoundingBox();
        /* check valid attack */
        if (!invisState && player.isAttackState() && demonBox.intersects(player.getBoundingBox())) {
            this.health.setCurrentHealth(this.health.getCurrentHealth() - player.getAtttackDamage());
            invisState = true;
            invisCounter = 0;

            /* log print*/
            String type = this instanceof NormalDemon ? "Demon" : "Navec";
            System.out.println("Fae inflicts " + player.getAtttackDamage() + " damage points on " + type + ". "
                    + type + "'s current health: "+ this.health.getCurrentHealth() + "/" + this.health.getMaxHealth());
        }
    }

    /**
     * Make demon move.
     *
     * @param left  the left image
     * @param right the right image
     * @param speed the speed value
     */
    public void makeMove(Image left, Image right, double speed) {
        /* passive demon */
        if (speed == 0) {
            currentImage = facingLeft ? left : right;
            return;
        }

        currentImage = left;
        this.facingLeft = true;
        /* demon movement */
        switch (direction) {
            case LEFT:
                move(-speed, 0);
                break;
            case RIGHT:
                this.facingLeft = false;
                currentImage = right;
                move(speed, 0);
                break;
            case UP:
                move(0, -speed);
                break;
            case DOWN:
                move(0, speed);
        }
    }

    /**
     * Check demon invincibility state
     *
     * @param invL the invincible left image
     * @param invR the invincible right image
     */
    public void checkInvis(Image invL, Image invR) {
        /* invincible cooldown elapsed */
        if (invisState && (invisCounter * 1000 / REFRESH_RATE) >= INVINCIBLE_MS) {
            invisState = false;
        }
        if (invisState) {
            this.currentImage = facingLeft ? invL : invR;
        }
    }

    /**
     * Gets health.
     *
     * @return the health
     */
    public Health getHealth() {
        return health;
    }
}
