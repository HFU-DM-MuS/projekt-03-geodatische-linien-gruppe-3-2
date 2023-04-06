package app;

import javax.swing.*;
import java.awt.*;

import utils.ApplicationTime;
import utils.Constants;

public class GraphicsContent extends JPanel {

    private final ApplicationTime t;
    private int w = Constants.WINDOW_WIDTH;
    private int h = Constants.WINDOW_HEIGHT;
    private int cx = w / 2;
    private int cy = h / 2;
    private double deltaTime = 0.0;
    private double lastFrameTime = 0.0;

    public GraphicsContent(ApplicationTime thread) {
        this.t = thread;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        deltaTime = t.getTimeInSeconds() - lastFrameTime;
        lastFrameTime = t.getTimeInSeconds();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
    }
}
