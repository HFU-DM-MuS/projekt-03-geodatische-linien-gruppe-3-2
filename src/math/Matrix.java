package math;

public class Matrix {
    // rows
    private final int rows;
    // columns
    private final int cols;

    private final double[][] data;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matrix(double[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new double[rows][cols];
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                this.data[row][col] = data[row][col];
            }
        }
    }

    public Matrix(Matrix other) {
        this(other.data);
    }

    public double get(int col, int row) {
        return this.data[row][col];
    }

    public Matrix multiply(Matrix other) throws Exception {
        if (this.cols != other.rows) {
            throw new Exception("Matrix dimensions do not fit");
        }

        Matrix result = new Matrix(this.rows, other.cols);

        for (int row = 0; row < result.rows; row++) {
            for (int col = 0; col < result.cols; col++) {
                for (int i = 0; i < this.cols; i++) {
                    result.data[row][col] += this.data[row][i] * other.data[i][col];
                }
            }
        }

        return result;
    }

    public Vector doScreenProjection(Vector v) throws Exception {
        Matrix vm = new Matrix(new double[][]{
                {v.x()},
                {v.y()},
                {v.z()},
                {1.0}
        });

        Matrix result = this.multiply(vm);

        return new Vector(
                result.data[0][0],
                result.data[1][0],
                0
        );
    }

    public Vector multiply(Vector v) throws Exception {
        if (this.cols != 3) {
            throw new Exception("Only matrices with 3 columns can be multiplied with a (3D-)Vector");
        }

        Matrix m = this.multiply(new Matrix(new double[][]{
                {v.x()},
                {v.y()},
                {v.z()}
        }));

        return new Vector(
                m.data[0][0],
                m.data[1][0],
                0.0
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");

        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                sb.append(this.data[row][col]).append(" ");
            }
            sb.append("\n");
        }
        sb.append("]");

        return sb.toString();
    }
}
