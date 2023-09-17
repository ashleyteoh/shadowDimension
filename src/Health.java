import bagel.*;

import static java.lang.Math.round;

/**
 * The type Health.
 */
public class Health {

    private int percent;
    private int maxHealth;
    private int currentHealth;
    private final static DrawOptions GREEN = new DrawOptions().setBlendColour(0, 0.8,0.2);
    private final static DrawOptions ORANGE = new DrawOptions().setBlendColour(0.9, 0.6, 0);
    private final static DrawOptions RED = new DrawOptions().setBlendColour(1, 0, 0);
    private final static int LOW = 35;
    private final static int HIGH = 65;
    private DrawOptions colour;
    private Font healthFont;

    /**
     * Instantiates a new Health.
     *
     * @param maxHealth the max health
     * @param fontSize  the font size
     */
    public Health(int maxHealth, int fontSize) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.healthFont = new Font("res/frostbite.ttf", fontSize);
    }

    /**
     * Sets current health.
     *
     * @param currentHealth the current health
     */
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    /**
     * Gets current health.
     *
     * @return the current health
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Gets max health.
     *
     * @return the max health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Update.
     *
     * @param HEALTH_X the health x coordinate
     * @param HEALTH_Y the health y coordinate
     */
    /* calculate and print health percent with colour */
    protected void update(double HEALTH_X, double HEALTH_Y) {

        percent = round(currentHealth * 100 / maxHealth);

        /* determine colour of health */
        if (LOW < percent && percent <= HIGH) {
            colour = ORANGE;
        }
        else if (percent <= LOW) {
            colour = RED;
        }
        else {
            colour = GREEN;
        }
        healthFont.drawString(percent + "%" , HEALTH_X, HEALTH_Y, colour);
    }

}
