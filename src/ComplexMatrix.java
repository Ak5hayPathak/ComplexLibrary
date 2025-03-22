import java.util.concurrent.atomic.AtomicInteger;

public final class ComplexMatrix extends Complex{

    private ComplexMatrix() {
        throw new UnsupportedOperationException("Cannot instantiate ComplexMatrix.");
    }

    public static int setRow(){
        return IO.getIntInRange(1, "Enter the number of rows: ");
    }

    public static int setColumn(){
        return IO.getIntInRange(1, "Enter the number of columns: ");
    }

    public static Complex[][] inputMatrix(){
        int row = setRow();
        int column = setColumn();

        Complex[][] matrix = new Complex[row][column];

        System.out.println("\nEnter values. For complex numbers, separate real and imaginary parts with a comma\n(e.g., 3,4 for 3 + 4i).");
        for(int i=0; i<row; i++){
            for (int j=0; j<column; j++){
                matrix[i][j] = Complex.inputComplex("Enter value(" + (i + 1) + ", " + (j + 1) + "): ");
            }
        }
        return matrix;
    }

    public static boolean isMatrixNull(Complex[][] matrix){
        return matrix == null || matrix.length == 0 || matrix[0].length == 0;
    }

    private static int computeMaxWidth(Complex[][] matrix, int precision) {
        AtomicInteger maxWidth = new AtomicInteger(0);

        Thread widthCalculator = new Thread(() -> {
            int localMaxWidth = 0;
            for (Complex[] row : matrix) {
                for (Complex num : row) {
                    String formatted = num.formatComplex(precision);
                    localMaxWidth = Math.max(localMaxWidth, formatted.length());
                }
            }
            maxWidth.set(localMaxWidth);
        });

        widthCalculator.start();
        try {
            widthCalculator.join(); // Wait for thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return maxWidth.get();
    }

    public static void printMatrix(Complex[][] matrix, int precision) {
        if (!isMatrixNull(matrix)) {
            int columnWidth = computeMaxWidth(matrix, precision) + 3; // Add padding

            for (Complex[] row : matrix) {
                for (Complex num : row) {
                    System.out.printf("%-" + columnWidth + "s", num.formatComplex(precision));
                }
                System.out.println();
            }
        } else {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }
    }

    public static void printMatrix(Complex[][] matrix){
        printMatrix(matrix, 3);
    }

    public static Complex[][] identityMatrix(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Matrix size must be positive.");
        }

        Complex[][] identity = new Complex[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                identity[i][j] = (i == j) ? Complex.ONE : new Complex(0, 0);
            }
        }

