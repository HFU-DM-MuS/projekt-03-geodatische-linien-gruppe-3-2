package math;

public class Vector {
    private double x, y, z;

    public Vector(double _x, double _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    public Vector(double _x, double _y) {
        this(_x, _y, 0.0);
    }

    public Vector() {
        this(.0, .0, .0);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public Vector add(Vector other) {
        x += other.x();
        y += other.y();
        z += other.z();

        return this;
    }

    public Vector multiply(double f) {
        x *= f;
        y *= f;
        z *= f;

        return this;
    }

    public Vector divide(double d) {
        x /= d;
        y /= d;
        z /= d;

        return this;
    }

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public Vector normalize() {
        double length = length();

        x /= length;
        y /= length;
        z /= length;

        return this;
    }

    public double dot(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}
