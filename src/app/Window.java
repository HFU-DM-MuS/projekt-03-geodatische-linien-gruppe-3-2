package app;

import utils.ApplicationTime;
import utils.Constants;
import utils.FrameUpdater;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Timer;

public class Window {


    public Window() {
        startAnimation();
    }

    public void startAnimation() {
        ApplicationTime applicationTimeThread = new ApplicationTime();
        applicationTimeThread.start();
        FrameUpdater frameUpdater = new FrameUpdater(createFrames(applicationTimeThread));
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(frameUpdater, 100, (int) Constants.TPF);
    }

    private ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
        // a list of all frames (windows) that will be shown
        ArrayList<JFrame> frames = new ArrayList<>();

        // Create main frame (window)
        JFrame frame = new JFrame("Cabinet Projection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new GraphicsContent(applicationTimeThread);
        frame.add(panel);
        frame.pack(); // adjusts size of the JFrame to fit the size of it's components
        frame.setVisible(true);

        frames.add(frame);
        return frames;
    }
}


