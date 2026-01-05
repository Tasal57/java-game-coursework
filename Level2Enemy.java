package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Level2Enemy is a more advanced enemy that follows the player horizontally
 * and includes basic separation behavior from other enemies.
 */
public class Level2Enemy extends Walker implements StepListener, CollisionListener {

    // Define enemy's shape for physics and collisions
    private static final Shape enemyShape = new PolygonShape(
            -1.2f, 1.5f,   1.2f, 1.5f,   1.5f, 0.75f,
            1.5f, -1.5f,   -1.5f, -1.5f,   -1.5f, 0.75f
    );

    // Load image for enemy appearance
    private static final BodyImage enemyImage = new BodyImage("data/level2_enemy.png", 6.0f);

    private int health = 5;          // Enemy's hit points
    private float speed = 2.0f;      // Horizontal speed
    private Game game;              // Reference to main game object

    /**
     * Constructor that initializes the enemy in the level.
     *
     * @param world the current game level
     * @param game  the main game logic object
     */
    public Level2Enemy(GameLevel world, Game game) {
        super(world, enemyShape);
        this.game = game;

        addImage(enemyImage);             // Add enemy's visual appearance
        addCollisionListener(this);       // Listen for collisions (e.g., bullets)
        world.addStepListener(this);      // Add to world update cycle
    }

    /**
     * Called before each simulation step to update enemy behavior.
     * Enemy moves toward the player and avoids overlap with other enemies.
     */
    @Override
    public void preStep(StepEvent e) {
        Student student = ((GameLevel) getWorld()).getStudent();

        if (student != null) {
            Vec2 studentPosition = student.getPosition();
            Vec2 enemyPosition = getPosition();

            // Calculate direction vector toward the player
            Vec2 direction = studentPosition.sub(enemyPosition);

            // Normalize only the X component (to restrict movement to horizontal axis)
            float lengthX = Math.abs(direction.x);
            if (lengthX != 0) {
                direction.x /= lengthX;
            }

            // Start with target velocity towards player
            Vec2 targetVelocity = new Vec2(direction.x * speed, getLinearVelocity().y);

            // Separation logic: avoid clustering with other Level2 enemies
            for (Body b : getWorld().getDynamicBodies()) {
                if ((b instanceof Level2Enemy || b instanceof Level2Enemy_2) && b != this) {
                    Vec2 otherEnemyPos = b.getPosition();
                    Vec2 separationVector = enemyPosition.sub(otherEnemyPos);
                    float distance = separationVector.length();

                    if (distance < 4.0f && distance > 0) {
                        separationVector.normalize();
                        float repulsionStrength = 8.0f / distance;
                        separationVector = separationVector.mul(repulsionStrength);

                        // Adjust only horizontal component to avoid vertical jumping
                        targetVelocity.addLocal(new Vec2(separationVector.x, 0));
                    }
                }
            }

            // Apply final movement
            setLinearVelocity(targetVelocity);
        }
    }

    @Override
    public void postStep(StepEvent e) {
        // No post-step logic required for this enemy
    }

    /**
     * Handles collision events, particularly when hit by a bullet.
     *
     * @param e the collision event
     */
    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Bullet) {
            takeDamage();            // Take damage from bullet
            e.getOtherBody().destroy(); // Destroy the bullet on contact
        }
    }

    /**
     * Reduces health and handles destruction when health reaches zero.
     */
    public void takeDamage() {
        health--;
        if (health <= 0) {
            if (getWorld() instanceof Level2) {
                ((Level2) getWorld()).incrementDefeatedEnemies(); // Inform level of defeat
            }
            destroy(); // Remove enemy from world
        }
    }
}
