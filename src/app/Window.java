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
import java.util.regex.*;

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

        frames.add(createGraphicsFrame(applicationTimeThread));
        frames.add(createGUIFrame(applicationTimeThread));

        return frames;
    }



    private JFrame createGUIFrame(ApplicationTime thread){

        final int nameLabelWidth = 100;
        final int valueLabelWidth = 30;
        final int labelHeight = 15;
        final int textFieldWidth = 100;
        final int textFieldHeight = 30;

        JFrame frame = new JFrame("Controls");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));
        panel.setPreferredSize(new Dimension(400, 300));



        // projection alpha slider
        JPanel panelRow1 = new JPanel();
        panelRow1.setLayout(new BorderLayout(10, 0));
        panelRow1.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel projectionAlphaLabel = new JLabel("Alpha");
        projectionAlphaLabel.setPreferredSize(new Dimension(nameLabelWidth, labelHeight));
        JLabel projectionAlphaValue = new JLabel(String.format("%d°", (int)Constants.PROJECTION_ALPHA));
        projectionAlphaValue.setPreferredSize(new Dimension(valueLabelWidth, labelHeight));
        JSlider projectionAlphaSlider = new JSlider(0, 360, (int)Constants.PROJECTION_ALPHA);
        projectionAlphaSlider.addChangeListener(e -> {
            Constants.PROJECTION_ALPHA = (double)projectionAlphaSlider.getValue();
            projectionAlphaValue.setText(String.format("%d", (int)Constants.PROJECTION_ALPHA));
        });

        panelRow1.add(projectionAlphaLabel, BorderLayout.LINE_START);
        panelRow1.add(projectionAlphaSlider, BorderLayout.CENTER);
        panelRow1.add(projectionAlphaValue, BorderLayout.LINE_END);



        // projection s1 slider
        JPanel panelRow2 = new JPanel();
        panelRow2.setLayout(new BorderLayout(10, 0));
        panelRow2.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel projectionS1Label = new JLabel("s1");
        projectionS1Label.setPreferredSize(new Dimension(nameLabelWidth, labelHeight));
        JLabel projectionS1Value = new JLabel(String.format("%.2f", Constants.PROJECTION_S1));
        projectionS1Value.setPreferredSize(new Dimension(valueLabelWidth, labelHeight));
        JSlider projectionS1Slider = new JSlider(0, 100, (int)(Constants.PROJECTION_S1 * 100));
        projectionS1Slider.addChangeListener(e -> {
            Constants.PROJECTION_S1 = (double)projectionS1Slider.getValue() / 100.0;
            projectionS1Value.setText(String.format("%.2f", Constants.PROJECTION_S1));
        });

        panelRow2.add(projectionS1Label, BorderLayout.LINE_START);
        panelRow2.add(projectionS1Slider, BorderLayout.CENTER);
        panelRow2.add(projectionS1Value, BorderLayout.LINE_END);


        // globe rotation slider
        JPanel panelRow3 = new JPanel();
        panelRow3.setLayout(new BorderLayout(10, 0));
        panelRow3.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel globeRotationLabel = new JLabel("Rotation");
        globeRotationLabel.setPreferredSize(new Dimension(nameLabelWidth, labelHeight));
        JLabel globeRotationValue = new JLabel(String.format("%d°", (int)Constants.PROJECTION_S1));
        globeRotationValue.setPreferredSize(new Dimension(valueLabelWidth, labelHeight));
        JSlider globeRotationSlider = new JSlider(0, 360, (int)Constants.GLOBE_ROTATION);
        globeRotationSlider.addChangeListener(e -> {
            Constants.GLOBE_ROTATION = (double)globeRotationSlider.getValue();
            globeRotationValue.setText(String.format("%d", (int)Constants.GLOBE_ROTATION));
        });

        panelRow3.add(globeRotationLabel, BorderLayout.LINE_START);
        panelRow3.add(globeRotationSlider, BorderLayout.CENTER);
        panelRow3.add(globeRotationValue, BorderLayout.LINE_END);


        // show wireframe
        JPanel panelRow4 = new JPanel();
        panelRow4.setLayout(new BorderLayout(10, 0));
        panelRow4.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel showWireframeLabel = new JLabel("Show Wireframe");
        showWireframeLabel.setPreferredSize(new Dimension(nameLabelWidth, labelHeight));
        JCheckBox showWireframeCheckbox = new JCheckBox();
        showWireframeCheckbox.setSelected(Constants.GLOBE_SHOW_WIREFRAME);
        showWireframeCheckbox.addChangeListener(e -> {
            Constants.GLOBE_SHOW_WIREFRAME = showWireframeCheckbox.isSelected();
        });

        panelRow4.add(showWireframeLabel, BorderLayout.LINE_START);
        panelRow4.add(showWireframeCheckbox, BorderLayout.LINE_END);


        // coordinate 1
        JPanel panelRow5 = new JPanel();
        panelRow5.setLayout(new GridBagLayout());
        panelRow5.setBorder(new EmptyBorder(10, 10, 0, 10));

        JLabel startCoordLabel = new JLabel("Start");
        //startCoordLabel.setPreferredSize(new Dimension(nameLabelWidth, labelHeight));
        JTextField startCoordTextField = new JTextField();
        //startCoordTextField.setPreferredSize(new Dimension(textFieldWidth, textFieldHeight));

        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        panelRow5.add(startCoordLabel, c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 6.0;
        panelRow5.add(startCoordTextField, c);

        // coordinate 2
        JPanel panelRow6 = new JPanel();
        panelRow6.setLayout(new GridBagLayout());
        panelRow6.setBorder(new EmptyBorder(10, 10, 0, 10));

        JLabel endCoordLabel = new JLabel("End");
        //startCoordLabel.setPreferredSize(new Dimension(nameLabelWidth, labelHeight));
        JTextField endCoordTextField = new JTextField();
        //startCoordTextField.setPreferredSize(new Dimension(textFieldWidth, textFieldHeight));

        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        panelRow6.add(endCoordLabel, c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 6.0;
        panelRow6.add(endCoordTextField, c);


        JPanel panelRow7 = new JPanel();
        panelRow7.setLayout(new BorderLayout(10, 0));
        panelRow7.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            String regex = "^(-?\\d+\\.\\d*),\\s?(-?\\d+\\.\\d*)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcherStart = pattern.matcher(startCoordTextField.getText().trim());
            Matcher matcherEnd = pattern.matcher(startCoordTextField.getText().trim());

            if(matcherStart.find()){
                Constants.COORD_START_LAT = Double.parseDouble(matcherStart.group(1));
                Constants.COORD_START_LONG = Double.parseDouble(matcherStart.group(2));
            }

            if(matcherEnd.find()){
                Constants.COORD_END_LAT = Double.parseDouble(matcherEnd.group(1));
                Constants.COORD_END_LONG = Double.parseDouble(matcherEnd.group(2));
            }

            System.out.printf("Start: %f %f\tEnd: %f %f", Constants.COORD_START_LAT, Constants.COORD_START_LONG, Constants.COORD_END_LAT, Constants.COORD_END_LONG);
        });

        panelRow7.add(startButton, BorderLayout.NORTH);

        panel.add(panelRow1);
        panel.add(panelRow2);
        panel.add(panelRow3);
        panel.add(panelRow4);
        panel.add(panelRow5);
        panel.add(panelRow6);
        panel.add(panelRow7);

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


