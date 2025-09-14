import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.List;
import java.util.ArrayList;


/**
 * This class represents the background scene object which is a child-class of TankGame
 * It is basically the map of the game which I designed for the player to get the extreme fun out of
 * It has a small part like a labyrinth, but it also makes the player deal with both the distance and mobility
 * For loops have been used to make the walls
 */
public class BackgroundScene extends Main {

    private final int width = 1280;
    private final int height = 720;
    private final int wallsize = 16;

    private final List<ImageView> walls = new ArrayList<>();

    public Scene createScene() {

        Image wallImage = new Image(getClass().getResource("/assets/wall.png").toExternalForm());

        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        // Top and Bottom borders
        for (int x = 0; x < width; x += wallsize) {
            addWall(root, wallImage, x, 0);
            addWall(root, wallImage, x , height - wallsize);

        }

        // Left and right borders
        for (int y = 0; y < height; y += wallsize) {
            addWall(root, wallImage, 0, y);
            addWall(root, wallImage, width - wallsize, y);

        }

        // Vertical Wall #1
        for (int z = 100; z < 600; z += wallsize) {
            for (int offsetX = 0; offsetX < wallsize; offsetX += wallsize) {
                addWall(root, wallImage, 300 + offsetX, z);
            }

        }

        // Vertical Wall #2
        for (int v = 0; v < 600; v += wallsize) {
            addWall(root, wallImage, 800, v);
        }

        // Vertical Wall #3
        for(int x = 576; x < 704;x += wallsize) {
            addWall(root, wallImage, 992, x);
        }

        // Horizontal Wall #1
        for (int c = 400; c < 800; c += wallsize) {
            addWall(root, wallImage, c, 480);
        }

        // Horizontal Wall #2
        for (int a = 800; a < 1120; a += wallsize) {
            addWall(root, wallImage, a, 320);
            addWall(root, wallImage, a, 320 + wallsize);
        }

        return new Scene(root, width, height);

    }

    /*
    This method helps with the addition of the walls into the List
     */
    private void addWall(Pane root, Image wallImage, int x, int y) {
        ImageView wall = new ImageView(wallImage);
        wall.setFitWidth(wallsize);
        wall.setFitHeight(wallsize);
        wall.setX(x);
        wall.setY(y);
        root.getChildren().add(wall);
        walls.add(wall);
    }

    /*
    Getter of the walls List
     */
    public List<ImageView> getWalls() {
        return walls;
    }

}
