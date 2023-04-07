package app;

import javax.swing.*;
import java.awt.*;

import math.Coordinate;
import math.Matrix;
import math.Vector;
import utils.ApplicationTime;
import utils.Constants;

public class GraphicsContent extends JPanel {

    private final ApplicationTime t;
    private final int w = Constants.WINDOW_WIDTH;
    private final int h = Constants.WINDOW_HEIGHT;
    private final int cx = w / 2;
    private final int cy = h / 2;
    private double deltaTime = 0.0;
    private double lastFrameTime = 0.0;
    private final Matrix projectionMatrix = new Matrix(new double[][]{
            {-Constants.PROJECTION_S1 * Math.sin(Math.toRadians(Constants.PROJECTION_ROT)), 1.0, 0.0, Constants.WINDOW_WIDTH / 2.},
            {-Constants.PROJECTION_S1 * Math.cos(Math.toRadians(Constants.PROJECTION_ROT)), 0.0, -1.0, Constants.WINDOW_HEIGHT / 2.}
    });

    private final double phi_p = Math.toDegrees(
            Math.atan(
                    Constants.PROJECTION_S1 *
                            Math.sin(Math.toRadians(Constants.PROJECTION_ROT))
            )
    );
    private final double theta_p = Math.toDegrees(
            Math.atan(
                    -Constants.PROJECTION_S1 *
                            Math.cos(Math.toRadians(Constants.PROJECTION_ROT)) *
                            Math.cos(
                                    Math.atan(
                                            Constants.PROJECTION_S1 *
                                                    Math.sin(Math.toRadians(Constants.PROJECTION_ROT))
                                    )
                            )
            )
    );

    public GraphicsContent(ApplicationTime thread) {
        this.t = thread;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        deltaTime = t.getTimeInSeconds() - lastFrameTime;
        lastFrameTime = t.getTimeInSeconds();

        this.paintCoordinateSystem(g);
        this.paintGlobeWireframe(g);
        this.paintCircumcircle(g);
    }

    private void paintCoordinateSystem(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // coordinate system
        g.setColor(Color.BLACK);
        g.drawLine(0, h / 2, w, h / 2);
        g.drawLine(w / 2, 0, w / 2, h);

        // axis labels
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("x-axis", w - 50, h / 2 + 10);
        g.drawString("y-axis", w / 2 + 10, 10);

        // draw fps
        g.drawString(String.format("%.1f FPS", 1.0 / deltaTime), 10, 10);


        Vector ux = new Vector(100.0, 0.0, 0.0);
        Vector uy = new Vector(0.0, 100.0, 0.0);
        Vector uz = new Vector(0.0, 0.0, 100.0);

        try {
            Vector sx = projectionMatrix.doScreenProjection(ux);
            Vector sy = projectionMatrix.doScreenProjection(uy);
            Vector sz = projectionMatrix.doScreenProjection(uz);

            g2d.setStroke(new BasicStroke(5.0f));

            g.setColor(Color.RED);
            g.drawLine(cx, cy, (int) sx.x(), (int) sx.y());

            g.setColor(Color.GREEN);
            g.drawLine(cx, cy, (int) sy.x(), (int) sy.y());

            g.setColor(Color.BLUE);
            g.drawLine(cx, cy, (int) sz.x(), (int) sz.y());


        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void paintGlobeWireframe(Graphics g) {
        try {
            int step = 1;
            double[] lats = {-30.0, -60.0, 0.0, 30.0, 60.0};
            double[] longs = {0.0, 45.0, 90.0, 135.0, 180.0, 225.0, 270.0, 315.0};

            // horizontal lines
            for (double latitude : lats) {
                for (int longitude = 0; longitude < 360; longitude += step) {
                    Coordinate c = new Coordinate(longitude, latitude);
                    Vector cv = c.toCartesian();
                    Vector sv = projectionMatrix.doScreenProjection(cv);

                    int dotSize = 1;
                    if (cv.x() >= 0.0) {
                        g.setColor(Color.BLACK);
                        dotSize = 2;
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                    }

                    g.fillRect((int) sv.x(), (int) sv.y(), dotSize, dotSize);
                }
            }

            // vertical lines
            for (double longitude : longs) {
                for (int latitude = -90; latitude < 90; latitude += step) {
                    Coordinate c = new Coordinate(longitude, latitude);
                    Vector cv = c.toCartesian();
                    Vector sv = projectionMatrix.doScreenProjection(cv);

                    int dotSize = 1;
                    if (cv.x() >= 0.0) {
                        g.setColor(Color.BLACK);
                        dotSize = 2;
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                    }

                    g.setColor(Color.BLACK);
                    g.fillRect((int) sv.x(), (int) sv.y(), dotSize, dotSize);
                }
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void paintCircumcircle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        try {

            int step = 5;
            Vector cv_prev = new Vector(0.0, Math.cos(Math.toRadians(0)), Math.sin(Math.toRadians(0)));
            cv_prev.rotateWorldY(-theta_p).rotateWorldZ(phi_p).multiply(Constants.GLOBE_SCALE);

            for (int t = step; t <= 360; t += step) {
                Vector cv = new Vector(0.0, Math.cos(Math.toRadians(t)), Math.sin(Math.toRadians(t)));

                cv.rotateWorldY(-theta_p).rotateWorldZ(phi_p).multiply(Constants.GLOBE_SCALE);

                Vector sv = projectionMatrix.doScreenProjection(cv);
                Vector sv_prev = projectionMatrix.doScreenProjection(cv_prev);

                g.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(2.0f));
                g.drawLine((int) sv_prev.x(), (int) sv_prev.y(), (int) sv.x(), (int) sv.y());

                cv_prev = cv;
            }
        } catch (Exception e) {
            System.err.println(e);
        }


    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
    }
}
