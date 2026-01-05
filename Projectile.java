package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Abstract base class for projectiles (e.g., bullets, fireballs).
 * Handles movement, lifespan, and collision with static bodies.
 */
public abstract class Projectile extends DynamicBody {

    private static final float SPEED = 15f;     // Projectile speed multiplier
    private static final float LIFESPAN = 3f;   // Lifespan in seconds before auto-destroy

    private final GameLevel level;

    /**
     * Constructs a new projectile.
     *
     * @param level    The level in which this projectile exists.
     * @param shape    The shape of the projectile.
     * @param position The starting position.
     * @param direction The direction the projectile will travel in (normalized vector recommended).
     */
    public Projectile(GameLevel level, Shape shape, Vec2 position, Vec2 direction) {
        super(level, shape);
        this.level = level;

        setPosition(position);
        setGravityScale(0);  // No gravity so it flies straight
        setLinearVelocity(direction.mul(SPEED));  // Launch in given direction

        // Handle projectile lifespan
        level.addStepListener(new StepListener() {
            private float timeElapsed = 0;

            @Override
            public void preStep(StepEvent e) {
                // Not needed
            }

            @Override
            public void postStep(StepEvent e) {
                timeElapsed += e.getStep();  // Accumulate time
                if (timeElapsed > LIFESPAN) {
                    destroy();               // Auto-destroy after lifespan
                    level.removeStepListener(this); // Clean up listener
                }
            }
        });

        // Destroy projectile if it hits a wall or static object
        addCollisionListener(e -> {
            if (e.getOtherBody() instanceof StaticBody) {
                destroy();
            }
        });
    }
}
