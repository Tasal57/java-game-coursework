package game;

import java.io.Serializable;

/**
 * Serializable class to store the player's game state for saving/loading.
 */
public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    public int levelNumber;
    public int credits;
    public float playerX;
    public float playerY;
    public int lives;
    public int timeLeft;
    public String playerName;
}
