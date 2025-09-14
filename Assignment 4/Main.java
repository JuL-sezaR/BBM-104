import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the main handling part and creations of this game
 * It handles the creation of everything available in this game, walls, tanks, explosions, enemy tanks, etc.
 * It also handles pressed key method, perfectly being able to move using arrows, X for shooting and more
 * It handles literally everything for movement, only 4 directions, once at a time, stopping when no key is pressed, etc.
 * It uses an AnimationTimer to check if player's lives are less than or equal to zero to set the game over screen visible
 * It handles pausing perfectly too
 */
public class Main extends Application {

    private final List<Bullet> bullets = new ArrayList<>();
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private GameManagement gameLoop;
    private Tank playerTank;
    private Pane root;
    private List<ImageView> walls;
    private List<Explosion> explosions;
    private GameUI gameUI;
    private PauseMenu pauseMenu;
    private boolean isPaused = false;
    private GameOverScreen gameOverScreen;
    private boolean isGameOver = false;
    private AnimationTimer gameStateChecker;


    @Override
    public void start(Stage primaryStage) {
        BackgroundScene backgroundScene = new BackgroundScene();
        Scene scene = backgroundScene.createScene();

        root = (Pane) scene.getRoot(); // Get root pane

        playerTank = new Tank();
        playerTank.positionCenter(WIDTH, HEIGHT); // Position at center
        root.getChildren().add(playerTank.getTank()); // Add tank to scene

        walls = backgroundScene.getWalls();
        explosions = new ArrayList<>();

        // Movement state
        boolean[] keys = new boolean[4]; // Up, Down, Left, Right

        pauseMenu = new PauseMenu(WIDTH, HEIGHT);
        root.getChildren().add(pauseMenu);

        gameOverScreen = new GameOverScreen(WIDTH, HEIGHT);
        root.getChildren().add(gameOverScreen);

        gameLoop = new GameManagement(playerTank, walls, root, bullets, explosions);
        gameLoop.start();

        gameStateChecker = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameLoop.isGameOver() && !isGameOver) {
                    isGameOver = true;
                    gameLoop.pause();
                    gameOverScreen.updateScore(gameUI.getScore());
                    gameOverScreen.setVisible(true);
                }
            }
        };
        gameStateChecker.start();

        gameUI = gameLoop.getGameUI();

        // Key pressed handler
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();

            if (isGameOver) {
                if (code == KeyCode.R) {
                    resetGame();
                    gameOverScreen.setVisible(false);
                    isGameOver = false;
                    gameLoop.resume();
                } else if (code == KeyCode.ESCAPE) {
                    Platform.exit();
                }
                return;
            }

            if (code == KeyCode.P) {
                isPaused = !isPaused;
                if (isPaused) {
                    pauseGame();
                } else {
                    resumeGame();
                }
            }

            if (isPaused) {
                if (code == KeyCode.R) {
                    resetGame();
                    return;
                } else if (code == KeyCode.ESCAPE) {
                    Platform.exit();
                    return;
                }
            }

            else {
                if (code == KeyCode.UP) {
                    keys[0] = true;
                    resetMovement(keys, playerTank, 0, -1);
                } else if (code == KeyCode.DOWN) {
                    keys[1] = true;
                    resetMovement(keys, playerTank, 0, 1);
                } else if (code == KeyCode.LEFT) {
                    keys[2] = true;
                    resetMovement(keys, playerTank, -1, 0);
                } else if (code == KeyCode.RIGHT) {
                    keys[3] = true;
                    resetMovement(keys, playerTank, 1, 0);
                }

                if (code == KeyCode.X) {
                    fireBullet(playerTank, root);
                }
            }
        });

        // Key released handler
        scene.setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.UP) {
                keys[0] = false;
            } else if (code == KeyCode.DOWN) {
                keys[1] = false;
            } else if (code == KeyCode.LEFT) {
                keys[2] = false;
            } else if (code == KeyCode.RIGHT) {
                keys[3] = false;
            }
            stopIfNoKeys(keys, playerTank);
        });


        // Final stage setup
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }

    /*
    This helper method is for pausing the game and showing the pause manu when pressed "P"
     */
    private void pauseGame() {
        if (gameLoop != null) gameLoop.pause();

        int score = (gameUI != null) ? gameUI.getScore() : 0;
        int lives = playerTank.getLives();

        pauseMenu.update(score, lives);
        pauseMenu.setVisible(true);
    }

    /*
    Helper method for pausing the game and hiding the menu after pressing "P" for a second time
     */
    private void resumeGame() {
        pauseMenu.setVisible(false);
        if (gameLoop != null) gameLoop.resume();
        isPaused = false;
    }

    /*
    Helper method for keeping the tank in one direction
     */
    private void resetMovement(boolean[] keys, Tank tank, int dx, int dy) {
        tank.setDirection(dx, dy);
    }

    /*
    Helper function for stopping if there is any keys pressed
     */
    private void stopIfNoKeys(boolean[] keys, Tank tank) {
        for (boolean key : keys) {
            if (key) return;
        }
        tank.setDirection(0, 0); // Stop moving
    }

    /*
    This is the method for firing bullet, it is used for any tank(enemy/player)
     */
    private void fireBullet(Tank tank, Pane root) {
        double tankX = tank.getTank().getX() + tank.getTank().getFitWidth() / 2;
        double tankY = tank.getTank().getY() + tank.getTank().getFitHeight() / 2;
        double angleInDegrees = tank.getTank().getRotate();
        double angleInRadians = Math.toRadians(angleInDegrees);


        Bullet bullet = new Bullet(tankX, tankY, angleInRadians);
        bullets.add(bullet);
        root.getChildren().add(bullet.getImageView());
    }

    /*
    Main resetting method for when pressed "R"
     */
    private void resetGame() {
        gameLoop.reset();
        playerTank.reset();
        playerTank.positionCenter(WIDTH, HEIGHT);

        bullets.clear();
        explosions.clear();

        pauseMenu.setVisible(false);
        isPaused = false;

        gameLoop.start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}