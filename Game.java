package game;

import java.awt.event.KeyListener;
import javax.swing.*;
import java.awt.event.ActionEvent;

import org.jbox2d.common.Vec2;

/**
 * The main Game class handles overall game logic such as level transitions,
 * life management, timer, pausing, sound management, saving/loading, etc.
 */
public class Game {
    private int lives = 3;                      // Initial number of lives
    private int timeLeft = 120;                // Initial timer in seconds
    private Timer gameTimer;                   // Swing Timer for countdown

    private GameLevel currentLevel;            // Currently active level
    private GameView view;                     // Game view (GUI)
    private SoundManager soundManager;         // Handles game audio
    private String playerName;                 // Player's name
    private boolean paused = false;            // Game pause state
    private PauseMenuPanel pauseMenu;          // Pause menu overlay panel

    /**
     * Constructor: Initializes sound, asks for player's name, and shows main menu.
     */
    public Game() {
        soundManager = new SoundManager();
        soundManager.setVolume(-10.0f); // Default volume

        playerName = JOptionPane.showInputDialog("Enter your name:");
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
        }

        new MainMenu(this); // Show the main menu
    }

    /**
     * Starts the actual game after the main menu.
     */
    public void start() {
        currentLevel = new Level1(this);
        loadLevelMusic("data/background_level1.wav");

        view = new GameView(currentLevel, 800, 600, this);
        bindPauseKey();

        JFrame frame = new JFrame("City Game - Player " + playerName);
        frame.add(view);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.pack();
        frame.setVisible(true);

        // Controls
        StudentController controller = new StudentController(this, currentLevel.getStudent());
        view.addKeyListener(controller);
        view.addMouseListener(new MouseHandler(currentLevel, view));
        view.requestFocus();

        currentLevel.start();
        startCountdownTimer();
    }

    /** Binds ESC key to toggle pause. */
    private void bindPauseKey() {
        InputMap inputMap = view.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = view.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "pauseGame");
        actionMap.put("pauseGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });
    }

    /** Starts the countdown timer and checks for level completion. */
    private void startCountdownTimer() {
        gameTimer = new Timer(1000, e -> {
            if (!paused) {
                if (timeLeft > 0) {
                    timeLeft--;
                } else {
                    System.out.println("Time is up! Game Over!");
                    gameTimer.stop();
                    endGame();
                }
                if (currentLevel.isComplete()) {
                    goToNextLevel();
                }
            }
        });
        gameTimer.start();
    }

    /**
     * Loads background music for a specific level.
     */
    private void loadLevelMusic(String filePath) {
        try {
            soundManager.loadBackgroundMusic(filePath);
        } catch (Exception e) {
            System.out.println("Failed to load music: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Transitions to the next level if the current one is complete.
     */
    public void goToNextLevel() {
        if (!currentLevel.isComplete()) {
            printLevelObjective();
            return;
        }

        currentLevel.stop();
        soundManager.stopBackgroundMusic();

        if (currentLevel instanceof Level1) {
            currentLevel = new Level2(this);
            loadLevelMusic("data/background_level2.wav");
        } else if (currentLevel instanceof Level2) {
            currentLevel = new Level3(this);
            loadLevelMusic("data/background_level3.wav");
        } else {
            System.out.println("Congratulations! You completed the game!");
            endGame();
            return;
        }

        // Update world and controls
        view.setWorld(currentLevel);
        view.updateBackground();
        bindPauseKey();
        currentLevel.start();

        SwingUtilities.invokeLater(() -> view.requestFocusInWindow());

        resetPlayerState();
        System.out.println("Welcome to the next level!");
    }

    /**
     * Prints the current level's objective to the console.
     */
    private void printLevelObjective() {
        if (currentLevel instanceof Level1) {
            System.out.println("Collect at least 20 credits to move to level 2!");
        } else if (currentLevel instanceof Level2) {
            System.out.println("Defeat both enemies to proceed to level 3!");
        } else if (currentLevel instanceof Level3) {
            System.out.println("Defeat the boss enemy to win the game!");
        } else {
            System.out.println("Complete the level's objective to continue.");
        }
    }

    /**
     * Resets player credits and reattaches the controller for the new level.
     */
    private void resetPlayerState() {
        Student student = currentLevel.getStudent();
        if (student != null) {
            student.setCredits(0);

            for (KeyListener kl : view.getKeyListeners()) {
                if (kl instanceof StudentController) {
                    view.removeKeyListener(kl);
                }
            }

            StudentController controller = new StudentController(this, student);
            view.addKeyListener(controller);
        }
    }

    /** Ends the game and exits. */
    private void endGame() {
        if (currentLevel != null) currentLevel.stop();
        if (gameTimer != null) gameTimer.stop();
        System.exit(0);
    }

    /** Reduces player's lives and checks for game over. */
    public void loseLife() {
        lives--;
        System.out.println("Lives left: " + lives);
        if (lives <= 0) {
            System.out.println("No lives left! Game Over!");
            endGame();
        }
    }

    /** Saves current game state to a file. */
    public void saveGame() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            try (var out = new java.io.ObjectOutputStream(
                    new java.io.FileOutputStream(chooser.getSelectedFile()))) {

                SaveData data = new SaveData();
                data.levelNumber = getCurrentLevelNumber();
                data.credits = currentLevel.getStudent().getCredits();
                data.playerX = currentLevel.getStudent().getPosition().x;
                data.playerY = currentLevel.getStudent().getPosition().y;
                data.lives = lives;
                data.timeLeft = timeLeft;
                data.playerName = playerName;

                out.writeObject(data);
                System.out.println("Game saved.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Loads saved game state from a file. */
    public void loadGame() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            try (var in = new java.io.ObjectInputStream(
                    new java.io.FileInputStream(chooser.getSelectedFile()))) {

                SaveData data = (SaveData) in.readObject();
                this.playerName = data.playerName;
                this.lives = data.lives;
                this.timeLeft = data.timeLeft;

                if (currentLevel != null) currentLevel.stop();

                switch (data.levelNumber) {
                    case 1 -> {
                        currentLevel = new Level1(this);
                        loadLevelMusic("data/background_level1.wav");
                    }
                    case 2 -> {
                        currentLevel = new Level2(this);
                        loadLevelMusic("data/background_level2.wav");
                    }
                    case 3 -> {
                        currentLevel = new Level3(this);
                        loadLevelMusic("data/background_level3.wav");
                    }
                    default -> {
                        System.out.println("Invalid level in save.");
                        return;
                    }
                }

                view.setWorld(currentLevel);
                view.updateBackground();
                bindPauseKey();
                currentLevel.start();

                currentLevel.getStudent().setCredits(data.credits);
                currentLevel.getStudent().setPosition(new Vec2(data.playerX, data.playerY));

                resetPlayerState();
                System.out.println("Game loaded successfully.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Toggles game pause/resume. */
    public void togglePause() {
        paused = !paused;
        if (paused) {
            pauseGame();
        } else {
            resumeGame();
        }
    }

    /** Pauses the game and shows pause menu. */
    public void pauseGame() {
        if (currentLevel != null) currentLevel.stop();
        if (gameTimer != null && gameTimer.isRunning()) gameTimer.stop();

        pauseMenu = new PauseMenuPanel(this);
        pauseMenu.setSize(200, 100);

        int centerX = (view.getWidth() - pauseMenu.getWidth()) / 2;
        pauseMenu.setLocation(centerX, 20);

        view.setLayout(null);
        view.add(pauseMenu);
        view.revalidate();
        view.repaint();
    }

    /** Resumes the game from pause. */
    public void resumeGame() {
        if (currentLevel != null) currentLevel.start();
        if (gameTimer != null && !gameTimer.isRunning()) gameTimer.start();

        if (pauseMenu != null) {
            view.remove(pauseMenu);
            pauseMenu = null;
            view.repaint();
        }

        paused = false;
        view.requestFocus();
    }

    /** @return Player's remaining lives. */
    public int getLives() {
        return lives;
    }

    /** @return Time left in seconds. */
    public int getTimeLeft() {
        return timeLeft;
    }


    /** @return Sound manager for the game. */
    public SoundManager getSoundManager() {
        return soundManager;
    }

    /** @return Current player object. */
    public Student getStudent() {
        return currentLevel.getStudent();
    }

    /** @return Current level number (1, 2, 3...) */
    private int getCurrentLevelNumber() {
        if (currentLevel instanceof Level1) return 1;
        if (currentLevel instanceof Level2) return 2;
        if (currentLevel instanceof Level3) return 3;
        return -1;
    }

    /** Main entry point. */
    public static void main(String[] args) {
        new Game();
    }
}

