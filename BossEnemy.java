package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Represents the boss enemy in the game.
 * This enemy chases the player and periodically shoots fireballs.
 */
public class BossEnemy extends Walker implements StepListener, CollisionListener {

    // Static image and shape shared by all BossEnemy instances
    private static final BodyImage bossImage = new BodyImage("data/bossEnemy.png", 8f);
    private static final Shape bossShape = new PolygonShape(
            -2.5f, 2.0f,  2.5f, 2.0f,  2.5f, -3.0f,  -2.5f, -3.0f
    );

    private int health = 5;                  // Boss health
    private float speed = 2.0f;              // Movement speed
    private int stepsSinceLastShot = 0;      // Counter to regulate fireball shooting
    private final int shootIntervalSteps = 180; // Steps between each fireball attack. 60fp per second so 3seconds total

    private final Game game;                 // Reference to the main Game

    /**
     * Constructs the BossEnemy in the given level and attaches listeners.
     * @param world The game level (world) this boss exists in.
     * @param game Reference to the main game instance.
     */
    public BossEnemy(GameLevel world, Game game) {
        super(world, bossShape);
        this.game = game;

        addImage(bossImage);               // Set visual appearance
        addCollisionListener(this);       // Handle collision with bullets
        world.addStepListener(this);      // Enable step-based behavior (movement/shooting)
    }

    /**
     * Called before each physics step.
     * Handles chasing the player and shooting fireballs.
     */
    @Override
    public void preStep(StepEvent e) {
        Student student = ((GameLevel) getWorld()).getStudent();
        if (student != null) {
            // Calculate direction vector toward the player
            Vec2 direction = student.getPosition().sub(getPosition());
            float distance = direction.length();

            if (distance > 0) {
                direction.normalize();
                Vec2 targetVelocity = direction.mul(speed);
                setLinearVelocity(targetVelocity); // Move toward the player
            }
        }

        // Fireball shooting logic
        stepsSinceLastShot++;
        if (stepsSinceLastShot >= shootIntervalSteps) {
            shootFireball();
            stepsSinceLastShot = 0;
        }
    }

    /**
     * Fires a fireball aimed at the player's position.
     */
    private void shootFireball() {
        GameLevel world = (GameLevel) getWorld();
        Student player = world.getStudent();
        if (player == null) return;

        Vec2 bossPos = getPosition();
        Vec2 direction = player.getPosition().sub(bossPos);

        if (direction.length() != 0) {
            direction.normalize();
        } else {
            direction = new Vec2(1, 0); // Default direction if overlapping
        }

        // Fireball is spawned at boss's position and moves toward player
        new Fireball(world, bossPos.add(new Vec2(0, 0)), direction, game);

        System.out.println("Boss is shooting fireball");
    }

    @Override
    public void postStep(StepEvent e) {
        // No post-step logic needed currently
    }

    /**
     * Reduces the boss's health and destroys it when health reaches 0.
     */
    public void takeDamage() {
        health--;
        System.out.println("Boss health: " + health);
        if (health <= 0) {
            destroy();
            System.out.println("Boss defeated!");
        }
    }

    /**
     * Handles collision with bullets.
     * @param e The collision event.
     */
    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Bullet) {
            takeDamage();              // Boss takes damage
            e.getOtherBody().destroy(); // Remove bullet
        }
    }

    /**
     * Checks if the boss has been defeated.
     * @return True if health is 0 or below.
     */
    public boolean isDefeated() {
        return health <= 0;
    }
}
