package math;

public class Vector {
    private double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(double _x, double _y) {
        this(_x, _y, 0.0);
    }

    public Vector() {
        this(.0, .0, .0);
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double z() {
        return this.z;
    }

    public Vector add(Vector other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;

        return this;
    }

    public Vector multiply(double f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;

        return this;
    }

    public Vector divide(double d) {
        this.x /= d;
        this.y /= d;
        this.z /= d;

        return this;
    }

    public double length() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    public Vector normalize() {
        double length = length();

        this.x /= length;
        this.y /= length;
        this.z /= length;

        return this;
    }

    public double dot(Vector other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector cross(Vector other) {
        return new Vector(this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z, this.x * other.y - this.y * other.x);
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", this.x, this.y, this.z);
    }
}
