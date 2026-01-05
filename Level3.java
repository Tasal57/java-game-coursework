package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Level3 is the final level where the player faces a boss enemy.
 * It includes a ground platform and a boss fight mechanic.
 */
public class Level3 extends GameLevel {

    private BossEnemy boss;  // Reference to the boss enemy

    /**
     * Constructor for Level 3.
     *
     * @param game Reference to the main Game instance
     */
    public Level3(Game game) {
        super(game);
        populate(game);  // Initialize the level
    }

    /**
     * Populate the level with ground, player, and boss enemy.
     *
     * @param game Reference to the game
     */
    @Override
    protected void populate(Game game) {
        // Initialize the player
        student = new Student(this, game);
        student.setPosition(new Vec2(0, -5));

        // Create the ground platform
        Shape groundShape = new BoxShape(30, 0.5f);
        StaticBody ground = new StaticBody(this, groundShape);
        ground.setPosition(new Vec2(0f, -11.5f));

        // Spawn the boss enemy
        boss = new BossEnemy(this, game);
        boss.setPosition(new Vec2(5, 0));

        // Add collision logic between player and boss
        student.addCollisionListener(e -> {
            if (e.getOtherBody() instanceof BossEnemy) {
                game.loseLife();  // Lose a life on contact
                game.getSoundManager().playSound("data/enemy_hit_sound.wav");  // Play sound
            }
        });
    }

    /**
     * Determines if the level is complete.
     * Completion occurs when the boss is defeated.
     *
     * @return true if the boss is defeated
     */
    @Override
    public boolean isComplete() {
        return boss != null && boss.isDefeated();
    }

    /**
     * Provides the background image for Level 3.
     *
     * @return the path to the background image
     */
    @Override
    public String getBackgroundImage() {
        return "data/level3_background.jpg";
    }
}


