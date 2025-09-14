import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This class represents my gameUI object, which is used for making the tiny scoreboard for the players to see their scores and lives
 * It clearly handles any updates like when a player dies, removes a life and if the player takes down an enemy tank it updates player's score
 * I wanted my scoreboard to look not so dangerous so that's why I choose the color green for it
 */
public class GameUI {
    private VBox uiContainer;
    private Label scoreLabel;
    private HBox livesContainer;
    private int currentScore = 0;

    public GameUI() {
        setupUI();
    }

    private void setupUI() {
        uiContainer = new VBox(10);
        uiContainer.setLayoutX(20);
        uiContainer.setLayoutY(20);

        scoreLabel = new Label("Score: " + currentScore);
        scoreLabel.setTextFill(Color.GREEN);
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        scoreLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding:5px; -fx-background-radius: 5px");

        livesContainer = new HBox(5);
        Label livesLabel = new Label("Lives: ");
        livesLabel.setTextFill(Color.GREEN);
        livesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        livesLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding:5px; -fx-background-radius: 5px");
        uiContainer.getChildren().addAll(scoreLabel, livesContainer);
    }

    public VBox getUI() {
        return uiContainer;
    }

    /*
    This method handles updating player's lives perfectly
     */
    public void updateLives(int lives) {
        livesContainer.getChildren().clear();

        Label livesLabel = new Label("Lives: ");
        livesLabel.setTextFill(Color.GREEN);
        livesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        livesLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding:5px; -fx-background-radius: 5px");

        Label livesCountLabel = new Label(String.valueOf(lives));
        livesCountLabel.setTextFill(Color.GREEN);
        livesCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        livesCountLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding:5px; -fx-background-radius: 5px");
        livesContainer.getChildren().addAll(livesLabel, livesCountLabel);

    }

    /*
    This method handles updating player's score perfectly
     */
    public void updateScore(int score) {
        currentScore = score;
        scoreLabel.setText("Score: " + score);
    }

    /*
    This method is a helper for updating the score
     */
    public void addScore(int points) {
        currentScore += points;
        updateScore(currentScore);
    }

    /*
    Another helper method for getting the score
     */
    public int getScore() {
        return currentScore;
    }

    /*
    This helper method is for the restart/reset command when pressed "R"
     */
    public void reset() {
        currentScore = 0;
        updateScore(0);
        updateLives(3);
    }

}
