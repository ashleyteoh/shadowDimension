import bagel.Image;

/**
 * The type Normal demon.
 */
public class NormalDemon extends Demon {
    private final static Image DEMON_LEFT = new Image("res/demon/demonLeft.png");
    private final static Image DEMON_RIGHT = new Image("res/demon/demonRight.png");
    private final static Image DEMON_INVIS_L = new Image("res/demon/demonInvincibleLeft.PNG");
    private final static Image DEMON_INVIS_R = new Image("res/demon/demonInvincibleRight.PNG");
    private final static Image FIRE = new Image("res/demon/demonFire.png");
    private boolean aggressive;
    private double speed;
    private static final int MAX_HEALTH = 40;
    private static final int DAMAGE = 10;
    private static final int ATTACK_RANGE = 150;

    /**
     * Instantiates a new Normal demon.
     *
     * @param startX the start x coordinate
     * @param startY the start y coordinate
     */
    public NormalDemon(int startX, int startY) {
        super(startX, startY, MAX_HEALTH);
        aggressive = rand.nextBoolean();

        if (aggressive) {
            speed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * rand.nextDouble();
        } else {
            speed = 0;
            facingLeft = rand.nextBoolean();
        }
    }

    /**
     * Sets speed.
     *
     * @param speed the speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }


    /**
     * Update.
     *
     * @param level  the level
     * @param player the player
     */
    public void update(Level level, Player player) {
        super.update(level, DEMON_LEFT, DEMON_RIGHT, DEMON_INVIS_L, DEMON_INVIS_R, speed);
        this.attack(player, ATTACK_RANGE, FIRE, DAMAGE, "Demon");
    }


}
