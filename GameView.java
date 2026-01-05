package game;

import city.cs.engine.UserView;
import javax.swing.*;
import java.awt.*;

/**
 * GameView is responsible for drawing and rendering the visual elements on the screen.
 * It displays the game background, the player's lives, the time remaining, and the credits (coins).
 * It also handles drawing the "Game Over" screen when the player loses all lives.
 */
public class GameView extends UserView {
    private Image background;  // Image for the background of the game
    private Game game;  // Reference to the Game class to access the time left and the player's lives

    private Image heartIcon;  // Icon for the heart used to represent lives
    private Image coinIcon;   // Icon for the coin used to represent credits

    /**
     * Constructor for initializing the game view with a given game world, dimensions, and game instance.
     * This allows us to render the game with the current state (lives, time, credits, etc.).
     * @param world The GameWorld instance to render.
     * @param width The width of the game window.
     * @param height The height of the game window.
     * @param game The Game instance used to access the game state.
     */
    public GameView(GameLevel world, int width, int height, Game game) {
        super(world, width, height);  // Call to the parent constructor for setting the world and dimensions
        this.game = game;  // Store the game reference to access its state
        background = new ImageIcon(world.getBackgroundImage()).getImage();  // Load the background image
        heartIcon = new ImageIcon("data/heartIcon.png").getImage();  // Load the heart icon for displaying lives
        coinIcon = new ImageIcon("data/coinIcon.png").getImage();    // Load the coin icon for displaying credits
    }

    /**
     * Method to paint the background of the game world (the static image behind the game elements).
     * @param g The Graphics2D object used for drawing the image.
     */
    @Override
    protected void paintBackground(Graphics2D g) {
        // Draw the background image, stretching it to fit the window size
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }

    /**
     * Method to paint the foreground of the game world, which includes elements like lives,
     * time remaining, credits, and the "Game Over" message.
     * @param g The Graphics2D object used for drawing the game elements.
     */
    @Override
    protected void paintForeground(Graphics2D g) {
        super.paintForeground(g);  // Call the parent class method to ensure the normal foreground is painted

        int lives = game.getLives();  // Get the current number of lives from the game

        // Draw the lives as hearts
        for (int i = 0; i < lives; i++) {
            g.drawImage(heartIcon, 20 + (i * 30), 20, 25, 25, this);  // Draw hearts in a row for each life
        }

        // Draw the time remaining as a progress bar
        int maxTime = 60;  // Maximum time (in seconds)
        int timeLeft = game.getTimeLeft();  // Get the remaining time from the game
        int barWidth = (int) ((timeLeft / (float) maxTime) * 100);  // Scale the width of the bar based on remaining time
        g.setColor(Color.GRAY);
        g.fillRect(20, 60, 200, 20);  // Background of the time bar
        g.setColor(Color.GREEN);
        g.fillRect(20, 60, barWidth, 20);  // Foreground of the time bar (filled based on remaining time)
        g.setColor(Color.WHITE);
        g.drawString("Time: " + timeLeft, 100, 75);  // Draw the remaining time as text

        // Draw the credits as coins
        int credits = game.getStudent().getCredits();  // Get the current credits from the student's object
        for (int i = 0; i < credits / 10; i++) {  // Every 10 credits = 1 coin displayed
            g.drawImage(coinIcon, 20 + (i * 30), 100, 20, 20, this);  // Draw coins as small icons
        }

        // If the player has no lives left, display "Game Over" message
        if (lives <= 0) {
            g.setFont(new Font("Arial", Font.BOLD, 100));  // Set a large, bold font for the message
            g.setColor(Color.RED);  // Set the color to red for visibility
            g.drawString("GAME OVER", getWidth() / 2 - 280, getHeight() / 2);  // Draw the "Game Over" message
        }
    }

    public void updateBackground() {
        // Cast getWorld() to GameLevel to access getBackgroundImage()
        GameLevel gameLevel = (GameLevel) getWorld();
        background = new ImageIcon(gameLevel.getBackgroundImage()).getImage();
        repaint(); // Redraw the screen with the new background
    }
}