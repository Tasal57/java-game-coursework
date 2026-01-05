package game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * SoundManager handles all game audio including background music and sound effects.
 */
public class SoundManager {

    private Clip backgroundMusicClip;
    private FloatControl backgroundMusicControl;

    /**
     * Constructor for the SoundManager.
     */
    public SoundManager() {
        // No initialization needed here
    }

    /**
     * Loads and plays background music in a continuous loop.
     *
     * @param musicFilePath The file path to the audio file.
     * @throws IOException If the file cannot be read.
     * @throws UnsupportedAudioFileException If the audio format is not supported.
     * @throws LineUnavailableException If the system cannot open the audio line.
     */
    public void loadBackgroundMusic(String musicFilePath)
            throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        // Stop existing music if playing
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
        }

        // Load and prepare audio stream
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(musicFilePath));
        backgroundMusicClip = AudioSystem.getClip();
        backgroundMusicClip.open(audioStream);

        // Set volume control for background music
        backgroundMusicControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);

        // Start looping music
        backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
        backgroundMusicClip.start();
    }

    /**
     * Sets the volume of the background music.
     *
     * @param volume A float value; typical range is from -80.0 (mute) to 6.0 (max).
     */
    public void setVolume(float volume) {
        if (backgroundMusicClip != null && backgroundMusicControl != null) {
            backgroundMusicControl.setValue(volume);
        }
    }

    /**
     * Plays a short sound effect once.
     *
     * @param soundFile The path to the sound effect file.
     */
    public void playSound(String soundFile) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(soundFile));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Optional: Set effect volume (here, slightly increased)
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(+6.0f);

            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading sound: " + soundFile);
        }
    }

    /**
     * Stops and releases the background music clip if it's playing.
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
            backgroundMusicClip = null;
        }
    }
}