        return identity;
    }

    public static Complex[][] negativeIdentityMatrix(int size) {
        return constProduct(identityMatrix(size), Complex.NEG_ONE);
    }


    public static Complex[][] zeroMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Matrix dimensions must be positive.");
        }

        Complex[][] zeroMatrix = new Complex[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                zeroMatrix[i][j] = Complex.ZERO;
            }
        }

        return zeroMatrix;
    }

    public static Complex[][] copyMatrix(Complex[][] matrix) {
        if (isMatrixNull(matrix)) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        Complex[][] copy = new Complex[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = new Complex(matrix[i][j]); // Deep Copy
            }
        }
        return copy;
    }

    public static Complex[][] transpose(Complex[][] matrix){
        if(isMatrixNull(matrix)){
            throw new IllegalArgumentException("Matrix is empty or null.");
        }
        int row = matrix.length;
        int column = matrix[0].length;

        Complex[][] transpose = new Complex[column][row];
        for(int i=0; i<row; i++){
            for(int j=0; j<column; j++){
                transpose[j][i] = matrix[i][j];
            }
        }
        return transpose;
    }

    public static Complex[][] addMatrix(Complex[][] matrix1, Complex[][] matrix2) {

        if (isMatrixNull(matrix1)) {
            throw new IllegalArgumentException("Matrix 1 is empty or null.");
        }
        if (isMatrixNull(matrix2)) {
            throw new IllegalArgumentException("Matrix 2 is empty or null.");
        }

        if (matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length) {
            throw new IllegalArgumentException("Addition is not possible because the dimensions of the matrices are different.");
        }

        Complex[][] sum = new Complex[matrix1.length][matrix1[0].length];

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                sum[i][j] = ComplexMath.add(matrix1[i][j], matrix2[i][j]);
            }
        }

        return sum;
    }

    public static Complex[][] subtractMatrix(Complex[][] matrix1, Complex[][] matrix2) {

        if (isMatrixNull(matrix1)) {
            throw new IllegalArgumentException("Matrix 1 is empty or null.");
        }
        if (isMatrixNull(matrix2)) {
            throw new IllegalArgumentException("Matrix 2 is empty or null.");
        }

        if (matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length) {
            throw new IllegalArgumentException("Subtraction is not possible because the dimensions of the matrices are different.");
        }

        Complex[][] sub = new Complex[matrix1.length][matrix1[0].length];

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                sub[i][j] = ComplexMath.subtract(matrix1[i][j], matrix2[i][j]);
            }
        }
        return sub;
    }

    public static Complex[][] productMatrix(Complex[][] matrix1, Complex[][] matrix2) {
        if (isMatrixNull(matrix1) || isMatrixNull(matrix2)) {
            throw new IllegalArgumentException("One or both matrices are empty or null.");
        }
        if (matrix1[0].length != matrix2.length) {
            throw new IllegalArgumentException("Multiplication not possible: Column count of Matrix 1 must match row count of Matrix 2.");
        }

        int rows = matrix1.length;
        int cols = matrix2[0].length;
        int common = matrix1[0].length;

        // Initialize product matrix
        Complex[][] product = product = zeroMatrix(rows, cols);

        // Transpose matrix2 for better memory locality
        Complex[][] matrix2T = transpose(matrix2);

        // Optimized multiplication loop
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < common; k++) {
                    product[i][j] = ComplexMath.add(product[i][j], ComplexMath.multiply(matrix1[i][k], matrix2T[j][k]));
                }
            }
        }
        return product;
    }


    public static Complex[][] powerMatrix(Complex[][] matrix, int power) {
        if (isMatrixNull(matrix)) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }
        if (matrix.length != matrix[0].length) {
            throw new IllegalArgumentException("Matrix exponentiation is only possible for square matrices.");
        }

        int n = matrix.length;

        // If power is 0, return identity matrix
        if (power == 0) {
            return identityMatrix(n);
        }

        // Initialize result as the Identity Matrix
        Complex[][] result = identityMatrix(n);
        Complex[][] temp;

        // Perform matrix exponentiation
        for (int p = 0; p < power; p++) {
            // Reset temp to store multiplication result
            temp = zeroMatrix(n, n);

            // Matrix multiplication: temp = matrix * result
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        temp[i][j] = ComplexMath.add(temp[i][j], ComplexMath.multiply(matrix[i][k], result[k][j]));
                    }
                }
            }

            // Swap references
            result = temp;
        }

        return result;
    }

    static Complex[][] constProduct(Complex[][] matrix, Complex element) {

        if (isMatrixNull(matrix)) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        Complex[][] result = new Complex[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = ComplexMath.multiply(element, matrix[i][j]);
            }
        }
        return result;
    }

    public static Complex traceMatrix(Complex[][] matrix) {
        if (isMatrixNull(matrix)) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }
        if (matrix.length != matrix[0].length) {
            throw new IllegalArgumentException("Trace does not exist because the matrix is not square.");
        }

        Complex traceSum = Complex.ZERO; // Assuming Complex.ZERO exists

        for (int i = 0; i < matrix.length; i++) {
            traceSum = ComplexMath.add(traceSum, matrix[i][i]); // Summing diagonal elements
        }
        return traceSum;
    }

    public static Complex[][] cofactor(Complex[][] matrix, int elrow, int elcol) {
        if (isMatrixNull(matrix)) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int n = matrix.length;
        if (n == 1) {
            throw new IllegalArgumentException("Cofactor does not exist for a 1×1 matrix.");
        }

        // Check if row/column input is valid (1-based indexing)
        if (elrow < 1 || elrow > n || elcol < 1 || elcol > n) {
            throw new IllegalArgumentException("Invalid row or column index. Must be between 1 and " + n);
        }

        // Convert 1-based input to 0-based indexing
        elrow -= 1;
        elcol -= 1;

        Complex[][] cofactor = new Complex[n - 1][n - 1];

        int cofactorRow = 0;
        for (int i = 0; i < n; i++) {
            if (i == elrow) continue; // Skip the given row

            int cofactorCol = 0;
            for (int j = 0; j < n; j++) {
                if (j == elcol) continue; // Skip the given column

                // Use the copy constructor to avoid reference issues
                cofactor[cofactorRow][cofactorCol++] = new Complex(matrix[i][j]);
            }
            cofactorRow++;
        }

        return cofactor;
    }

    public static Complex determinant(Complex[][] matrix) {
        int n = matrix.length;

        if (matrix.length != matrix[0].length) {
            throw new IllegalArgumentException("Determinant does not exist because the matrix is not square.");
        }

        // Create copies of matrix for LU decomposition
        Complex[][] U = new Complex[n][n];
        Complex[][] L = new Complex[n][n];

        // Initialize U as a copy of the input matrix and L as the identity matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                U[i][j] = new Complex(matrix[i][j]); // Copy matrix elements
                L[i][j] = (i == j) ? Complex.ONE : new Complex(); // Identity matrix
            }
        }

        // LU Decomposition using Gaussian elimination
        for (int k = 0; k < n; k++) {
            if (U[k][k].isNull()) {
                throw new ArithmeticException("Matrix is singular (det = 0).");
            }

            for (int i = k + 1; i < n; i++) {
                L[i][k] = ComplexMath.divide(U[i][k], U[k][k]); // Compute L

                for (int j = k; j < n; j++) {
                    U[i][j] = ComplexMath.subtract(U[i][j], ComplexMath.multiply(L[i][k], U[k][j])); // Update U
                }
            }
        }

        // Compute determinant as the product of diagonal elements of U
        Complex determinant = Complex.ONE;
        for (int i = 0; i < n; i++) {
            determinant = ComplexMath.multiply(determinant, U[i][i]);
        }

        return determinant;
    }

    public static Complex[][] adjoint(Complex[][] matrix) {
        if (isMatrixNull(matrix)) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }
        int n = matrix.length;
        if (n != matrix[0].length) {
            throw new IllegalArgumentException("Adjoint is only defined for square matrices.");
        }

        Complex[][] adjoint = new Complex[n][n];

        // Compute cofactor matrix and store it in transposed form
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Compute determinant of minor directly without recursion where possible
                Complex[][] cofactorMatrix = cofactor(matrix, i+1, j+1);
                Complex determinant = determinant(cofactorMatrix);

                // Compute adjoint[j][i] (directly storing transposed cofactor)
                Complex sign = ((i+1 + j+1) % 2 == 0) ? new Complex(1, 0) : new Complex(-1, 0);
                adjoint[j][i] = ComplexMath.multiply(sign, determinant);
            }
        }

        return adjoint;
    }

    public static Complex[][] inverseMatrix(Complex[][] matrix) {
        if (isMatrixNull(matrix)) {
            System.out.println("Matrix is empty or null.");
            return null;
        }

        int n = matrix.length;
        if (n != matrix[0].length) {
            throw new IllegalArgumentException("Inverse is only defined for square matrices.");
        }

        Complex determinant = determinant(matrix);

        if (determinant.equals(Complex.ZERO)) {
            throw new IllegalArgumentException("Inverse does not exist because the determinant is zero!");
        }

        Complex[][] adjoint = adjoint(matrix);

        // Multiply adjugate by 1/determinant
        return constProduct(adjoint, determinant.getReciprocal());
    }

    public static Complex[][] divideMatrix(Complex[][] matrix1, Complex[][] matrix2) {

        if (isMatrixNull(matrix1) || isMatrixNull(matrix2)) {
            System.out.println("Matrix is empty or null.");
            return null;
        }

        if (matrix1[0].length != matrix2.length) {
            System.out.println("Division is not possible because the number of columns in the first matrix\nis not equal to the number of rows in the second matrix!");
            return null;
        }

        Complex[][] inverse = inverseMatrix(matrix2);
        return productMatrix(matrix1, inverse);
    }
}