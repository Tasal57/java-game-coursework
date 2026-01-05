package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * A MovingPlatform is a static body that simulates movement between two positions
 * by manually updating its position every simulation step.
 */
public class MovingPlatform extends StaticBody {

    // Define the shape of the platform (a simple rectangle)
    private static final Shape platformShape = new BoxShape(3f, 0.5f);

    private final Vec2 startPos;     // Starting point of the platform
    private final Vec2 endPos;       // Ending point of the platform
    private boolean movingToEnd = true;  // Direction flag
    private float speed;             // Speed of movement

    /**
     * Constructs a moving platform between two points.
     *
     * @param world    the physics world the platform belongs to
     * @param startPos the initial position
     * @param endPos   the target position to move toward
     * @param speed    the movement speed
     */
    public MovingPlatform(GameLevel world, Vec2 startPos, Vec2 endPos, float speed) {
        super(world, platformShape);
        this.startPos = startPos;
        this.endPos = endPos;
        this.speed = speed;

        // Set the initial position of the platform
        this.setPosition(startPos);

        // Add step listener to move the platform on each simulation step
        world.addStepListener(new StepListener() {
            @Override
            public void preStep(StepEvent e) {
                move();  // Move the platform before each simulation step
            }

            @Override
            public void postStep(StepEvent e) {
                // No action needed after the step
            }
        });
    }

    /**
     * Updates the platform's position, moving it between start and end points.
     */
    private void move() {
        Vec2 currentPos = getPosition();
        Vec2 targetPos = movingToEnd ? endPos : startPos;

        // Calculate direction vector to the target position
        Vec2 direction = targetPos.sub(currentPos);
        float distance = direction.length();

        // If the platform is close enough to the target, reverse direction
        if (distance < 0.1f) {
            movingToEnd = !movingToEnd;  // Toggle direction
        } else {
            direction.normalize();  // Normalize to get unit vector
            this.setPosition(currentPos.add(direction.mul(speed)));  // Move toward target
        }
    }
}
