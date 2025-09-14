import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * This class represents the Game Management object, which extends AnimationTimer
 * It basically handles every operation which has to be done, like firing bullets, spawning enemies, making explosions, etc.
 * I added an extra feature here, enemy tanks get spawned at four places first, then there is an eight location 2D array for them to choose
 * For example if player tank is close by 150 pixels of distance that location will get cancelled out so there won't be any enemy tanks spawning right next to the player
 * I used iterator for every operation, like bullets, explosions, etc.
 * This class might be considered to be the brain of the game
 */
public class GameManagement extends AnimationTimer {

    private final Tank playerTank;
    private final List<ImageView> walls;
    private final Pane root;
    private final List<Bullet> playerBullets;
    private final List<Bullet> enemyBullets;
    private final List<Explosion> explosions;
    private final List<EnemyTank> enemyTanks;
    private final GameUI gameUI;

    private long lastSpawnTime = 0;
    private final long minSpawnInterval = 3_000_000_000L;
    private final long maxSpawnInterval = 8_000_000_000L;
    private long nextSpawnTime;
    private java.util.Random random = new java.util.Random();
    private final int maxEnemyTanks = 8;


    private boolean running = false;


    public GameManagement(Tank tank, List<ImageView> walls, Pane root, List<Bullet> playerBullets, List<Explosion> explosions) {
        this.playerTank = tank;
        this.walls = walls;
        this.root = root;
        this.playerBullets = playerBullets;
        this.enemyBullets = new ArrayList<>();
        this.explosions = explosions;
        this.enemyTanks = new ArrayList<>();

        gameUI = new GameUI();
        root.getChildren().add(gameUI.getUI());
        gameUI.updateLives(playerTank.getLives());

        createEnemyTanks();
        setNextSpawnTime();
    }

    /*
    This method creates enemy tanks, just like I explained before with the logic of no nearby enemies for the player
     */
    private void createEnemyTanks() {
        // Create 3 enemy tanks at different positions
        EnemyTank enemy1 = new EnemyTank(100, 100);
        EnemyTank enemy2 = new EnemyTank(1100, 100);
        EnemyTank enemy3 = new EnemyTank(100, 600);
        EnemyTank enemy4 = new EnemyTank(640, 100);

        enemyTanks.add(enemy1);
        enemyTanks.add(enemy2);
        enemyTanks.add(enemy3);
        enemyTanks.add(enemy4);

        // Add enemy tanks to scene
        for (EnemyTank enemy : enemyTanks) {
            root.getChildren().add(enemy.getTank());
        }
    }

    /*
    Helper function for spawning tanks
     */
    private void setNextSpawnTime() {
        long randomInterval = minSpawnInterval + (long)(random.nextDouble() * (maxSpawnInterval - minSpawnInterval));
        nextSpawnTime = System.nanoTime() + randomInterval;
    }

    /*
    Main method for spawning enemy tanks
     */
    private void spawnEnemyTank(long now) {
        if (enemyTanks.size() >= maxEnemyTanks) return;

        double[][] spawnPositions = {
                {100, 100},
                {1100, 100},
                {100, 600},
                {1100, 600},
                {640, 50},
                {50, 360},
                {1200, 360},
                {640, 650},
        };

        int RandomIndex = random.nextInt(spawnPositions.length);
        double spawnX = spawnPositions[RandomIndex][0];
        double spawnY = spawnPositions[RandomIndex][1];

        boolean validPosition = true;
        for (EnemyTank existing : enemyTanks) {
            double distance = Math.sqrt(
                    Math.pow(existing.getTank().getX() - spawnX, 2) + Math.pow(existing.getTank().getY() - spawnY, 2)
            );
            if (distance < 150) {
                validPosition = false;
                break;
            }
        }

        double playerDistance = Math.sqrt(
                Math.pow(playerTank.getTank().getX() - spawnX, 2) + Math.pow(playerTank.getTank().getY() - spawnY, 2)
        );
        if (validPosition) {
            EnemyTank newEnemy = new EnemyTank(spawnX, spawnY);
            enemyTanks.add(newEnemy);
            root.getChildren().add(newEnemy.getTank());
        }

        setNextSpawnTime();
    }

