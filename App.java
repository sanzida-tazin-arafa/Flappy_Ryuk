package flappyryuk;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class App {
    public static void main(String args[]) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;

        // Play background music
        playBackgroundMusic("/flappyryuk/ls_theme.wav");

        JFrame frame = new JFrame("Flappy Ryuk ");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyRyuk flappyRyuk = new FlappyRyuk();
        frame.add(flappyRyuk);
        frame.pack();
        flappyRyuk.requestFocus();
        frame.setVisible(true);
    }

    public static void playBackgroundMusic(String filePath) {
        try {
            // Load the audio file from the classpath
            URL audioFileURL = App.class.getResource(filePath);
            if (audioFileURL == null) {
                System.err.println("Could not find audio file: " + filePath);
                return;
            }
            
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFileURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }
}                 