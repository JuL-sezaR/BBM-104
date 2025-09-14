import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

public class PauseMenu extends Pane {
    private final Label pauseTitle;
    private final Label scoreLabel;
    private final Label livesLabel;
    private final Label instructionsLabel;

    public PauseMenu(int width, int height) {
        setPrefSize(width, height);
        setStyle("-fx-background-color: rgba(0,0,0,0.7);");

        pauseTitle = new Label("PAUSED");
        pauseTitle.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #00FF00;");

        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #00FF00;");

        livesLabel = new Label("Lives: 3");
        livesLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #00FF00;");

        instructionsLabel = new Label("Press [R] to Restart | Press [Esc] to Quit");
        instructionsLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #00FF00;");

        VBox vbox = new VBox(30, pauseTitle, scoreLabel, livesLabel, instructionsLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setLayoutX(width / 2.0 - 200);
        vbox.setLayoutY(height / 2.0 - 150);

        getChildren().add(vbox);
        setVisible(false);
    }

    public void update(int score, int lives) {
        scoreLabel.setText("Score: " + score);
        livesLabel.setText("Lives: " + lives);
    }
}