    /*
    Main method for updating the remaining lives of the player at the left corner UI
     */
    private void updatePlayerUI() {
        gameUI.updateLives(playerTank.getLives());
    }

    /*
    This is the main of all main methods. It literally handles the whole mechanics of the game
     */
    @Override
    public void handle(long now) {
        playerTank.updatePosition(now, walls);


        if (now >= nextSpawnTime) {
            spawnEnemyTank(now);
        }

        for (EnemyTank enemy : enemyTanks) {
            enemy.updatePosition(now, walls);

            if (enemy.shouldShoot(now)) {
                double enemyX = enemy.getCenterX();
                double enemyY = enemy.getCenterY();
                double angle = enemy.getRotationAngle();

                Bullet enemyBullet = new Bullet(enemyX, enemyY, angle);
                enemyBullets.add(enemyBullet);
                root.getChildren().add(enemyBullet.getImageView());
            }
        }

        if(!playerTank.exploded()){
            Iterator<EnemyTank> enemyCollisionIterator = enemyTanks.iterator();
            while (enemyCollisionIterator.hasNext()) {
                EnemyTank enemy = enemyCollisionIterator.next();

                if(enemy.isAlive() &&
                        playerTank.getTank().getBoundsInParent().intersects(enemy.getTank().getBoundsInParent())) {

                    double playerExplosionX = playerTank.getCenterX();
                    double playerExplosionY = playerTank.getCenterY();
                    Explosion playerExplosion = new Explosion(playerExplosionX, playerExplosionY, Explosion.ExplosionType.NORMAL);
                    explosions.add(playerExplosion);
                    root.getChildren().add(playerExplosion.getImageView());

                    double enemyExplosionX = enemy.getCenterX();
                    double enemyExplosionY = enemy.getCenterY();
                    Explosion enemyExplosion = new Explosion(enemyExplosionX, enemyExplosionY, Explosion.ExplosionType.NORMAL);
                    explosions.add(enemyExplosion);
                    root.getChildren().add(enemyExplosion.getImageView());

                    playerTank.takeDamage(now);
                    updatePlayerUI();

                    enemy.destroy();

                    root.getChildren().remove(enemy.getTank());
                    enemyCollisionIterator.remove();

                    break;

                }
            }

        }

        Iterator<Bullet> playerBulletIterator = playerBullets.iterator();
        while (playerBulletIterator.hasNext()) {
            Bullet bullet = playerBulletIterator.next();
            bullet.updatePosition();

            boolean bulletRemoved = false;

            Iterator<EnemyTank> enemyIterator = enemyTanks.iterator();
            while (enemyIterator.hasNext() && !bulletRemoved) {
                EnemyTank enemy = enemyIterator.next();
                if (enemy.isAlive() && enemy.getTank().getBoundsInParent().intersects(bullet.getImageView().getBoundsInParent())) {

                    double explosionX = enemy.getCenterX();
                    double explosionY = enemy.getCenterY();

                    Explosion explosion = new Explosion(explosionX, explosionY, Explosion.ExplosionType.NORMAL);
                    explosions.add(explosion);
                    root.getChildren().add(explosion.getImageView());
                    gameUI.addScore(1);

                    root.getChildren().remove(enemy.getTank());
                    root.getChildren().remove(bullet.getImageView());
                    enemyIterator.remove();
                    playerBulletIterator.remove();

                    bulletRemoved = true;
                    break;
                }
            }

            if (!bulletRemoved) {
                boolean hitWall = false;
                for (ImageView wall : walls) {
                    if (wall.getBoundsInParent().intersects(bullet.getImageView().getBoundsInParent())) {
                        double explosionX = bullet.getX() + bullet.getImageView().getFitWidth() / 2;
                        double explosionY = bullet.getY() + bullet.getImageView().getFitHeight() / 2;

                        Explosion explosion = new Explosion(explosionX, explosionY, Explosion.ExplosionType.SMALL);
                        explosions.add(explosion);
                        root.getChildren().add(explosion.getImageView());

                        hitWall = true;
                        break;
                    }
                }
                if (hitWall || bullet.isOffScreen(root.getWidth(), root.getHeight())) {
                    root.getChildren().remove(bullet.getImageView());
                    playerBulletIterator.remove();
                }
            }
        }


        Iterator<Bullet> enemyBulletIterator = enemyBullets.iterator();
        while (enemyBulletIterator.hasNext()) {
            Bullet bullet = enemyBulletIterator.next();
            bullet.updatePosition();

            boolean bulletRemoved = false;

            if (!playerTank.exploded() && playerTank.getTank().getBoundsInParent().intersects(bullet.getImageView().getBoundsInParent())) {
                double explosionX = bullet.getX() + bullet.getImageView().getFitWidth() / 2;
                double explosionY = bullet.getY() + bullet.getImageView().getFitHeight() / 2;

                Explosion explosion = new Explosion(explosionX, explosionY, Explosion.ExplosionType.NORMAL);
                explosions.add(explosion);
                root.getChildren().add(explosion.getImageView()
                );
                playerTank.takeDamage(now);
                updatePlayerUI();
                root.getChildren().remove(bullet.getImageView());
                enemyBulletIterator.remove();
                bulletRemoved = true;
            }

            if (!bulletRemoved) {
                boolean hitWall = false;
                for (ImageView wall : walls) {
                    if (wall.getBoundsInParent().intersects(bullet.getImageView().getBoundsInParent())) {
                        double explosionX = bullet.getX() + bullet.getImageView().getFitWidth() / 2;
                        double explosionY = bullet.getY() + bullet.getImageView().getFitHeight() / 2;

                        Explosion explosion = new Explosion(explosionX, explosionY, Explosion.ExplosionType.SMALL);
                        explosions.add(explosion);
                        root.getChildren().add(explosion.getImageView());

                        hitWall = true;
                        break;
                    }
                }
                if (hitWall || bullet.isOffScreen(root.getWidth(), root.getHeight())) {
                    root.getChildren().remove(bullet.getImageView());
                    enemyBulletIterator.remove();
                }
            }
        }

        Iterator<Explosion> explosionIterator = explosions.iterator();
        while (explosionIterator.hasNext()) {
            Explosion explosion = explosionIterator.next();
            explosion.update(now);

            if (explosion.exploded()) {
                root.getChildren().remove(explosion.getImageView());
                explosionIterator.remove();
            }
        }
    }

