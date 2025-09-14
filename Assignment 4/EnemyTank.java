import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;
import java.util.Random;

/**
 * This class represents enemy tank object in this game
 * It is a little slower both at shooting and moving than the player
 * It also has different look than the player tank.
 * It handles image changes perfectly and there is any bugging with their directions
 */
public class EnemyTank {
    private static final String ENEMY_TANK_PATH = "/assets/whiteTank1.png";
    private static final String ANIMATION_PATH = "/assets/whiteTank2.png";

    private ImageView tankView;
    private double xVelocity;
    private double yVelocity;
    private double speed = 1.5;

    private double rotationAngle;
    private boolean hasMoved;

    private Image baseImage;
    private Image animationImage;

    private boolean useBaseImage = true;
    private long lastSwitchTime = 0;
    private final long switchInterval = 100_000_000;

    private long lastDirectionChange = 0;
    private final long directionChangeInterval = 2_000_000_000;
    private long lastShot = 0;
    private final long shootInterval = 1_500_000_000;
    private Random random = new Random();

    private boolean isAlive = true;
    private boolean isBlocked = false;

    public EnemyTank(double startX, double startY){
        baseImage = new Image(getClass().getResource(ENEMY_TANK_PATH).toExternalForm());

        try {
            animationImage = new Image(getClass().getResource(ANIMATION_PATH).toExternalForm());
        } catch (Exception e) {
            animationImage = baseImage;;
        }

        tankView = new ImageView(baseImage);
        tankView.setPreserveRatio(true);
        tankView.setFitWidth(32);
        tankView.setFitHeight(32);
        tankView.setX(startX);
        tankView.setY(startY);

        changeDirection();
    }

    /*
    Getter for the ImageView of the enemy tank
     */
    public ImageView getTank(){
        return tankView;
    }

    /*
    Helper function to understand if they are alive
     */
    public boolean isAlive(){
        return isAlive;
    }

    /*
    Helper function for their destruction when hit by a bullet or etc.
     */
    public void destroy() {
        isAlive = false;
    }

    /*
    This is the method for updating enemy tank's position(moving)
     */
    public void updatePosition(long now, List<ImageView> walls) {
        if (!isAlive) return;

        if (now - lastDirectionChange > directionChangeInterval) {
            changeDirection();
            lastDirectionChange = now;
        }

        double newX = tankView.getX() + xVelocity;
        double newY = tankView.getY() + yVelocity;

        double tankWidth = tankView.getBoundsInParent().getWidth();
        double tankHeight = tankView.getBoundsInParent().getHeight();

        boolean blockedX = false;
        boolean blockedY = false;

        if (newX < 0 || newX + tankWidth > 1280){
            blockedX = true;
        }
        if (newY < 0 || newY + tankHeight > 720){
            blockedY = true;
        }

        for (ImageView wall : walls) {
            if (wall.getBoundsInParent().intersects(newX, tankView.getY(), tankWidth, tankHeight)) {
                blockedX = true;
            }
            if (wall.getBoundsInParent().intersects(tankView.getX(), newY, tankWidth, tankHeight)) {
                blockedY = true;
            }

            if ((blockedX || blockedY) && !isBlocked) {
                reverseDirection();
                isBlocked = true;
            } else if (!blockedX && !blockedY) {
                isBlocked = false;
            }
            if (!blockedX) tankView.setX(newX);
            if (!blockedY) tankView.setY(newY);

            if (hasMoved && (xVelocity != 0 || yVelocity != 0)) {
                if (now - lastSwitchTime > switchInterval) {
                    useBaseImage = !useBaseImage;
                    tankView.setImage(useBaseImage ? baseImage : animationImage);
                    lastSwitchTime = now;
                }
            }
        }
    }

    /*
    Helper function to their moving ability, which helps them change Direction
     */
    private void changeDirection() {
        int direction = random.nextInt(5);

        switch (direction) {
            case 0:
                xVelocity = speed;
                yVelocity = 0;
                rotationAngle = 0;
                break;
            case 1:
                xVelocity = -speed;
                yVelocity = 0;
                rotationAngle = 180;
                break;
            case 2:
                xVelocity = 0;
                yVelocity = speed;
                rotationAngle = 90;
                break;
            case 3:
                xVelocity = 0;
                yVelocity = -speed;
                rotationAngle = 270;
                break;
            case 4:
                xVelocity = 0;
                yVelocity = 0;
                rotationAngle = 0;
                break;
        }

        tankView.setRotate(rotationAngle);
        hasMoved = (xVelocity != 0 || yVelocity != 0);

        if (!hasMoved) {
            useBaseImage = true;
            tankView.setImage(baseImage);
        }
    }

    /*
    Helper method to reverse tank's direction, very helpful especially if an enemy tank hits a wall
     */
    private void reverseDirection() {
        xVelocity = -xVelocity;
        yVelocity = -yVelocity;

        if (xVelocity > 0) {
            rotationAngle = 0;
        } else if (xVelocity < 0) {
            rotationAngle = 180;
        } else if (yVelocity > 0) {
            rotationAngle = 90;
        } else if (yVelocity < 0) {
            rotationAngle = 270;
        }
        tankView.setRotate(rotationAngle);
    }

    /*
    This method handles the shooting mechanism of enemy tank
     */
    public boolean shouldShoot(long now) {
        if (!isAlive) return false;

        if (now - lastShot > shootInterval) {
            lastShot = now;
            return true;
        }
        return false;
    }

    /*
    Getters for tank's centers and rotation angle, which helps at shooting bullets and explosions
     */
    public double getCenterX() {
        return tankView.getX() + tankView.getFitWidth() / 2;
    }

    public double getCenterY() {
        return tankView.getY() + tankView.getFitHeight() / 2;
    }

    public double getRotationAngle() {
        return Math.toRadians(tankView.getRotate());
    }
}
