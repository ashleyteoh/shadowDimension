import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;


/**
 * The type Barrier.
 */
public class Barrier {

    private final Image WALL = new Image("res/wall.png");
    private final Image TREE = new Image("res/tree.png");
    private final Point position;
    private Image image;

    /**
     * Instantiates a new Barrier.
     *
     * @param startX the start x coordinate
     * @param startY the start y coordinate
     * @param type   the type of barrier
     */
    public Barrier(int startX, int startY, String type){
        this.position = new Point(startX, startY);
        switch (type) {
            case "wall":
                this.image = WALL;
                break;
            case "tree":
                this.image = TREE;
                break;
        }
    }


    /**
     * Method that performs state update
     */
    public void update() {
        this.image.drawFromTopLeft(this.position.x, this.position.y);
    }

    /**
     * Returns the rectangle enclosing the image
     *
     * @return the rectangle
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(position, this.image.getWidth(), this.image.getHeight());
    }
}
