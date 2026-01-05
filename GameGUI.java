package game;

import javax.swing.*;
import java.awt.*;

/**
 * GUI window for controlling game settings like volume and viewing level instructions.
 */
public class GameGUI extends JFrame {
    private SoundManager soundManager;
    private Game game;  // Reference to the main game instance
    private JSlider volumeSlider;

    /**
     * Constructor: Creates the settings window.
     *
     * @param game          The main game instance for control access.
     * @param soundManager  The sound manager for controlling volume.
     */
    public GameGUI(Game game, SoundManager soundManager) {
        this.game = game;
        this.soundManager = soundManager;

        setTitle("Game Controls");

        // Volume control slider
        volumeSlider = new JSlider(JSlider.HORIZONTAL, -80, 6, -10);  // dB range for volume
        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.addChangeListener(e -> soundManager.setVolume(volumeSlider.getValue()));

        // Instructions button to go to the next level
        JButton nextLevelButton = new JButton("Instructions for the level");
        nextLevelButton.addActionListener(e -> game.goToNextLevel());

        // Panel layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.add(new JLabel("Volume"));
        panel.add(volumeSlider);
        panel.add(nextLevelButton);

        add(panel);

        setSize(300, 300);
        setLocation(100, 100);  // Avoid overlapping the main game window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close only this window
        setVisible(true);
    }
}
