package Matrix.RealMatrix;

import java.util.Objects;
import java.util.Random;
import IO.IO;

public class RMatrix {

    private final int rows, cols;
    private double[][] matrix;

    public RMatrix(double[][] matrix){
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        this.rows = matrix.length;
        this.cols = matrix[0].length;
        this.matrix = new double[this.rows][this.cols];

        for (int i = 0; i < rows; i++) {
            // Deep Copy
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, cols);
        }
    }

    public RMatrix(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        if (isRowColLessThanUnit()) {
            throw new ArithmeticException("Error: Rows/Columns must be greater than or equal to 1");
        }
        this.matrix = new double[this.rows][this.cols];
    }

    private boolean isRowColLessThanUnit(){
        return (this.rows < 1) || (this.cols < 1);
    }

    private boolean isRowColLessThanUnit(int row, int col){
        return (row < 1) || (col < 1);
    }

    private boolean rowColExceeded(int row, int col){
        return row >= this.rows || col >= this.cols;
    }

    public static int setRow(){
        return IO.getIntInRange(1, "Enter the number of rows: ");
    }

    public static int setColumn(){
        return IO.getIntInRange(1, "Enter the number of columns: ");
    }

    public int getRows(){
        return this.rows;
    }

    public int getColumns(){
        return this.cols;
    }

    public boolean isMatrixNull(){
        return this.matrix == null || this.matrix.length == 0 || this.matrix[0].length == 0;
    }

    public double[][] getMatrix(){
        return this.matrix;
    }

    public void setElement(int row, int col, double element){
        if (row < 0 || col < 0) {
            throw new ArithmeticException("Error: Row/Column must be greater than or equal to 1");
        } else if (this.rowColExceeded(row, col)) {
            throw new ArithmeticException("Error: Row/Column must not be greater than the matrix dimensions");
        }

        this.matrix[row][col] = element;
    }

    public double getElement(int row, int col){
        if (row < 0 || col < 0) {
            throw new ArithmeticException("Error: Row/Column must be greater than or equal to 1");
        } else if (this.rowColExceeded(row, col)) {
            throw new ArithmeticException("Error: Row/Column must not be greater than the matrix dimensions");
        }
        return this.matrix[row][col];
    }

    public void inputMatrix(){
        if(this.isMatrixNull()){
            throw new IllegalArgumentException("Matrix is empty or null.");
        }
        System.out.println("Enter values:");
        for(int i = 0; i < this.rows; i++){
            for (int j = 0; j < this.cols; j++){
                this.matrix[i][j] = IO.getDouble("Enter value(" + (i + 1) + ", " + (j + 1) + "): ");
            }
        }
    }

    public boolean isMatrixZero() {
        for (double[] row : this.matrix) {
            for (double element : row) {
                if (element != 0.0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int computeMaxWidth(double[][] matrix, int precision) {
        int maxWidth = 0;

        for (double[] row : matrix) {
            for (double num : row) {
                // Always show sign so we reserve space for negative symbol
                String formatted = String.format("%." + precision + "f", num);
                maxWidth = Math.max(maxWidth, formatted.length());
            }
        }

        return maxWidth;
    }

    public void printMatrix(int precision) {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int columnWidth = computeMaxWidth(matrix, precision) + 1; // Padding

        for (double[] row : matrix) {
            for (double num : row) {
                // Print without the plus sign
                System.out.printf("%" + columnWidth + "." + precision + "f", num);
            }
            System.out.println();
        }
    }

    public void printMatrix(){
        this.printMatrix(3);

    }

    public RMatrix getCopyMatrix() {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int rows = this.matrix.length;
        int cols = this.matrix[0].length;
        double[][] copy = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            // Deep Copy
            System.arraycopy(this.matrix[i], 0, copy[i], 0, cols);
        }
        return new RMatrix(copy);
    }

    public RMatrix getTranspose() {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int row = this.matrix.length;
        int column = this.matrix[0].length;
        double[][] transpose = new double[column][row];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                transpose[j][i] = this.matrix[i][j];
            }
        }
        return new RMatrix(transpose);
    }

    public RMatrix constProduct(double element) {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int rows = this.matrix.length;
        int cols = this.matrix[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = element * this.matrix[i][j];
            }
        }
        return new RMatrix(result);
    }

    public double getTrace() {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }
        if (this.matrix.length != this.matrix[0].length) {
            throw new IllegalArgumentException("Trace does not exist because the matrix is not square.");
        }

        double traceSum = 0.0;

        for (int i = 0; i < this.matrix.length; i++) {
            traceSum += this.matrix[i][i]; // Summing diagonal elements
        }
        return traceSum;
    }

    public RMatrix getCofactor(int elrow, int elcol) {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int n = this.matrix.length;
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

        double[][] cofactor = new double[n - 1][n - 1];

        int cofactorRow = 0;
        for (int i = 0; i < n; i++) {
            if (i == elrow) continue; // Skip the given row

            int cofactorCol = 0;
            for (int j = 0; j < n; j++) {
                if (j == elcol) continue; // Skip the given column

                cofactor[cofactorRow][cofactorCol++] = this.matrix[i][j];
            }
            cofactorRow++;
        }
        return new RMatrix(cofactor);
    }

    public double getDeterminant() {
        int n = this.matrix.length;

        if (this.matrix.length != this.matrix[0].length) {
            throw new IllegalArgumentException("Determinant does not exist because the matrix is not square.");
        }

        // Create a copy of the matrix for LU decomposition
        double[][] U = new double[n][n];

        // Initialize U as a copy of the input matrix
        for (int i = 0; i < n; i++) {
            // Copy matrix elements
            System.arraycopy(this.matrix[i], 0, U[i], 0, n);
        }

        double determinant = 1.0;

        // LU Decomposition using Gaussian elimination
        for (int k = 0; k < n; k++) {
            if (U[k][k] == 0.0) {
                return 0.0; // Determinant is zero if a pivot element is zero
            }

            for (int i = k + 1; i < n; i++) {
                double factor = U[i][k] / U[k][k];

                for (int j = k; j < n; j++) {
                    U[i][j] -= factor * U[k][j]; // Update U
                }
            }
        }
        // Compute determinant as the product of diagonal elements of U
        for (int i = 0; i < n; i++) {
            determinant *= U[i][i];
        }

        return determinant;
    }

    public RMatrix getAdjoint() {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }
        int n = this.matrix.length;
        if (n != this.matrix[0].length) {
            throw new IllegalArgumentException("Adjoint is only defined for square matrices.");
        }

        double[][] adjoint = new double[n][n];

        // Compute cofactor matrix and store it in transposed form
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Compute determinant of minor directly without recursion where possible
                RMatrix cofactor = this.getCofactor(i + 1, j + 1);
                double determinant = cofactor.getDeterminant();

                // Compute adjoint[j][i] (directly storing transposed cofactor)
                double sign = ((i + 1 + j + 1) % 2 == 0) ? 1.0 : -1.0;
                adjoint[j][i] = sign * determinant;
            }
        }

        return new RMatrix(adjoint);
    }

    public RMatrix getInverseMatrix() {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int n = this.matrix.length;
        if (n != this.matrix[0].length) {
            throw new IllegalArgumentException("Inverse is only defined for square matrices.");
        }

        // Create an augmented matrix [A | I]
        double[][] augmented = new double[n][2 * n];
        for (int i = 0; i < n; i++) {
            // Copy A
            System.arraycopy(this.matrix[i], 0, augmented[i], 0, n);
            for (int j = n; j < 2 * n; j++) {
                augmented[i][j] = (i == j - n) ? 1.0 : 0.0; // Identity matrix
            }
        }

        // Perform Gaussian elimination to convert [A | I] into [I | A^-1]
        for (int i = 0; i < n; i++) {
            // Find the pivot row
            int pivotRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(augmented[k][i]) > Math.abs(augmented[pivotRow][i])) {
                    pivotRow = k;
                }
            }

            // Swap rows if necessary
            if (i != pivotRow) {
                double[] temp = augmented[i];
                augmented[i] = augmented[pivotRow];
                augmented[pivotRow] = temp;
            }

            // Check if the matrix is singular
            if (augmented[i][i] == 0.0) {
                throw new IllegalArgumentException("Matrix is singular and cannot be inverted.");
            }

            // Normalize the pivot row
            double pivotValue = augmented[i][i];
            for (int j = 0; j < 2 * n; j++) {
                augmented[i][j] /= pivotValue;
            }

            // Eliminate the other rows
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmented[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmented[k][j] -= factor * augmented[i][j];
                    }
                }
            }
        }

        // Extract the inverse matrix from [I | A^-1]
        double[][] inverseMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(augmented[i], n, inverseMatrix[i], 0, n);
        }

        return new RMatrix(inverseMatrix);
    }

    public void fillRandom(boolean useInteger, double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value.");
        }

        Random rand = new Random();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (useInteger) {
                    int randomInt = (int) (min + rand.nextInt((int) (max - min + 1)));
                    this.matrix[i][j] = randomInt;
                } else {
                    double randomDouble = min + (max - min) * rand.nextDouble();
                    this.matrix[i][j] = randomDouble;
                }
            }
        }
    }

    public void zeroMatrix() {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Matrix dimensions must be positive.");
        }

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.matrix[i][j] = 0.0;
            }
        }
    }

    public void identityMatrix() {
        if (this.rows != this.cols) {
            throw new IllegalArgumentException("Matrix must be square");
        }

        int size = this.rows;

        this.matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            matrix[i][i] = 1.0;
        }
    }

    public void negativeIdentityMatrix() {
        if (this.rows != this.cols) {
            throw new IllegalArgumentException("Matrix must be square");
        }

        int size = this.rows;
        this.matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            matrix[i][i] = -1.0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RMatrix other = (RMatrix) obj;
        if (this.rows != other.rows || this.cols != other.cols) return false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Double.compare(this.matrix[i][j], other.matrix[i][j]) != 0) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result = (int) (31 * result + matrix[i][j]);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Matrix ").append(rows).append("x").append(cols).append(":\n");
        for (int i = 0; i < rows; i++) {
            sb.append("[ ");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%.4f", matrix[i][j]));
                if (j < cols - 1) sb.append(", ");
            }
            sb.append(" ]\n");
        }
        return sb.toString();
    }
}