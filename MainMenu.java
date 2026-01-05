package game;

import javax.swing.*;
import java.awt.*;

/**
 * MainMenu provides a simple GUI for the player to start the game,
 * view controls, access settings, or exit the application.
 */
public class MainMenu extends JFrame {

    /**
     * Constructs the main menu window.
     *
     * @param game the main Game instance
     */
    public MainMenu(Game game) {
        setTitle("Main Menu");
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Create a panel to hold buttons in a grid layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));  // 4 rows, 1 column, spacing

        // Create buttons
        JButton startButton = new JButton("Start Game");
        JButton instructionsButton = new JButton("Controls");
        JButton settingsButton = new JButton("Settings");
        JButton exitButton = new JButton("Exit");

        // Start button: Launches the game and hides the menu
        startButton.addActionListener(e -> {
            game.start();
            setVisible(false);
        });

        // Instructions button: Shows basic controls to the player
        instructionsButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Controls:\n" +
                        "→ Arrow keys to move\n" +
                        "→ Z to shoot\n" +
                        "→ Space to jump\n" +
                        "→ Hold Shift + Arrow keys to run\n" +
                        "→ Esc to pause"));

        // Settings button: Opens the settings GUI (volume control, etc.)
        settingsButton.addActionListener(e -> new GameGUI(game, game.getSoundManager()));

        // Exit button: Closes the application
        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the panel
        buttonPanel.add(startButton);
        buttonPanel.add(instructionsButton);
        buttonPanel.add(settingsButton);
        buttonPanel.add(exitButton);

        // Add the panel to the frame
        add(buttonPanel, BorderLayout.CENTER);

        // Center the window and prepare it
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
