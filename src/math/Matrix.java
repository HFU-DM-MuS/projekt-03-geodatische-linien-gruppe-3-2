package math;

public class Matrix {
    // rows
    private final int m;
    // columns
    private final int n;

    private final double[][] data;

    public Matrix(int m, int n) {
        this.m = m;
        this.n = n;
        this.data = new double[m][n];
    }

    public Matrix(double[][] data) {
        this.m = data.length;
        this.n = data[0].length;
        this.data = new double[m][n];
        for (int row = 0; row < this.m; row++) {
            for (int col = 0; col < this.n; col++) {
                this.data[row][col] = data[row][col];
            }
        }
    }

    public Matrix(Matrix other) {
        this(other.data);
    }

    public Matrix multiply(Matrix other) throws Exception {
        if (this.n != other.m) {
            throw new Exception("Matrix dimensions do not fit");
        }

        Matrix result = new Matrix(this.m, other.n);

        for (int row = 0; row < result.m; row++) {
            for (int col = 0; col < result.n; col++) {
                for (int i = 0; i < this.n; i++) {
                    result.data[row][col] += this.data[row][i] * other.data[i][col];
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        String result = "[\n";

        for (int row = 0; row < this.m; row++) {
            for (int col = 0; col < this.n; col++) {
                result += this.data[row][col] + " ";
            }
            result += "\n";
        }
        result += "]";

        return result;
    }
}
