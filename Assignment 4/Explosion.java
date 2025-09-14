import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents the explosion object in the game
 * There are two kinds of explosions: small and normal, so I used enum for a better design choice
 * It handles which type of explosion will be shown to the player in which scenarios
 */
public class Explosion {

    public enum ExplosionType {
        SMALL, NORMAL
    }

    private static final String SMALL_EXPLOSION_PATH = "/assets/smallExplosion.png";
    private static final String NORMAL_EXPLOSION_PATH = "/assets/explosion.png";
    private static final long EXPLOSION_DURATION = 200_000_000;

    private ImageView explosionView;
    private long startTime;
    private boolean exploded;

    public Explosion(double x, double y, ExplosionType type) {

        String imagePath;
        double explosionSize;

        switch (type) {
            case SMALL:
                imagePath = SMALL_EXPLOSION_PATH;
                explosionSize = 24;
                break;
            case NORMAL:
                imagePath = NORMAL_EXPLOSION_PATH;
                explosionSize = 64;
                break;
            default:
                imagePath = NORMAL_EXPLOSION_PATH;
                explosionSize = 24;
        }

        Image explosionImage = new Image(getClass().getResource(imagePath).toExternalForm());

        explosionView = new ImageView(explosionImage);
        explosionView.setFitWidth(explosionSize);
        explosionView.setFitHeight(explosionSize);
        explosionView.setPreserveRatio(true);

        explosionView.setX(x - explosionSize / 2);
        explosionView.setY(y - explosionSize / 2);

        startTime = 0;
        exploded = false;
    }

    /*
    Getter of explosion's ImageView
     */
    public ImageView getImageView() {
        return explosionView;
    }

    /*
    This is the update method which helps with the duration of the explosion
     */
    public void update(long now) {
        if (exploded) return;

        if (startTime == 0) {
            startTime = now;
        }

        if (now - startTime > EXPLOSION_DURATION) {
            exploded = true;
        }
    }

    /*
    Helper method for to understand if any tank has been exploded
     */
    public boolean exploded() {
        return exploded;
    }
}
