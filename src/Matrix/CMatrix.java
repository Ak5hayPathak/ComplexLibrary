package Matrix;
import Complex.*;
import IO.IO;

import java.util.concurrent.atomic.AtomicInteger;

public class CMatrix extends Complex{

    private final int rows, cols;
    private Complex[][] matrix;

    public CMatrix(Complex[][] matrix){
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        this.rows = matrix.length;
        this.cols = matrix[0].length;
        this.matrix = new Complex[this.rows][this.cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.matrix[i][j] = new Complex(matrix[i][j]); // Deep Copy
            }
        }
    }

    public CMatrix(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        if(isRowColLessThanUnit()){
            throw new ArithmeticException("Error: Rows/Columns must be greater than or equal to 1");
        }
        this.matrix = new Complex[this.rows][this.cols];
    }

    private boolean isRowColLessThanUnit(){
        return (this.rows < 1) || (this.cols < 1);
    }

    private boolean isRowColLessThanUnit(int row, int col){
        return (row < 1) || (col < 1);
    }

    private boolean rowColExceeded(int row, int col){
        return row > this.rows || col > this.cols;
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

    public Complex[][] getMatrix(){
        return this.matrix;
    }

    public void setElement(int row, int col, Complex element){
        if(row < 0 || col < 0){
            throw new ArithmeticException("Error: Row/Column must be greater than or equal to 1");
        }
        else if(this.rowColExceeded(row, col)){
            throw new ArithmeticException("Error: Row/Column must not be greater than the matrix dimensions");
        }

        this.matrix[row][col] = element;
    }

    public Complex getElement(int row, int col){
        if(row < 0 || col < 0){
            throw new ArithmeticException("Error: Row/Column must be greater than or equal to 1");
        }
        else if(this.rowColExceeded(row, col)){
            throw new ArithmeticException("Error: Row/Column must not be greater than the matrix dimensions");
        }
        return this.matrix[row][col];
    }

    public void inputMatrix(){

        if(this.isMatrixNull()){
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        System.out.println("Enter values");
        System.out.println("For complex numbers enter values in one of the following ways:");
        System.out.println(" - As two values separated by a comma: (a, b) -> a + bi");
        System.out.println(" - In standard complex format: a + bi or a - bi");
        System.out.println();

        for(int i=0; i<this.rows; i++){
            for (int j=0; j<this.cols; j++){
                this.matrix[i][j] = Complex.inputComplex("Enter value(" + (i + 1) + ", " + (j + 1) + "): ");
            }
        }
    }

    public boolean isMatrixZero() {
        for (Complex[] row : this.matrix) {
            for (Complex element : row) {
                if (!element.isZero()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int computeMaxWidth(Complex[][] matrix, int precision) {
        AtomicInteger maxWidth = new AtomicInteger(0);

        Thread widthCalculator = new Thread(() -> {
            int localMaxWidth = 0;
            for (Complex[] row : matrix) {
                for (Complex num : row) {
                    String formatted = formatComplex(num, precision);
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

    public void printMatrix(int precision) {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }
        int columnWidth = computeMaxWidth(matrix, precision) + 3; // Add padding

        for (Complex[] row : matrix) {
            for (Complex num : row) {
                System.out.printf("%-" + columnWidth + "s", formatComplex(num, precision));
            }
            System.out.println();
        }
    }

    public void printMatrix(){
        this.printMatrix(3);
    }

    public CMatrix getCopyMatrix() {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int rows = this.matrix.length;
        int cols = this.matrix[0].length;
        Complex[][] copy = new Complex[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = new Complex(this.matrix[i][j]); // Deep Copy
            }
        }
        return new CMatrix(copy);
    }

    public CMatrix getTranspose(){
        if(this.isMatrixNull()){
            throw new IllegalArgumentException("Matrix is empty or null.");
        }
        int row = this.matrix.length;
        int column = this.matrix[0].length;

        Complex[][] transpose = new Complex[column][row];
        for(int i=0; i<row; i++){
            for(int j=0; j<column; j++){
                transpose[j][i] = this.matrix[i][j];
            }
        }
        return new CMatrix(transpose);
    }

    public CMatrix constProduct(Complex element) {

        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int rows = this.matrix.length;
        int cols = this.matrix[0].length;
        Complex[][] result = new Complex[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = ComplexMath.multiply(element, this.matrix[i][j]);
            }
        }
        return new CMatrix(result);
    }

    public Complex getTrace() {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }
        if (this.matrix.length != this.matrix[0].length) {
            throw new IllegalArgumentException("Trace does not exist because the matrix is not square.");
        }

        Complex traceSum = Complex.ZERO; // Assuming Complex.ZERO exists

        for (int i = 0; i < this.matrix.length; i++) {
            traceSum = ComplexMath.add(traceSum, this.matrix[i][i]); // Summing diagonal elements
        }
        return traceSum;
    }

    public CMatrix getCofactor(int elrow, int elcol) {
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

        Complex[][] cofactor = new Complex[n - 1][n - 1];

        int cofactorRow = 0;
        for (int i = 0; i < n; i++) {
            if (i == elrow) continue; // Skip the given row

            int cofactorCol = 0;
            for (int j = 0; j < n; j++) {
                if (j == elcol) continue; // Skip the given column

                // Use the copy constructor to avoid reference issues
                cofactor[cofactorRow][cofactorCol++] = new Complex(this.matrix[i][j]);
            }
            cofactorRow++;
        }
        return new CMatrix(cofactor);
    }

    public Complex getDeterminant() {
        int n = this.matrix.length;

        if (this.matrix.length != this.matrix[0].length) {
            throw new IllegalArgumentException("Determinant does not exist because the matrix is not square.");
        }

        // Create copies of matrix for LU decomposition
        Complex[][] U = new Complex[n][n];
        Complex[][] L = new Complex[n][n];

        // Initialize U as a copy of the input matrix and L as the identity matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                U[i][j] = new Complex(this.matrix[i][j]); // Copy matrix elements
                L[i][j] = (i == j) ? Complex.ONE : new Complex(); // Identity matrix
            }
        }

        // LU Decomposition using Gaussian elimination
        for (int k = 0; k < n; k++) {
            if (U[k][k].isZero()) {
                return Complex.ZERO;
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

    public CMatrix getAdjoint() {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }
        int n = this.matrix.length;
        if (n != this.matrix[0].length) {
            throw new IllegalArgumentException("Adjoint is only defined for square matrices.");
        }

        Complex[][] adjoint = new Complex[n][n];

        // Compute cofactor matrix and store it in transposed form
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Compute determinant of minor directly without recursion where possible
                CMatrix cofactor = this.getCofactor(i+1, j+1);
                Complex determinant = cofactor.getDeterminant();

                // Compute adjoint[j][i] (directly storing transposed cofactor)
                Complex sign = ((i+1 + j+1) % 2 == 0) ? new Complex(1, 0) : new Complex(-1, 0);
                adjoint[j][i] = ComplexMath.multiply(sign, determinant);
            }
        }

        return new CMatrix(adjoint);
    }

    public CMatrix getInverseMatrix() {
        if (this.isMatrixNull()) {
            throw new IllegalArgumentException("Matrix is empty or null.");
        }

        int n = this.matrix.length;
        if (n != this.matrix[0].length) {
            throw new IllegalArgumentException("Inverse is only defined for square matrices.");
        }

        // Create an augmented matrix [A | I]
        Complex[][] augmented = new Complex[n][2 * n];
        for (int i = 0; i < n; i++) {
            // Copy A
            System.arraycopy(this.matrix[i], 0, augmented[i], 0, n);
            for (int j = n; j < 2 * n; j++) {
                augmented[i][j] = (i == j - n) ? Complex.ONE : Complex.ZERO; // Identity matrix
            }
        }

        // Perform Gaussian elimination to convert [A | I] into [I | A^-1]
        for (int i = 0; i < n; i++) {
            // Find the pivot row
            int pivotRow = i;
            for (int k = i + 1; k < n; k++) {
                if (augmented[k][i].getMod() > augmented[pivotRow][i].getMod()) {
                    pivotRow = k;
                }
            }

            // Swap rows if necessary
            if (i != pivotRow) {
                Complex[] temp = augmented[i];
                augmented[i] = augmented[pivotRow];
                augmented[pivotRow] = temp;
            }

            // Check if the matrix is singular
            if (augmented[i][i].equals(Complex.ZERO)) {
                throw new IllegalArgumentException("Matrix is singular and cannot be inverted.");
            }

            // Normalize the pivot row
            Complex pivotValue = augmented[i][i];
            for (int j = 0; j < 2 * n; j++) {
                augmented[i][j] = ComplexMath.divide(augmented[i][j], pivotValue);
            }

            // Eliminate the other rows
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    Complex factor = augmented[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmented[k][j] = ComplexMath.subtract(augmented[k][j], ComplexMath.multiply(factor, augmented[i][j]));
                    }
                }
            }
        }

        // Extract the inverse matrix from [I | A^-1]
        Complex[][] inverseMatrix = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(augmented[i], n, inverseMatrix[i], 0, n);
        }

        return new CMatrix(inverseMatrix);
    }


}
