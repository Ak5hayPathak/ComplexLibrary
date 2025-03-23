import java.util.Random;

public class ComplexRandom {

    private ComplexRandom() {
        throw new UnsupportedOperationException("Cannot instantiate ComplexRandom.");
    }

    private static final Random rand = new Random();

    private static double getRandomDouble(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    private static int getRandomInt(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }

    // Random complex number with both real and imaginary parts
    public static Complex randomComplex(double minReLimit, double maxReLimit, double minImLimit,  double maxImLimit, boolean isInteger) {
        if (isInteger) {
            return new Complex(getRandomInt((int) minReLimit, (int) maxReLimit),
                    getRandomInt((int) minImLimit, (int) maxImLimit));
        } else {
            return new Complex(getRandomDouble(minReLimit, maxReLimit),
                    getRandomDouble(minImLimit, maxImLimit));
        }
    }

    public static Complex randomReal(double minLimit, double maxLimit, boolean isInteger) {
        return isInteger?
                new Complex(getRandomInt((int) minLimit, (int) maxLimit), 0):
                new Complex(getRandomDouble(minLimit, maxLimit), 0);
    }

    public static Complex randomImaginary(double minLimit, double maxLimit, boolean isInteger) {
        return isInteger?
                new Complex(0, getRandomInt((int) minLimit, (int) maxLimit)):
                new Complex(0, getRandomDouble(minLimit, maxLimit));
    }

    public static QuadraticEquation randomComplexQuadratic(double minReLimit, double maxReLimit, double minImLimit, double maxImLimit, boolean isInteger){
        if(isInteger){
            return new QuadraticEquation(
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, true),
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, true),
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, true)
            );
        }else{
            return new QuadraticEquation(
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, false),
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, false),
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, false)
            );
        }
    }

    public static QuadraticEquation randomRealQuadratic(double minLimit, double maxLimit, boolean isInteger){
        if(isInteger){
            return new QuadraticEquation(
                    randomReal(minLimit, maxLimit, true),
                    randomReal(minLimit, maxLimit, true),
                    randomReal(minLimit, maxLimit, true)
            );
        }else{
            return new QuadraticEquation(
                    randomReal(minLimit, maxLimit, false),
                    randomReal(minLimit, maxLimit, false),
                    randomReal(minLimit, maxLimit, false)
            );
        }
    }

    public static QuadraticEquation randomImaginaryinaQuadratic(double minLimit, double maxLimit, boolean isInteger){
        if(isInteger){
            return new QuadraticEquation(
                    randomImaginary(minLimit, maxLimit, true),
                    randomImaginary(minLimit, maxLimit, true),
                    randomImaginary(minLimit, maxLimit, true)
            );
        }else{
            return new QuadraticEquation(
                    randomImaginary(minLimit, maxLimit, false),
                    randomImaginary(minLimit, maxLimit, false),
                    randomImaginary(minLimit, maxLimit, false)
            );
        }
    }

    public static CubicEquation randomCompelxCubic(double minReLimit, double maxReLimit, double minImLimit, double maxImLimit, boolean isInteger){
        if(isInteger){
            return new CubicEquation(
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, true),
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, true),
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, true),
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, true)
            );
        }else{
            return new CubicEquation(
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, false),
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, false),
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, false),
                    randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, false)
            );
        }
    }

    public static CubicEquation randomRealCubic(double minLimit, double maxLimit, boolean isInteger){
        if(isInteger){
            return new CubicEquation(
                    randomReal(minLimit, maxLimit, true),
                    randomReal(minLimit, maxLimit, true),
                    randomReal(minLimit, maxLimit, true),
                    randomReal(minLimit, maxLimit, true)
            );
        }else{
            return new CubicEquation(
                    randomReal(minLimit, maxLimit, false),
                    randomReal(minLimit, maxLimit, false),
                    randomReal(minLimit, maxLimit, false),
                    randomReal(minLimit, maxLimit, false)
            );
        }
    }

    public static CubicEquation randomImaginaryCubic(double minLimit, double maxLimit, boolean isInteger){
        if(isInteger){
            return new CubicEquation(
                    randomImaginary(minLimit, maxLimit, true),
                    randomImaginary(minLimit, maxLimit, true),
                    randomImaginary(minLimit, maxLimit, true),
                    randomImaginary(minLimit, maxLimit, true)
            );
        }else{
            return new CubicEquation(
                    randomImaginary(minLimit, maxLimit, false),
                    randomImaginary(minLimit, maxLimit, false),
                    randomImaginary(minLimit, maxLimit, false),
                    randomImaginary(minLimit, maxLimit, false)
            );
        }
    }

    public static Complex[][] randomComplexMatrix(int row, int column, double minReLimit, double maxReLimit, double minImLimit, double maxImLimit ,boolean isInteger) {
        Complex[][] random = new Complex[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                random[i][j] = randomComplex(minReLimit, maxReLimit, minImLimit, maxImLimit, isInteger);
            }
        }
        return random;
    }

    public static Complex[][] randomRealMatrix(int row, int column, double minLimit, double maxLimit, boolean isInteger) {
        Complex[][] random = new Complex[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                random[i][j] = randomReal(minLimit, maxLimit, isInteger);
            }
        }
        return random;
    }

    public static Complex[][] randomImaginaryMatrix(int row, int column, double minLimit, double maxLimit, boolean isInteger) {
        Complex[][] random = new Complex[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                random[i][j] = randomImaginary(minLimit, maxLimit, isInteger);
            }
        }
        return random;
    }
}