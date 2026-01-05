package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Represents a basic bullet projectile shot by the player.
 * Extends the {@link Projectile} class for shared behavior.
 */
public class Bullet extends Projectile {

    // Shape and speed constants
    private static final Shape bulletShape = new CircleShape(0.2f); // Small circular shape
    private static final float SPEED = 10f; // Bullet speed

    /**
     * Constructs a bullet at a given position and shoots it in a direction.
     *
     * @param world     The game level the bullet exists in.
     * @param position  The starting position of the bullet.
     * @param direction The direction the bullet will travel (should be normalized).
     */
    public Bullet(GameLevel world, Vec2 position, Vec2 direction) {
        super(world, bulletShape, position, direction);

        // Set bullet initial properties
        setPosition(position);                 // Set start position
        setGravityScale(0);                   // No gravity — bullet travels straight
        setLinearVelocity(direction.mul(SPEED)); // Launch bullet
    }
}
