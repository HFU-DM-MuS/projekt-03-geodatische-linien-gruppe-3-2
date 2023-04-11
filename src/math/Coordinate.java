package math;

import utils.Constants;

public class Coordinate {
    private final double longitude;
    private final double latitude;

    private final double r = Constants.GLOBE_SCALE;

    public Coordinate(double lat, double lon) {
        this.longitude = lon;
        this.latitude = lat;
    }

    public Coordinate() {
        this(0.0, 0.0);
    }

    public Vector toCartesian() {
        return new Vector(
                this.r * Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(this.longitude)),
                this.r * Math.cos(Math.toRadians(this.latitude)) * Math.sin(Math.toRadians(this.longitude)),
                this.r * Math.sin(Math.toRadians(this.latitude))
        );
    }
}
