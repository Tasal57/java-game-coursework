package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Level2Enemy_2 is a secondary enemy type in Level 2.
 * It follows the player horizontally and avoids overlapping with other enemies.
 */
public class Level2Enemy_2 extends Walker implements StepListener, CollisionListener {

    // Shape: taller box-shaped enemy
    private static final Shape newEnemyShape = new BoxShape(1.5f, 2.5f);

    // Appearance: enemy sprite/image
    private static final BodyImage newEnemyImage = new BodyImage("data/level2_enemy2.png", 6.0f);

    private int health = 3;        // Health points
    private float speed = 1.5f;    // Movement speed
    private Game game;             // Reference to game

    /**
     * Constructor to initialize the enemy.
     *
     * @param world The current game level
     * @param game  The main game instance
     */
    public Level2Enemy_2(GameLevel world, Game game) {
        super(world, newEnemyShape);
        this.game = game;

        addImage(newEnemyImage);         // Add the enemy image
        addCollisionListener(this);      // Listen for bullet collisions
        world.addStepListener(this);     // Add to the world update cycle
    }

    /**
     * Called before each world update step.
     * The enemy moves toward the player and keeps distance from other enemies.
     */
    @Override
    public void preStep(StepEvent e) {
        Student student = ((GameLevel) getWorld()).getStudent();

        if (student != null) {
            Vec2 studentPosition = student.getPosition();
            Vec2 enemyPosition = getPosition();

            // Get direction vector toward the player
            Vec2 direction = studentPosition.sub(enemyPosition);

            // Normalize X axis only (for horizontal movement)
            float lengthX = Math.abs(direction.x);
            if (lengthX != 0) {
                direction.x /= lengthX;
            }

            // Base movement toward player
            Vec2 targetVelocity = new Vec2(direction.x * speed, getLinearVelocity().y);

            // Separation logic to avoid clustering with other enemies
            for (Body b : getWorld().getDynamicBodies()) {
                if ((b instanceof Level2Enemy || b instanceof Level2Enemy_2) && b != this) {
                    Vec2 otherEnemyPos = b.getPosition();
                    Vec2 separationVector = enemyPosition.sub(otherEnemyPos);
                    float distance = separationVector.length();

                    if (distance < 4.0f && distance > 0) {
                        separationVector.y = 0; // Ignore vertical difference
                        separationVector.normalize();

                        float repulsionStrength = 8.0f / distance;
                        separationVector = separationVector.mul(repulsionStrength);

                        // Push away horizontally from nearby enemies
                        targetVelocity.addLocal(new Vec2(separationVector.x, 0));
                    }
                }
            }

            // Apply the calculated movement
            setLinearVelocity(targetVelocity);
        }
    }

    /**
     * No post-step actions required for this enemy.
     */
    @Override
    public void postStep(StepEvent e) {}

    /**
     * Handles collision events with bullets.
     *
     * @param e The collision event
     */
    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Bullet) {
            takeDamage();               // Take damage on hit
            e.getOtherBody().destroy(); // Destroy the bullet
        }
    }

    /**
     * Reduces health and destroys the enemy when health reaches zero.
     */
    public void takeDamage() {
        health--;
        if (health <= 0) {
            if (getWorld() instanceof Level2) {
                ((Level2) getWorld()).incrementDefeatedEnemies(); // Notify level
            }
            destroy(); // Enemy dies
        }
    }
}

