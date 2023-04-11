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
    private Matrix projectionMatrix;

    private Graphics g;
    private Graphics2D g2d;

    private double phi_p;
    private double theta_p;

    public GraphicsContent(ApplicationTime thread) {
        this.t = thread;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.g = g;
        this.g2d = (Graphics2D) g;

        deltaTime = t.getTimeInSeconds() - lastFrameTime;
        lastFrameTime = t.getTimeInSeconds();

        this.recalculateValues();

        this.paintCoordinateSystem();
        if (Constants.GLOBE_SHOW_WIREFRAME) {
            this.paintGlobeWireframe();
        }

        this.paintCircumcircle();

        //this.drawGeoPosition(new Coordinate(0.0, 51.477928));
        if(Constants.COORD_SHOW_ON_GLOBE){


            this.paintGeodaticLine();
        }
    }

    private void paintCoordinateSystem() {

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

        /*
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
            e.printStackTrace();
        }
         */
    }

    private void paintGlobeWireframe() {
        try {
            int step = 2;
            double[] lats = {-30.0, -60.0, 0.0, 30.0, 60.0};
            double[] longs = {0.0, 45.0, 90.0, 135.0, 180.0, 225.0, 270.0, 315.0};

            // horizontal lines
            for (double latitude : lats) {
                for (int longitude = 0; longitude < 360; longitude += step) {
                    Coordinate c = new Coordinate(latitude, longitude);
                    Vector cv = c.toCartesian();

                    // no need to rotate horizontal lines
                    cv.rotateWorldZ(Constants.GLOBE_ROTATION);

                    Vector sv = projectionMatrix.doScreenProjection(cv);

                    int dotSize = 1;

                    if (isVisible(cv)) {
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
                    Coordinate c = new Coordinate(latitude, longitude);
                    Vector cv = c.toCartesian();

                    // apply rotation
                    cv.rotateWorldZ(Constants.GLOBE_ROTATION);

                    Vector sv = projectionMatrix.doScreenProjection(cv);

                    int dotSize = 1;
                    if (isVisible(cv)) {
                        g.setColor(Color.BLACK);
                        dotSize = 2;
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                    }

                    if (longitude == 0.0) {
                        // greenwich
                        g.setColor(Color.GREEN);
                    }

                    g.fillRect((int) sv.x(), (int) sv.y(), dotSize, dotSize);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paintCircumcircle() {
        try {

            int step = 2;
            Vector cv_prev = new Vector(0.0, Math.cos(Math.toRadians(0)), Math.sin(Math.toRadians(0)));
            cv_prev.rotateWorldY(-theta_p).rotateWorldZ(phi_p).multiply(Constants.GLOBE_SCALE);

            g.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int t = step; t <= 360; t += step) {
                Vector cv = new Vector(0.0, Math.cos(Math.toRadians(t)), Math.sin(Math.toRadians(t)));

                cv.rotateWorldY(-theta_p).rotateWorldZ(phi_p).multiply(Constants.GLOBE_SCALE);

                Vector sv = projectionMatrix.doScreenProjection(cv);
                Vector sv_prev = projectionMatrix.doScreenProjection(cv_prev);

                g.drawLine((int) sv_prev.x(), (int) sv_prev.y(), (int) sv.x(), (int) sv.y());

                cv_prev = cv;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
    }

    private boolean isVisible(Vector point) {
        return (
                Math.cos(Math.toRadians(phi_p)) * Math.cos(Math.toRadians(theta_p)) * point.x() +
                        Math.sin(Math.toRadians(phi_p)) * Math.cos(Math.toRadians(theta_p)) * point.y() +
                        Math.sin(Math.toRadians(theta_p)) * point.z()
        ) >= 0.0;
    }

    private void drawGeoPosition(Coordinate coord) {

        this.drawGeoPosition(coord, Color.RED);
    }

    private void drawGeoPosition(Coordinate coord, Color c) {
        Vector cv = coord.toCartesian();

        // apply rotation
        cv.rotateWorldZ(Constants.GLOBE_ROTATION);

        try {
            Vector sv = projectionMatrix.doScreenProjection(cv);

            int dotSize = 6;
            g.setColor(c);

            if (isVisible(cv)) {
                g.fillOval((int) sv.x() - dotSize / 2, (int) sv.y() - dotSize / 2, dotSize, dotSize);
            } else {
                g.drawOval((int) sv.x() - dotSize / 2, (int) sv.y() - dotSize / 2, dotSize, dotSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void paintGeodaticLine() {

        Coordinate coordStart = new Coordinate(Constants.COORD_START_LAT, Constants.COORD_START_LONG);
        Coordinate coordEnd = new Coordinate(Constants.COORD_END_LAT, Constants.COORD_END_LONG);

        Vector vq = coordStart.toCartesian();
        Vector vp = coordEnd.toCartesian();

        double delta = Math.acos(
                Math.toRadians(vp.dot(vq) / (vp.length() * vq.length()))
        );
        System.out.println(String.format("%.2f", Math.toDegrees(delta)));
        System.out.println(String.format("%.2f", delta * Constants.GLOBE_SCALE));

        this.drawGeoPosition(coordStart, Color.GREEN);
        this.drawGeoPosition(coordEnd, Color.RED);
    }

    private void recalculateValues() {

        projectionMatrix = new Matrix(new double[][]{
                {-Constants.PROJECTION_S1 * Math.sin(Math.toRadians(Constants.PROJECTION_ALPHA)), 1.0, 0.0, Constants.WINDOW_WIDTH / 2.},
                {-Constants.PROJECTION_S1 * Math.cos(Math.toRadians(Constants.PROJECTION_ALPHA)), 0.0, -1.0, Constants.WINDOW_HEIGHT / 2.}
        });

        phi_p = Math.toDegrees(
                Math.atan(
                        Constants.PROJECTION_S1 *
                                Math.sin(Math.toRadians(Constants.PROJECTION_ALPHA))
                )
        );

        theta_p = Math.toDegrees(
                Math.atan(
                        -Constants.PROJECTION_S1 *
                                Math.cos(Math.toRadians(Constants.PROJECTION_ALPHA)) *
                                Math.cos(
                                        Math.atan(
                                                Constants.PROJECTION_S1 *
                                                        Math.sin(Math.toRadians(Constants.PROJECTION_ALPHA))
                                        )
                                )
                )
        );
    }
}
