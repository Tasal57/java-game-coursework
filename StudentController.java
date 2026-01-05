package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * StudentController listens for keyboard input and controls the student's (player's) movements
 * and actions (walking, jumping, etc.) within the game world.
 */
public class StudentController implements KeyListener {

    private Student student;  // The student (player) object that will be controlled by the keyboard
    private Game game;  // The game object to check the game's state (e.g., if the player has lost all lives)

    /**
     * Constructor to initialize the controller with the game and student references.
     * @param game The main game object that manages the game state
     * @param student The player-controlled student object
     */
    public StudentController(Game game, Student student) {
        this.student = student;  // Assign the player (student) object
        this.game = game;  // Assign the game object
    }


    // Java expects you to implement all the methods defined by the keyListener interface,
    @Override
    public void keyTyped(KeyEvent e) {
        // Empty implementation, as you don't need this method for your game.
    }



    // Called when a key is pressed, handles player movement and actions
    @Override
    public void keyPressed(KeyEvent e) {
        // If the game is over (no lives left), ignore further inputs
        if (game.getLives() <= 0) {
            return; // Do nothing if the game is over
        }

        int code = e.getKeyCode();  // Get the key code of the key that was pressed
        boolean isRunning = e.isShiftDown();  // Check if the Shift key is held down (used for running)


        /* These if and else if statements can't run simultaneously only one can run and the rest are skipped.
        But you can move and jump at the same time if keys are held because of how Java's event system works.
         */

        // Check for left arrow key press (move left)
        if (code == KeyEvent.VK_LEFT) {
            student.startWalking(-5, isRunning); // Move the student left (negative x-direction), run if Shift is held
            // REMEMBER, to run,  hold shift first and then move whilst simultaneously holding shift.
        }
        // Check for right arrow key press (move right)
        else if (code == KeyEvent.VK_RIGHT) {
            student.startWalking(5, isRunning);  // Move the student right (positive x-direction), run if Shift is held
        }
        // Check for spacebar key press (jump)
        else if (code == KeyEvent.VK_SPACE) {
            student.jump(isRunning);  // Make the student jump; higher jump if Shift is held.
            // The isRunning is a greater value than walking so putting this parameter will result in a greater jump.
        }

        else if (code == KeyEvent.VK_Z) {
            student.shoot();

        }
    }

    // Called when a key is released, stops the student's movement
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();  // Get the key code of the key that was released

        // If the left or right arrow keys are released, stop walking
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) {
            student.stopWalking();  // Stop the student from walking when the keys are released
        }
    }

}



