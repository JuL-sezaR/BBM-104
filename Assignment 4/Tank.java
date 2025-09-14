import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

/**
 * This class represents the player tank object
 * It has a different visual than the other tanks, being yellow and fast
 * I also wanted to add an invulnerability mechanic to the game for the retro
 * So, when a player explodes it re-spawns into the same spawn-point, but being invulnerable for 2 seconds (you'll understand it when you die)
 * It handles hitting walls perfectly, so you cannot pass through walls
 */
public class Tank {
    private static final String YELLOW_TANK_PATH = "/assets/yellowTank1.png";
    private static final String YELLOW_TANK_ANIMATION_PATH = "/assets/yellowTank2.png";
    private ImageView tankView;

    private double xVelocity;
    private double yVelocity;
    private final double speed = 2.0;

    private double rotationAngle;
    private boolean hasMoved;

    private Image baseImage;
    private Image animationImage;

    private boolean useBaseImage = true;
    private long lastSwitchTime = 0;
    private final long switchInterval = 100_000_000;

    private int lives = 3;
    private boolean exploded = false;
    private long respawnTime = 0;
    private final long invulnerabilityInterval = 2_000_000_000;

    public Tank() {
        baseImage = new Image(getClass().getResource(YELLOW_TANK_PATH).toExternalForm());
        animationImage = new Image(getClass().getResource(YELLOW_TANK_ANIMATION_PATH).toExternalForm());

        tankView = new ImageView(baseImage);
        tankView.setPreserveRatio(true);
        tankView.setFitHeight(32);
        tankView.setFitWidth(32);
    }

    /*
    Helper method for creating the tank
     */
    public ImageView getTank() {
        return tankView;
    }

    /*
    Helper method for UI, like pause screen or game over screen, etc.
     */
    public int getLives() {
        return lives;
    }

    /*
    Helper function to understand if player tank is exploded or alive
     */
    public boolean exploded() {
        return exploded;
    }

    /*
    Helper function to understand if player tank is invulnerable
     */
    public boolean isInvulnerable(long now) {
        return now - respawnTime < invulnerabilityInterval;
    }

    /*
    Method for taking damage, also handles to spawn the tank if it has lives at the center
     */
    public void takeDamage(long now) {
        if (isInvulnerable(now)) return;

        lives--;
        if (lives <= 0) {
            exploded = true;
        } else {
            // Respawn at center
            positionCenter(1280, 720); // Adjust screen dimensions as needed
            respawnTime = now;
        }
    }

    /*
    Helper function for the resetting/restarting the game
     */
    public void reset() {
        lives = 3;
        exploded = false;
        respawnTime = 0;
        positionCenter(1280, 720);
    }

    /*
    Helper method for setting tank's direction, which can move to 4 different directions
     */
    public void setDirection(int dx, int dy) {
        xVelocity = dx * speed;
        yVelocity = dy * speed;

        if (dx == 1){
            rotationAngle = 0;
        } else if (dx == -1){
            rotationAngle = 180;
        } else if (dy == 1){
            rotationAngle = 90;
        } else if (dy == -1){
            rotationAngle = 270;
        }

        tankView.setRotate(rotationAngle);

        if ( dx != 0 || dy != 0 ){
            hasMoved = true;
        } else {
            hasMoved = false;
            useBaseImage = true;
            tankView.setImage(baseImage);
        }
    }

    /*
    Helper method for positioning the player tank at the center
     */
    public void positionCenter(int screenWidth, int screenHeight) {
        double centerX = (screenWidth - tankView.getBoundsInParent().getWidth()) / 2;
        double centerY = (screenHeight - tankView.getBoundsInParent().getHeight()) / 2;
        tankView.setX(centerX);
        tankView.setY(centerY);

    }

    /*
    Method for the tank to move
     */
    public void updatePosition(long now, List<ImageView> walls) {
        double newX = tankView.getX() + xVelocity;
        double newY = tankView.getY() + yVelocity;

        // Create bounds for proposed new position
        double tankWidth = tankView.getBoundsInParent().getWidth();
        double tankHeight = tankView.getBoundsInParent().getHeight();

        // Default to not blocked
        boolean blockedX = false;
        boolean blockedY = false;

        // Check each wall
        for (ImageView wall : walls) {
            if (wall.getBoundsInParent().intersects(newX, tankView.getY(), tankWidth, tankHeight)) {
                blockedX = true;
            }
            if (wall.getBoundsInParent().intersects(tankView.getX(), newY, tankWidth, tankHeight)) {
                blockedY = true;
            }
        }

        // Only apply movement if not blocked
        if (!blockedX) tankView.setX(newX);
        if (!blockedY) tankView.setY(newY);

        // Handle animation frame switching
        if (hasMoved) {
            if (now - lastSwitchTime > switchInterval) {
                useBaseImage = !useBaseImage;
                tankView.setImage(useBaseImage ? baseImage : animationImage);
                lastSwitchTime = now;
            }
        }

        // Effects for the time interval of being invulnerable(blinking)
        if (isInvulnerable(now)) {
            long blinkInterval = 200_000_000;
            boolean shouldShow = ((now - respawnTime) / blinkInterval) % 2 == 0;
            tankView.setVisible(shouldShow);
        } else {
            tankView.setVisible(true);
        }
    }

    /*
    These two are helper methods for both explosions and bullet shooting
     */
    public double getCenterX() {
        return tankView.getX() + tankView.getFitWidth() / 2;
    }

    public double getCenterY() {
        return tankView.getY() + tankView.getFitHeight() / 2;
    }
}