    /*
    This method is for starting the AnimationTimer
     */
    @Override
    public void start() {
        super.start();
        running = true;
    }

    /*
    This method is a helper for pausing the game
     */
    public void pause() {
        super.stop();
        running = false;
    }

    /*
    This method is a helper for resuming the game after the pause
     */
    public void resume() {
        if (!running) {
            super.start();
            running = true;
        }
    }

    /*
    Another helper method for resetting the game when pressed "R"
     */
    public void reset() {

        for (Bullet bullet : playerBullets) {
            root.getChildren().remove(bullet.getImageView());
        }
        playerBullets.clear();

        for ( Bullet bullet : enemyBullets) {
            root.getChildren().remove(bullet.getImageView());
        }
        enemyBullets.clear();

        for (Explosion explosion : explosions) {
            root.getChildren().remove(explosion.getImageView());
        }
        explosions.clear();

        for (EnemyTank enemyTank : enemyTanks) {
            root.getChildren().remove(enemyTank.getTank());
        }
        enemyTanks.clear();

        gameUI.reset();

        createEnemyTanks();
    }

    /*
    Getter for the GameUI
     */
    public GameUI getGameUI() {
        return gameUI;
    }

    /*
    Helper method to understand if the player has less or equal lives to 0
     */
    public boolean isGameOver() {
        return playerTank.getLives() <= 0;
    }
}
