package app;

import utils.ApplicationTime;
import utils.Constants;
import utils.FrameUpdater;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;

public class Window {

    private final int nameLabelWidth = 50;
    private final int valueLabelWidth = 30;

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

        frames.add(createGraphicsFrame(applicationTimeThread));
        frames.add(createGUIFrame(applicationTimeThread));

        return frames;
    }



    private JFrame createGUIFrame(ApplicationTime thread){
        JFrame frame = new JFrame("Controls");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.setPreferredSize(new Dimension(300, 100));



        // projection alpha slider
        JPanel panelRow1 = new JPanel();
        panelRow1.setLayout(new BorderLayout(10, 0));
        panelRow1.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel projectionAlphaLabel = new JLabel("Alpha");
        projectionAlphaLabel.setPreferredSize(new Dimension(nameLabelWidth, 10));
        JLabel projectionAlphaValue = new JLabel(String.format("%d°", (int)Constants.PROJECTION_ALPHA));
        projectionAlphaValue.setPreferredSize(new Dimension(valueLabelWidth, 10));
        JScrollBar projectionAlphaBar = new JScrollBar(Adjustable.HORIZONTAL, (int)Constants.PROJECTION_ALPHA, 5, 0, 365);
        projectionAlphaBar.addAdjustmentListener(e -> {
            Constants.PROJECTION_ALPHA = (double)projectionAlphaBar.getValue();
            projectionAlphaValue.setText(String.format("%d", (int)Constants.PROJECTION_ALPHA));
        });

        panelRow1.add(projectionAlphaLabel, BorderLayout.LINE_START);
        panelRow1.add(projectionAlphaBar, BorderLayout.CENTER);
        panelRow1.add(projectionAlphaValue, BorderLayout.LINE_END);



        // projection s1 slider
        JPanel panelRow2 = new JPanel();
        panelRow2.setLayout(new BorderLayout(10, 0));
        panelRow2.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel projectionS1Label = new JLabel("s1");
        projectionS1Label.setPreferredSize(new Dimension(nameLabelWidth, 10));
        JLabel projectionS1Value = new JLabel(String.format("%.2f", Constants.PROJECTION_S1));
        projectionS1Value.setPreferredSize(new Dimension(valueLabelWidth, 10));
        JScrollBar projectionS1Bar = new JScrollBar(Adjustable.HORIZONTAL, (int)(Constants.PROJECTION_S1 * 100), 5, 0, 105);
        projectionS1Bar.addAdjustmentListener(e -> {
            Constants.PROJECTION_S1 = (double)projectionS1Bar.getValue() / 100.0;
            projectionS1Value.setText(String.format("%.2f", Constants.PROJECTION_S1));
        });

        panelRow2.add(projectionS1Label, BorderLayout.LINE_START);
        panelRow2.add(projectionS1Bar, BorderLayout.CENTER);
        panelRow2.add(projectionS1Value, BorderLayout.LINE_END);


        // globe rotation slider
        JPanel panelRow3 = new JPanel();
        panelRow3.setLayout(new BorderLayout(10, 0));
        panelRow3.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel globeRotationLabel = new JLabel("Rotation");
        globeRotationLabel.setPreferredSize(new Dimension(nameLabelWidth, 10));
        JLabel globeRotationValue = new JLabel(String.format("%d°", (int)Constants.PROJECTION_S1));
        globeRotationValue.setPreferredSize(new Dimension(valueLabelWidth, 10));
        JScrollBar globeRotationBar = new JScrollBar(Adjustable.HORIZONTAL, (int)(Constants.GLOBE_ROTATION), 5, 0, 365);
        globeRotationBar.addAdjustmentListener(e -> {
            Constants.GLOBE_ROTATION = (double)globeRotationBar.getValue();
            globeRotationValue.setText(String.format("%d", (int)Constants.GLOBE_ROTATION));
        });

        panelRow3.add(globeRotationLabel, BorderLayout.LINE_START);
        panelRow3.add(globeRotationBar, BorderLayout.CENTER);
        panelRow3.add(globeRotationValue, BorderLayout.LINE_END);

        panel.add(panelRow1);
        panel.add(panelRow2);
        panel.add(panelRow3);

        frame.add(panel);
        frame.setLocation(Constants.WINDOW_WIDTH, 1);
        frame.pack();
        frame.setVisible(true);

        return frame;
    }

    private JFrame createGraphicsFrame(ApplicationTime thread) {
        // Create main frame (window)
        JFrame frame = new JFrame("Projekt 3 - Geodätische Linien");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new GraphicsContent(thread);

        frame.add(panel);
        frame.setLocation(1, 1);
        frame.pack(); // adjusts size of the JFrame to fit the size of it's components
        frame.setVisible(true);

        return frame;
    }

}


