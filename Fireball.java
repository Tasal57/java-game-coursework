package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * A fireball projectile fired by the boss enemy.
 * It moves in a fixed direction and causes damage to the player on collision.
 */
public class Fireball extends DynamicBody implements CollisionListener {

    private static final Shape fireballShape = new CircleShape(0.5f); // Size of fireball
    private static final BodyImage fireballImage = new BodyImage("data/fireball.png", 2.5f); // Visual representation

    private final Game game; // Reference to the main game to reduce life on hit

    /**
     * Constructs a fireball projectile.
     *
     * @param world         The current game level.
     * @param startPosition The starting position of the fireball.
     * @param direction     The direction the fireball travels in.
     * @param game          Reference to the game (for calling game-related methods like loseLife).
     */
    public Fireball(GameLevel world, Vec2 startPosition, Vec2 direction, Game game) {
        super(world, fireballShape);
        this.game = game;

        setPosition(startPosition);
        addImage(fireballImage);

        // Set velocity toward target direction
        setLinearVelocity(direction.mul(20)); // Speed multiplier

        addCollisionListener(this);
    }

    /**
     * Handles collision logic for the fireball.
     * It damages the player or disappears when hitting walls or other enemies.
     *
     * @param e The collision event.
     */
    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Student) {
            game.loseLife();  // Player takes damage
            destroy();        // Fireball vanishes on impact
        } else if (e.getOtherBody() instanceof StaticBody) {
            destroy();        // Disappears when hitting environment
        } else if (e.getOtherBody() instanceof Walker && !(e.getOtherBody() instanceof BossEnemy)) {
            destroy();        // Avoid hitting the boss itself or other walkers
        }
    }
}
