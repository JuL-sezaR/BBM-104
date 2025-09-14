import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * This class represents the Game Over Screen of the game.
 * Basically, it has everything you need to see when you explode
 * Jokes Aside, I wanted to make a custom looking game over screen,
 * So I make the background look a transparent red to add some impact
 * It handles the score update, so it never fails to put your current score at the screen
 */
public class GameOverScreen extends Pane {

    private final Label gameOverTitle;
    private final Label scoreLabel;
    private final Label instructionsLabel;

    public GameOverScreen(int width, int height) {
        setPrefSize(width, height);

        setStyle("-fx-background-color: rgba(255, 0, 0, 0.7);");

        gameOverTitle = new Label("GAME OVER");
        gameOverTitle.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: #FFFFFF;");

        instructionsLabel = new Label("Press [R] to Restart | Press [Esc] to Quit");
        instructionsLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #FFFFFF;");

        VBox vbox = new VBox(30, gameOverTitle, scoreLabel, instructionsLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setLayoutX(width / 2.0 - 200);
        vbox.setLayoutY(height / 2.0 - 150);

        getChildren().add(vbox);
        setVisible(false);
    }

    /*
    Helper function for keeping the score updated
     */
    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }
}
