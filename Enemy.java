package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Enemy class representing an AI-controlled enemy character.
 * The enemy moves back and forth automatically and occasionally jumps.
 * It also changes appearance when colliding with the player.
 */
public class Enemy extends Walker implements StepListener, CollisionListener {

    // Define the shape and image of the enemy
    private static final Shape enemyShape = new BoxShape(1, 2);
    private static final BodyImage enemyImage = new BodyImage("data/enemy.png", 4f);

    private float moveSpeed = 2f; // Movement speed of the enemy
    private boolean movingRight = true;  // Flag to track movement direction
    private float jumpForce = 10f;  // Jump force for the enemy
    private SoundManager soundManager;

    /**
     * Constructor: Creates an enemy in the given world.
     * @param world The game world where the enemy exists.
     */
    public Enemy(World world) {
        super(world);
        this.soundManager = new SoundManager();
        new SolidFixture(this, enemyShape);
        addImage(enemyImage);

        // Register the Enemy as a StepListener to enable movement logic
        world.addStepListener(this);

        // Register as a CollisionListener to detect interactions with other objects
        addCollisionListener(this);
    }

    /**
     * Changes the appearance of the enemy.
     * This is triggered when the enemy collides with the player.
     */
    public void changeApperance() {
        removeAllImages();
        addImage(new BodyImage("data/enemy2.png", 4f)); // Change to a different enemy sprite
        soundManager.playSound("data/enemy_hit_sound.wav");
    }

    /**
     * Handles enemy movement logic.
     * The enemy moves left and right within set boundaries and jumps randomly.
     */
    public void move() {
        // Get the current position
        Vec2 position = getPosition();

        // Change direction if the enemy moves too far in one direction
        if (position.x > 10) {
            movingRight = false;  // Move left
        } else if (position.x < -10) {
            movingRight = true;  // Move right
        }

        // Apply movement velocity depending on the direction
        if (movingRight) {
            setLinearVelocity(new Vec2(moveSpeed, getLinearVelocity().y));
        } else {
            setLinearVelocity(new Vec2(-moveSpeed, getLinearVelocity().y));
        }

        // Randomly jump every few steps (1% chance per step)
        if (Math.random() < 0.01) {
            jump();
        }
    }

    /**
     * Makes the enemy jump by applying vertical velocity.
     */
    public void jump() {
        setLinearVelocity(new Vec2(getLinearVelocity().x, jumpForce));
    }

    /**
     * Handles collision events with other bodies.
     * @param e The collision event.
     */
    @Override
    public void collide(CollisionEvent e) {
        // If the enemy collides with a static object (e.g., walls), it changes direction
        if (e.getOtherBody() instanceof StaticBody) {
            movingRight = !movingRight;
        }

        // If the enemy collides with the player, it changes appearance
        if (e.getOtherBody() instanceof Student) {
            System.out.println("Enemy hit by the student!");
            changeApperance();
        }
    }

    /**
     * Called before each physics step.
     * Moves the enemy every step.
     * @param e The step event.
     */
    @Override
    public void preStep(StepEvent e) {
        move();
    }

    /**
     * Called after each physics step.
     * @param e The step event.
     */
    @Override
    public void postStep(StepEvent e) {
        // no post-step logic needed.
    }

}
