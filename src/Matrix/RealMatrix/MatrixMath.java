package Matrix.RealMatrix;

public final class MatrixMath {

    private MatrixMath() {
        throw new UnsupportedOperationException("Cannot instantiate MatrixMath.");
    }

    public static RMatrix add(RMatrix matrix1, RMatrix matrix2) {
        if (matrix1 == null || matrix1.getMatrix() == null) {
            throw new IllegalArgumentException("Matrix 1 is null or not initialized.");
        }
        if (matrix2 == null || matrix2.getMatrix() == null) {
            throw new IllegalArgumentException("Matrix 2 is null or not initialized.");
        }

        if (matrix1.getRows() != matrix2.getRows() || matrix1.getColumns() != matrix2.getColumns()) {
            throw new IllegalArgumentException("Addition is not possible because the dimensions of the matrices are different.");
        }

        int rows = matrix1.getRows();
        int cols = matrix1.getColumns();

        double[][] sum = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum[i][j] = matrix1.getElement(i, j) + matrix2.getElement(i, j);
            }
        }

        return new RMatrix(sum);
    }

    public static RMatrix subtract(RMatrix matrix1, RMatrix matrix2) {
        if (matrix1 == null || matrix1.getMatrix() == null) {
            throw new IllegalArgumentException("Matrix 1 is null or not initialized.");
        }
        if (matrix2 == null || matrix2.getMatrix() == null) {
            throw new IllegalArgumentException("Matrix 2 is null or not initialized.");
        }

        if (matrix1.getRows() != matrix2.getRows() || matrix1.getColumns() != matrix2.getColumns()) {
            throw new IllegalArgumentException("Subtraction is not possible because the dimensions of the matrices are different.");
        }

        int rows = matrix1.getRows();
        int cols = matrix1.getColumns();

        double[][] sub = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sub[i][j] = matrix1.getElement(i, j) - matrix2.getElement(i, j);
            }
        }

        return new RMatrix(sub);
    }

    public static RMatrix product(RMatrix matrix1, RMatrix matrix2) {
        if (matrix1 == null || matrix1.getMatrix() == null || matrix2 == null || matrix2.getMatrix() == null) {
            throw new IllegalArgumentException("One or both matrices are null or not initialized.");
        }

        if (matrix1.getColumns() != matrix2.getRows()) {
            throw new IllegalArgumentException("Multiplication not possible: Column count of Matrix 1 must match row count of Matrix 2.");
        }

        int rows = matrix1.getRows();
        int cols = matrix2.getColumns();
        int common = matrix1.getColumns();

        // Initialize product matrix
        double[][] product = new double[rows][cols];

        // Transpose matrix2 for better memory locality
        RMatrix matrix2T = matrix2.getTranspose();

        // Optimized multiplication loop
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double sum = 0.0;
                for (int k = 0; k < common; k++) {
                    sum += matrix1.getElement(i, k) * matrix2T.getElement(j, k);
                }
                product[i][j] = sum;
            }
        }

        return new RMatrix(product);
    }

    public static RMatrix powerInt(RMatrix matrix, int power) {
        if (matrix == null || matrix.getMatrix() == null) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int n = matrix.getRows();

        if (n != matrix.getColumns()) {
            throw new IllegalArgumentException("Matrix exponentiation is only possible for square matrices.");
        }

        // Identity matrix for power 0
        if (power == 0) {
            return identityMatrix(n);
        }

        // Copy base matrix
        RMatrix base = matrix;
        RMatrix result = identityMatrix(n);

        while (power > 0) {
            if ((power & 1) == 1) {
                result = multiplyMatrices(result, base);
            }
            base = multiplyMatrices(base, base);
            power >>= 1;
        }

        return result;
    }

    // Helper method for matrix multiplication
    private static RMatrix multiplyMatrices(RMatrix A, RMatrix B) {
        int n = A.getRows();
        double[][] result = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                for (int k = 0; k < n; k++) {
                    sum += A.getElement(i, k) * B.getElement(k, j);
                }
                result[i][j] = sum;
            }
        }

        return new RMatrix(result);
    }

    // Helper to create an identity matrix
    public static RMatrix identityMatrix(int size) {
        double[][] identity = new double[size][size];
        for (int i = 0; i < size; i++) {
            identity[i][i] = 1.0;
        }
        return new RMatrix(identity);
    }

    public static RMatrix divide(RMatrix matrix1, RMatrix matrix2) {
        if (matrix1 == null || matrix1.getMatrix() == null ||
                matrix2 == null || matrix2.getMatrix() == null) {
            System.out.println("Matrix is empty or null.");
            return null;
        }

        if (matrix1.getColumns() != matrix2.getRows()) {
            System.out.println("Division is not possible because the number of columns in the first matrix\n" +
                    "is not equal to the number of rows in the second matrix!");
            return null;
        }

        RMatrix inverse = matrix2.getInverseMatrix();
        return product(matrix1, inverse);
    }
}