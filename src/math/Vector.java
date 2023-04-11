package math;

public class Vector {
    private double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(double x, double y) {
        this(x, y, 0.0);
    }

    public Vector() {
        this(.0, .0, .0);
    }

    public Vector(Vector other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
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

        return new Vector(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector scale(double f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;

        return this;
    }

    public Vector multiply(double f) {
        return new Vector(this.x * f, this.y * f, this.z * f);
    }

    public Vector divide(double d) {
        this.x /= d;
        this.y /= d;
        this.z /= d;

        return new Vector(this.x / d, this.y / d, this.z / d);
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
        return new Vector(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public Vector rotateWorldX(double angle) {

        angle = Math.toRadians(angle);

        Matrix rotMat = new Matrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, Math.cos(angle), -Math.sin(angle)},
                {0.0, Math.sin(angle), Math.cos(angle)}
        });

        this.rotateWithMatrix(rotMat);

        return this;
    }

    public Vector rotateWorldY(double angle) {

        angle = Math.toRadians(angle);

        Matrix rotMat = new Matrix(new double[][]{
                {Math.cos(angle), 0.0, Math.sin(angle)},
                {0.0, 1.0, 0.0},
                {-Math.sin(angle), 0.0, Math.cos(angle)}
        });

        this.rotateWithMatrix(rotMat);

        return this;
    }

    public Vector rotateWorldZ(double angle) {

        angle = Math.toRadians(angle);

        Matrix rotMat = new Matrix(new double[][]{
                {Math.cos(angle), -Math.sin(angle), 0.0},
                {Math.sin(angle), Math.cos(angle), 0.0},
                {0.0, 0.0, 1.0}
        });

        this.rotateWithMatrix(rotMat);

        return this;
    }

    public Vector copy() {
        return new Vector(this);
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", this.x, this.y, this.z);
    }

    private void rotateWithMatrix(Matrix m) {
        try {
            Matrix rotated = m.multiply(new Matrix(new double[][]{
                    {this.x},
                    {this.y},
                    {this.z}
            }));

            this.x = rotated.get(0, 0);
            this.y = rotated.get(0, 1);
            this.z = rotated.get(0, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
