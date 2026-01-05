package game;

import javax.swing.*;
import java.awt.*;

/**
 * PauseMenuPanel provides a simple overlay panel for in-game pause options,
 * including Resume, Save, Load, and Quit buttons.
 */
public class PauseMenuPanel extends JPanel {

    /**
     * Constructs the pause menu UI.
     *
     * @param game the game instance used to control pause-related actions
     */
    public PauseMenuPanel(Game game) {
        setOpaque(false); // Make the panel transparent to allow background visibility
        setLayout(new GridLayout(4, 1, 0, 10)); // Vertically stack buttons with spacing

        // === Resume Button ===
        JButton resumeButton = new JButton("Resume");

        resumeButton.addActionListener(e -> game.resumeGame()); // Resume game on click

        // === Save Button ===
        JButton saveButton = new JButton("Save Game");

        saveButton.addActionListener(e -> game.saveGame()); // Trigger game save

        // === Load Button ===
        JButton loadButton = new JButton("Load Game");

        loadButton.addActionListener(e -> game.loadGame()); // Trigger game load

        // === Quit Button ===
        JButton quitButton = new JButton("Quit");

        quitButton.addActionListener(e -> System.exit(0)); // Exit the game

        // Add buttons to the panel
        add(resumeButton);
        add(saveButton);
        add(loadButton);
        add(quitButton);
    }

    /**
     * Paints a semi-transparent overlay behind the buttons.
     *
     * @param g the graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Create a semi-transparent black background
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(20, 50, 100, 150)); // RGBA with alpha transparency
        g2d.fillRect(0, 0, getWidth(), getHeight()); // Fill entire panel
        g2d.dispose(); // Clean up
    }
}
