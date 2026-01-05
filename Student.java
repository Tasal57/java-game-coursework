package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The Student class represents the player character in the game.
 * Handles movement, jumping, double-jumping, shooting, collision detection, and power-ups.
 */
public class Student extends Walker implements StepListener {

    // Constants for the character's shape, movement, and power-ups
    private static final Shape studentShape = new BoxShape(1.2f, 2.2f);
    private static final BodyImage imageDefault = new BodyImage("data/sonic.png", 4f);
    private static final BodyImage imageRight = new BodyImage("data/sonicRight.png", 4f);
    private static final BodyImage imageLeft = new BodyImage("data/sonicLeft.png", 4f);

    private static final float WALK_SPEED = 3f;
    private static final float RUN_SPEED = 6f;
    private static final float NORMAL_JUMP_FORCE = 140f;
    private static final float RUNNING_JUMP_FORCE = 170f;
    private static final int DOUBLE_JUMP_DURATION = 7000;  // Duration in milliseconds
    private static final Vec2 RESPAWN_POSITION = new Vec2(4, -5);

    private int credits = 0;  // Player's credits
    private boolean canDoubleJump = false;  // If double jump is available
    private boolean hasDoubleJumped = false;  // If double jump has been used
    private boolean isMoving = false;  // Whether the player is moving
    private final Game game;  // Reference to the game
    private boolean facingRight = true;  // Determines the direction the player is facing

    /**
     * Constructor for the Student class.
     * Initializes the player character, shapes, and adds step listener for collision detection.
     */
    public Student(World world, Game game) {
        super(world);
        new SolidFixture(this, studentShape);
        addImage(imageDefault);
        this.game = game;
        world.addStepListener(this);  // Adding this class as a step listener for collision detection
    }

    // Movement Controls

    /**
     * Starts walking the character based on speed and whether it's running.
     *
     * @param speed Direction and speed (positive for right, negative for left)
     * @param isRunning Whether the character is running or walking
     */
    public void startWalking(float speed, boolean isRunning) {
        float adjustedSpeed = isRunning ? RUN_SPEED : WALK_SPEED;
        setLinearVelocity(new Vec2(adjustedSpeed * Math.signum(speed), getLinearVelocity().y));
        isMoving = true;

        if (speed > 0) {
            facingRight = true;  // Moving right
        } else if (speed < 0) {
            facingRight = false; // Moving left
        }

        flipCharacter(speed < 0);
    }

    /**
     * Stops the character from moving.
     */
    public void stopWalking() {
        setLinearVelocity(new Vec2(0, getLinearVelocity().y));
        isMoving = false;
        resetToDefaultImage();
    }

    /**
     * Makes the character jump. If running, a stronger jump is applied.
     *
     * @param isRunning Whether the jump is a running jump
     */
    public void jump(boolean isRunning) {
        float jumpForce = isRunning ? RUNNING_JUMP_FORCE : NORMAL_JUMP_FORCE;
        if (getLinearVelocity().y == 0) {
            applyImpulse(new Vec2(0, jumpForce));  // Normal jump
            hasDoubleJumped = false;
        } else if (canDoubleJump && !hasDoubleJumped) {
            applyImpulse(new Vec2(0, jumpForce * 0.8f));  // Double jump
            hasDoubleJumped = true;
        }
    }

    // Character Image Handling

    /**
     * Flips the character's image based on direction.
     *
     * @param left Whether the character is facing left
     */
    private void flipCharacter(boolean left) {
        removeAllImages();
        addImage(left ? imageLeft : imageRight);
    }

    /**
     * Resets the character's image to the default when not moving.
     */
    private void resetToDefaultImage() {
        if (!isMoving) {
            removeAllImages();
            addImage(imageDefault);
        }
    }

    // Power-ups

    /**
     * Activates the double jump power-up, allowing a second jump.
     */
    public void activateDoubleJump() {
        canDoubleJump = true;
        System.out.println("Double Jump Activated!");

        // Deactivates double jump after the specified duration
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                canDoubleJump = false;
                System.out.println("Double Jump Expired.");
            }
        }, DOUBLE_JUMP_DURATION);
    }

    // Credits System

    /**
     * Gets the current number of credits.
     *
     * @return The number of credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Sets the number of credits.
     *
     * @param credits The number of credits to set
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Increments the number of credits by a given amount.
     *
     * @param points The number of points to add to credits
     */
    public void incrementCredits(int points) {
        credits += points;
    }

    // Collision and Falling Logic

    /**
     * Handles events when the character is falling below the game world.
     *
     * @param stepEvent The step event
     */
    @Override
    public void preStep(StepEvent stepEvent) {
        if (getPosition().y < -15) {
            handleFall();
        }
    }

    /**
     * Handles post-step logic (not implemented here).
     *
     * @param stepEvent The step event
     */
    @Override
    public void postStep(StepEvent stepEvent) {
        // No implementation needed
    }

    /**
     * Handles the player falling off the platform.
     */
    private void handleFall() {
        System.out.println("Student fell off the platform!");

        game.loseLife();
        System.out.println("Lives left: " + game.getLives());

        if (game.getLives() > 0) {
            setLinearVelocity(new Vec2(0, 0)); // Stop momentum before respawn
            setPosition(RESPAWN_POSITION);
            System.out.println("Respawning player...");
        } else {
            System.out.println("Game Over! Implement game over logic.");
            // Game over logic (e.g., restart game, show game over screen)
        }
    }

    // Shooting

    /**
     * Makes the player shoot a bullet in the current facing direction.
     */
    public void shoot() {
        Vec2 direction = facingRight ? new Vec2(1, 0) : new Vec2(-1, 0); // Face right or left
        new Bullet((GameLevel) getWorld(), getPosition().add(direction.mul(1.5f)), direction);  // Corrected
    }


}
