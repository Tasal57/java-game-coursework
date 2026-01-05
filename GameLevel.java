package game;

import city.cs.engine.*;

/**
 * Abstract base class for all game levels.
 * Defines common properties and methods used across all levels.
 */
public abstract class GameLevel extends World {
    protected Student student; // The main player character
    protected Game game;       // Reference to the main game instance

    /**
     * Constructor to create a level and attach it to the game.
     *
     * @param game The main game object.
     */
    public GameLevel(Game game) {
        super();
        this.game = game;
    }

    /**
     * Returns the student (player) in the level.
     *
     * @return the player character.
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Assigns the student (player) to this level.
     *
     * @param student the player character to set.
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * Returns the Game instance for cross-class interactions.
     *
     * @return the main Game object.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Populates the level with specific game objects.
     *
     * @param game the main Game object.
     */
    protected abstract void populate(Game game);

    /**
     * Checks if the level objectives have been completed.
     *
     * @return true if complete, false otherwise.
     */
    public abstract boolean isComplete();

    /**
     * Gets the background image path for the level.
     *
     * @return the file path for the background image.
     */
    public abstract String getBackgroundImage();


    /**
     * Stops the level logic.
     */
    @Override
    public void stop() {
        super.stop();

    }
}
