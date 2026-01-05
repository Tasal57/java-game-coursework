package game;

import city.cs.engine.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.jbox2d.common.Vec2;
import java.awt.Point;
import javax.swing.Timer;

/**
 * MouseHandler listens for mouse events and creates balls in the game world
 * at the location of mouse clicks. It also applies forces to balls and ensures
 * balls are removed after some time to prevent overcrowding.
 */
public class MouseHandler implements MouseListener {

    private GameLevel world;
    private GameView view;
    private int maxBalls = 5;  // Maximum number of balls allowed in the game world at once
    private int currentBalls = 0;  // Current number of balls in the game world

    public MouseHandler(GameLevel w, GameView v) {
        this.world = w;
        this.view = v;
    }

    // Called when the mouse is pressed; creates a new ball at the clicked location
    @Override
    public void mousePressed(MouseEvent e) {
        // Check if the maximum number of balls has not been exceeded
        if (currentBalls < maxBalls) {
            // Create a new ball with a circular shape
            Shape circleShape = new CircleShape(1f);
            DynamicBody ball = new DynamicBody(world, circleShape);

            // Get the mouse coordinates in pixels and convert them to world coordinates
            Point mousePoint = e.getPoint();
            Vec2 worldPoint = view.viewToWorld(mousePoint);
            ball.setPosition(worldPoint);  // Set the ball's position in the game world

            // Apply a random force to the ball to make it move
            float randomX = (float) (Math.random() * 10 - 5);  // Random X velocity
            float randomY = (float) (Math.random() * 10 - 5);  // Random Y velocity
            ball.setLinearVelocity(new Vec2(randomX, randomY));

            // Increment the ball count
            currentBalls++;

            // Set a timer to destroy the ball after 5 seconds
            Timer timer = new Timer(5000, event -> {
                ball.destroy();  // Remove the ball after 5 seconds
                currentBalls--;  // Decrease the ball count
            });
            timer.setRepeats(false);  // Timer should run only once
            timer.start();
        }
    }

    // Empty implementations for other mouse events
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
