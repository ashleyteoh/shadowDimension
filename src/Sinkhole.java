import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The type Sinkhole.
 */
public class Sinkhole {
    private final static Image SINKHOLE = new Image("res/sinkhole.png");
    private final static int DAMAGE_POINTS = 30;
    private final Point position;
    private boolean isActive;

    /**
     * Instantiates a new Sinkhole.
     *
     * @param startX the start x coordinate
     * @param startY the start y coordinate
     */
    public Sinkhole(int startX, int startY){
        this.position = new Point(startX, startY);
        this.isActive = true;
    }

    /**
     * Method that performs state update
     */
    public void update() {
        if (isActive){
            SINKHOLE.drawFromTopLeft(this.position.x, this.position.y);
        }
    }

    /**
     * Get bounding box rectangle.
     *
     * @return the rectangle
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(position, SINKHOLE.getWidth(), SINKHOLE.getHeight());
    }

    /**
     * Get damage points.
     *
     * @return the damage points
     */
    public int getDamagePoints(){
        return DAMAGE_POINTS;
    }

    /**
     * Whether sinkhole is active boolean.
     *
     * @return the boolean
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets active state of sinkhole
     *
     * @param active the active boolean
     */
    public void setActive(boolean active) {
        isActive = active;
    }
}