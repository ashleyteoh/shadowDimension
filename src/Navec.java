import bagel.Image;

/**
 * The type Navec.
 */
public class Navec extends Demon {
    private final static Image NAVEC_LEFT = new Image("res/navec/navecLeft.png");
    private final static Image NAVEC_RIGHT = new Image("res/navec/navecRight.png");
    private final static Image NAVEC_INVIS_L = new Image("res/navec/navecInvincibleLeft.png");
    private final static Image NAVEC_INVIS_R = new Image("res/navec/navecInvincibleRight.png");
    private final static Image FIRE = new Image("res/navec/navecFire.png");
    private double speed;
    private static final int MAX_HEALTH = 80;
    private static final int DAMAGE = 20;
    private static final int ATTACK_RANGE = 200;

    /**
     * Instantiates a new Navec.
     *
     * @param startX the start x coordinate
     * @param startY the start y coordinate
     */
    public Navec(int startX, int startY) {
        super(startX, startY, MAX_HEALTH);
        speed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * rand.nextDouble();
    }

    /**
     * Gets speed.
     *
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets speed.
     *
     * @param speed the speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }


    /**
     * Update.
     *
     * @param level  the level
     * @param player the player
     */
    public void update(Level level, Player player) {
        super.update(level, NAVEC_LEFT, NAVEC_RIGHT, NAVEC_INVIS_L, NAVEC_INVIS_R, speed);
        this.attack(player, ATTACK_RANGE, FIRE, DAMAGE, "Navec");
    }

}
