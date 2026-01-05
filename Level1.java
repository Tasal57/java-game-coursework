package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.util.*;

/**
 * Level1 represents the first level of the game, including platforms, enemies, and collectibles.
 */
public class Level1 extends GameLevel {

    private boolean collectedItem = false;
    private Timer timer; // Timer to periodically spawn collectibles
    private Enemy enemy1, enemy2; // Two enemy characters in the level

    /**
     * Constructor to set up Level 1.
     *
     * @param game The main game instance.
     */
    public Level1(Game game) {
        super(game);

        populate(game);              // Set up objects and entities
        startCollectibleSpawner();   // Begin periodic collectible spawning
    }

    /**
     * Populates the level with platforms, player, enemies, and collectibles.
     */
    @Override
    protected void populate(Game game) {

        // Create the player if not already initialized
        if (student == null) {
            student = new Student(this, game);
        }
        student.setPosition(new Vec2(4, -5));

        // Ground setup
        Shape groundShape = new BoxShape(30, 0.5f);
        StaticBody ground = new StaticBody(this, groundShape);
        ground.setPosition(new Vec2(0f, -11.5f));

        // Create and place platforms at random positions
        Random rand = new Random();
        List<Vec2> platformPositions = new ArrayList<>();
        int maxAttempts = 100;
        int platformCount = 4;

        for (int i = 0; i < platformCount; i++) {
            int attempts = 0;
            while (attempts < maxAttempts) {
                float x = rand.nextFloat() * 40 - 30;  // X range: -10 to 10
                float y = rand.nextFloat() * 20 - 5;   // Y range: -5 to 5
                float width = rand.nextFloat() * 2 + 2;

                Vec2 newPos = new Vec2(x, y);
                boolean overlap = false;

                for (Vec2 pos : platformPositions) {
                    if (pos.sub(newPos).length() < 6f) {
                        overlap = true;
                        break;
                    }
                }

                if (!overlap) {
                    Shape platformShape = new BoxShape(width, 0.5f);
                    StaticBody platform = new StaticBody(this, platformShape);
                    platform.setPosition(newPos);
                    platformPositions.add(newPos);
                    break; // Stop checking once platform is placed
                }

                attempts++;
            }
        }

        // Add enemies
        enemy1 = new Enemy(this);
        enemy1.setPosition(new Vec2(-5, -5));

        enemy2 = new Enemy(this);
        enemy2.setPosition(new Vec2(8, -5));

        // Spawn the initial collectible
        spawnCollectible();

        // Add boundaries to the world
        createWalls();

        // Collision listeners for the player
        student.addCollisionListener(e -> {
            if (e.getOtherBody() instanceof Collectible) {
                handleCollectibleCollection((Collectible) e.getOtherBody());
            }
            if (e.getOtherBody() instanceof Enemy) {
                handleEnemyCollision();
            }
        });
    }

    /**
     * Creates a top wall to restrict the player's movement vertically.
     */
    private void createWalls() {
        Shape topWallShape = new BoxShape(30, 0.5f);
        StaticBody topWall = new StaticBody(this, topWallShape);
        topWall.setPosition(new Vec2(0, 20)); // Positioned above visible play area
    }

    /**
     * Spawns a collectible item at a random position on the screen.
     */
    public void spawnCollectible() {
        Random rand = new Random();
        float x = rand.nextFloat() * 20 - 10;
        float y = rand.nextFloat() * 10 - 5;

        Collectible collectible = new Collectible(this);
        collectible.setPosition(new Vec2(x, y));
    }

    /**
     * Starts a timer to spawn collectibles every 15 seconds.
     */
    private void startCollectibleSpawner() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                spawnCollectible();
            }
        }, 0, 15000);
    }

    /**
     * Handles what happens when the player collects a collectible.
     *
     * @param collectible The collectible object collected.
     */
    private void handleCollectibleCollection(Collectible collectible) {
        student.incrementCredits(10);     // Give points
        student.activateDoubleJump();     // Grant power-up
        collectedItem = true;             // Mark item as collected
        collectible.destroy();            // Remove from world
    }

    /**
     * Called when the player collides with an enemy.
     */
    private void handleEnemyCollision() {
        getGame().loseLife(); // Decrease player lives
    }

    /**
     * Determines if the level is complete.
     *
     * @return true if the student has earned enough credits.
     */
    @Override
    public boolean isComplete() {
        return student.getCredits() >= 20;
    }

    /**
     * Called when the level stops, used to clean up resources like timers.
     */
    @Override
    public void stop() {
        super.stop();
        if (timer != null) {
            timer.cancel(); // Stop the collectible spawn timer
        }
    }

    /**
     * Returns the background image path for this level.
     *
     * @return file path to the background image.
     */
    @Override
    public String getBackgroundImage() {
        return "data/Background.jpg";
    }
}
