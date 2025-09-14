import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents the bullet object of the game
 * Both player and enemy tanks have bullets, which indeed works pretty well
 * Here to get the best results I used some maths, like angles and speed of course
 * I set the speed to 5, because that's what made me feel like it's retro
 */
public class Bullet {

    private static final String BULLET_PATH = "/assets/bullet.png";
    private static final double BULLET_SPEED = 5.0;

    private ImageView bulletView;
    private double dx, dy;
    private double x, y;

    public Bullet(double startX, double startY, double angle) {
        Image bulletImage = new Image(getClass().getResource(BULLET_PATH).toExternalForm());
        bulletView = new ImageView(bulletImage);
        bulletView.setFitWidth(8);
        bulletView.setFitHeight(8);
        this.x = startX;
        this.y = startY;
        bulletView.setX(x);
        bulletView.setY(y);

        dx = Math.cos(angle) * BULLET_SPEED;
        dy = Math.sin(angle) * BULLET_SPEED;
    }

    /*
    This is a getter for showing the bullet in the real game
     */
    public ImageView getImageView() {
        return bulletView;
    }

    /*
    This is a helper method for better efficiency on updating bullet's position
     */
    public void updatePosition() {
        x += dx;
        y += dy;

        bulletView.setX(x);
        bulletView.setY(y);
    }

    /*
    This is another helper method for checking if the bullet is off-screen or not to remove it
     */
    public boolean isOffScreen(double sceneWidth, double sceneHeight) {
        return bulletView.getX() < - 10 || bulletView.getX() > sceneWidth + 10 ||
                bulletView.getY() < -10 || bulletView.getY() > sceneHeight + 10;
    }

    /*
    These two methods are defined to make explosions efficient, so they are another helper methods
     */
    public double getX() {
        return bulletView.getX();
    }

    public double getY() {
        return bulletView.getY();
    }
}
