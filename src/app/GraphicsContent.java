package app;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import math.Coordinate;
import math.Matrix;
import math.Vector;
import utils.ApplicationTime;
import utils.Constants;

public class GraphicsContent extends JPanel {

    private final ApplicationTime t;
    private final int w = Constants.WINDOW_WIDTH;
    private final int h = Constants.WINDOW_HEIGHT;

    private final int cx = w/2;
    private final int cy = h / 2;
    private double deltaTime = 0.0;
    private double lastFrameTime = 0.0;
    private Matrix projectionMatrix;

    private Graphics g;
    private Graphics2D g2d;

    private double phi_p;
    private double theta_p;

    private final ArrayList<String> debugStrings = new ArrayList<String>();

    public GraphicsContent(ApplicationTime thread) {
        this.t = thread;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(Constants.DRAW_DEBUG_INFO){
            this.debugStrings.clear();
        }

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

        if (Constants.COORD_SHOW_ON_GLOBE) {
            this.paintGeodesicLine();
        }

        if(Constants.DRAW_DEBUG_INFO){
            this.addDebugInfo(String.format("%.1f FPS", 1.0 / deltaTime));
            this.drawDebugInfo();
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




        Vector ux = new Vector(1.0, 0.0, 0.0);

        Vector uy = new Vector(0.0, 1.0, 0.0);
        Vector uz = new Vector(0.0, 0.0, 1.0);

        ux.scale(Constants.GLOBE_SCALE);
        uy.scale(Constants.GLOBE_SCALE);
        uz.scale(Constants.GLOBE_SCALE);

        ux.rotateWorldZ(Constants.GLOBE_ROTATION);
        uy.rotateWorldZ(Constants.GLOBE_ROTATION);
        uz.rotateWorldZ(Constants.GLOBE_ROTATION);

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
            cv_prev.rotateWorldY(-theta_p).rotateWorldZ(phi_p).scale(Constants.GLOBE_SCALE);

            g.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int t = step; t <= 360; t += step) {
                Vector cv = new Vector(0.0, Math.cos(Math.toRadians(t)), Math.sin(Math.toRadians(t)));

                cv.rotateWorldY(-theta_p).rotateWorldZ(phi_p).scale(Constants.GLOBE_SCALE);

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

    private void drawVector(Vector v, Color c) {
        Vector o = new Vector();

        v.rotateWorldZ(Constants.GLOBE_ROTATION);

        try {
            Vector sv = projectionMatrix.doScreenProjection(v);
            Vector svo = projectionMatrix.doScreenProjection(o);

            g.setColor(c);
            g2d.setStroke(new BasicStroke(2.0f));
            g.drawLine((int) svo.x(), (int) svo.y(), (int) sv.x(), (int) sv.y());

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void paintGeodesicLine() {

        Coordinate coordStart = new Coordinate(Constants.COORD_START_LAT, Constants.COORD_START_LONG);
        Coordinate coordEnd = new Coordinate(Constants.COORD_END_LAT, Constants.COORD_END_LONG);

        Vector q = coordEnd.toCartesian();
        Vector p = coordStart.toCartesian();


        this.drawVector(p, Color.PINK);
        this.drawVector(q, Color.ORANGE);

        // TODO: fix
        // calculate delta angle
        double delta = Math.acos(
                p.dot(q) / (p.length() * q.length())
        );
        this.addDebugInfo(String.format("delta: %.2f", delta));

        // calculate distance
        double distance = delta * Constants.GLOBE_SCALE;

        Vector p_u = p.copy().normalize();
        Vector n_u = p.cross(q).normalize();
        Vector u_u = n_u.cross(p_u).normalize();

        this.addDebugInfo(String.format("q: %s", q));
        this.addDebugInfo(String.format("p: %s", p));
        this.addDebugInfo(String.format("p x q: %s", p.cross(q)));

        this.drawVector(n_u.scale(Constants.GLOBE_SCALE), Color.BLUE);
        /*
        this.drawVector(p_u.copy().scale(Constants.GLOBE_SCALE), Color.RED);
        this.drawVector(u_u.copy().scale(Constants.GLOBE_SCALE), Color.GREEN);
        this.drawVector(n_u.copy().scale(Constants.GLOBE_SCALE), Color.BLUE);

         */

        double r = Constants.GLOBE_SCALE;

        try {

            g.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double step = Math.toRadians(5.0);
            Vector v_prev = p_u.multiply(r * Math.cos(0.0)).add(u_u.multiply(r * Math.sin(0.0)));
            v_prev.rotateWorldZ(Constants.GLOBE_ROTATION);
            for (double t = step; t <= delta; t += step) {

                Vector v = p_u.multiply(r * Math.cos(t)).add(u_u.multiply(r * Math.sin(t)));


                v.normalize().scale(r);

                // apply rotation
                v.rotateWorldZ(Constants.GLOBE_ROTATION);

                Vector sv = projectionMatrix.doScreenProjection(v);
                Vector sv_prev = projectionMatrix.doScreenProjection(v_prev);

                int dotSize = 2;

                if (isVisible(v) && isVisible(v_prev)) {
                    g.drawLine((int) sv_prev.x(), (int) sv_prev.y(), (int) sv.x(), (int) sv.y());
                } else {
                    g.fillRect((int) sv.x() - dotSize / 2, (int) sv.y() - dotSize / 2, dotSize, dotSize);
                }

                v_prev = v;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

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

    private void addDebugInfo(String line) {
        this.debugStrings.add(line);
    }

    private void drawDebugInfo() {
        int x = 10;
        int y = 10;
        g.setColor(Color.BLACK);

        for(String line : this.debugStrings) {
            g.drawString(line, x, y);

            y += 15;
        }
    }
}
