package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.util.Random;

/**
 * Level2 is the second level of the game, introducing moving platforms and new enemy types.
 */
public class Level2 extends GameLevel {

    private int defeatedEnemies = 0; // Tracks how many enemies have been defeated

    /**
     * Constructor for Level 2.
     *
     * @param game The main game instance.
     */
    public Level2(Game game) {
        super(game);
        populate(game); // Populate level with objects
    }

    /**
     * Populates Level 2 with platforms, enemies, and the player.
     */
    @Override
    protected void populate(Game game) {
        // Create and position the player
        student = new Student(this, game);
        student.setPosition(new Vec2(0, -5));

        // Create the ground
        Shape groundShape = new BoxShape(30, 0.5f);
        StaticBody ground = new StaticBody(this, groundShape);
        ground.setPosition(new Vec2(0f, -11.5f));

        // Add moving platforms
        MovingPlatform platform1 = new MovingPlatform(this, new Vec2(-10, -2), new Vec2(10, -2), 0.05f);
        MovingPlatform platform2 = new MovingPlatform(this, new Vec2(10, 6), new Vec2(-10, 6), 0.05f);

        // Add enemies with new behavior for Level 2
        Level2Enemy enemy1 = new Level2Enemy(this, game);
        enemy1.setPosition(new Vec2(-20, -5));

        Level2Enemy_2 enemy2 = new Level2Enemy_2(this, game);
        enemy2.setPosition(new Vec2(8, -5));

        // Add top boundary wall
        createWalls();

        // Handle collisions between the player and the new enemy types
        student.addCollisionListener(e -> {
            if (e.getOtherBody() instanceof Level2Enemy || e.getOtherBody() instanceof Level2Enemy_2) {
                game.loseLife(); // Reduce life when hit
                game.getSoundManager().playSound("data/enemy_hit_sound.wav");
            }
        });
    }

    /**
     * Creates a top boundary wall to limit vertical movement.
     */
    private void createWalls() {
        Shape topWallShape = new BoxShape(30, 0.5f);
        StaticBody topWall = new StaticBody(this, topWallShape);
        topWall.setPosition(new Vec2(0, 20));
    }

    /**
     * Checks if the level is complete.
     *
     * @return true if the player has defeated both enemies.
     */
    @Override
    public boolean isComplete() {
        return defeatedEnemies >= 2;
    }

    /**
     * Returns the background image path for this level.
     *
     * @return background image filename
     */
    @Override
    public String getBackgroundImage() {
        return "data/level2_background.png";
    }

    /**
     * Increments the count of defeated enemies.
     */
    public void incrementDefeatedEnemies() {
        defeatedEnemies++;
    }
}
