package Matrix.ComplexMatrix;

import Complex.*;

public final class CMatrixMath {

    private CMatrixMath() {
        super();
        throw new UnsupportedOperationException("Cannot instantiate MatrixMath.");
    }

    public static CMatrix zeroMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Matrix dimensions must be positive.");
        }

        Complex[][] zeroMatrix = new Complex[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                zeroMatrix[i][j] = Complex.ZERO;
            }
        }

        return new CMatrix(zeroMatrix);
    }

    public static CMatrix identityMatrix(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Matrix size must be positive.");
        }

        Complex[][] identity = new Complex[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                identity[i][j] = (i == j) ? Complex.ONE : new Complex(0, 0);
            }
        }
        return new CMatrix(identity);
    }

    public static CMatrix negativeIdentityMatrix(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Matrix size must be positive.");
        }

        Complex[][] negativeIdentity = new Complex[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                negativeIdentity[i][j] = (i == j) ? Complex.NEG_ONE : new Complex(0, 0);
            }
        }
        return new CMatrix(negativeIdentity);
    }

    public static CMatrix add(CMatrix matrix1, CMatrix matrix2) {

        if (matrix1.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix 1 is empty or null.");
        }
        if (matrix2.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix 2 is empty or null.");
        }

        if (matrix1.getRows() != matrix2.getRows() || matrix1.getColumns() != matrix2.getColumns()) {
            throw new IllegalArgumentException("Addition is not possible because the dimensions of the matrices are different.");
        }

        int rows = matrix1.getRows();
        int cols = matrix1.getColumns();

        Complex[][] sum = new Complex[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum[i][j] = ComplexMath.add(matrix1.getElement(i, j), matrix2.getElement(i, j));
            }
        }

        return new CMatrix(sum);
    }

    public static CMatrix subtract(CMatrix matrix1, CMatrix matrix2) {

        if (matrix1.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix 1 is empty or null.");
        }
        if (matrix2.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix 2 is empty or null.");
        }

        if (matrix1.getRows() != matrix2.getRows() || matrix1.getColumns() != matrix2.getColumns()) {
            throw new IllegalArgumentException("Subtraction is not possible because the dimensions of the matrices are different.");
        }

        int rows = matrix1.getRows();
        int cols = matrix1.getColumns();

        Complex[][] sub = new Complex[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sub[i][j] = ComplexMath.subtract(matrix1.getElement(i, j), matrix2.getElement(i, j));
            }
        }

        return new CMatrix(sub);
    }

    public static CMatrix product(CMatrix matrix1, CMatrix matrix2) {
        if (matrix1.isMatrixNull() || matrix2.isMatrixNull()) {
            throw new IllegalArgumentException("One or both matrices are empty or null.");
        }
        if (matrix1.getColumns() != matrix2.getRows()) {
            throw new IllegalArgumentException("Multiplication not possible: Column count of Matrix 1 must match row count of Matrix 2.");
        }

        int rows = matrix1.getRows();
        int cols = matrix2.getColumns();
        int common = matrix1.getColumns();

        // Initialize product matrix
        CMatrix product = zeroMatrix(rows, cols);

        // Transpose matrix2 for better memory locality
        CMatrix matrix2T = matrix2.getTranspose();
        Complex temp;

        // Optimized multiplication loop
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < common; k++) {
                    temp = ComplexMath.add(product.getElement(i, j), ComplexMath.multiply(matrix1.getElement(i, k), matrix2T.getElement(j, k)));
                    product.setElement(i,j, temp);
                }
            }
        }
        return product;
    }

    public static CMatrix powerInt(CMatrix matrix, int power) {
        if (matrix.isMatrixNull()) {
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

        // Copy the base matrix
        CMatrix base = matrix;
        CMatrix result = identityMatrix(n);

        while (power > 0) {
            if ((power & 1) == 1) { // If power is odd, multiply result with base
                result = multiplyMatrices(result, base);
            }
            base = multiplyMatrices(base, base); // Square the base
            power >>= 1; // Equivalent to power = power / 2
        }

        return result;
    }

    // Helper method for matrix multiplication
    private static CMatrix multiplyMatrices(CMatrix A, CMatrix B) {
        int n = A.getRows();
        CMatrix result = zeroMatrix(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Complex sum = new Complex(0, 0);
                for (int k = 0; k < n; k++) {
                    sum = ComplexMath.add(sum, ComplexMath.multiply(A.getElement(i, k), B.getElement(k, j)));
                }
                result.setElement(i, j, sum);
            }
        }

        return result;
    }

    public static CMatrix divide(CMatrix matrix1, CMatrix matrix2) {

        if (matrix1.isMatrixNull() || matrix2.isMatrixNull()) {
            System.out.println("Matrix is empty or null.");
            return null;
        }

        if (matrix1.getColumns() != matrix2.getRows()) {
            System.out.println("Division is not possible because the number of columns in the first matrix\nis not equal to the number of rows in the second matrix!");
            return null;
        }

        CMatrix inverse = matrix2.getInverseMatrix();
        return product(matrix1, inverse);
    }

}
