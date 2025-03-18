import java.util.Random;

public class Complex{
    protected double reNum, imNum;

    public enum Type {
        RE, IM, COMP;
    }

    Complex(){
        this(0.0, 0.0);
    }

    Complex(double reNum, double imNum){
        this.reNum = reNum;
        this.imNum = imNum;
    }

    Complex(double magnitude, double angle, boolean isPolar){
        this( (isPolar) ? magnitude * Math.cos(angle): magnitude,
                (isPolar) ? magnitude * Math.sin(angle): angle);
    }

    public Complex(Complex other) {
        this.reNum = other.reNum;
        this.imNum = other.imNum;
    }

    public boolean isNull() {
        return Math.abs(this.reNum) < EPSILON && Math.abs(this.imNum) < EPSILON;
    }
    public boolean isPureReal(){return (!this.isNull()) && (Math.abs(this.imNum) < EPSILON);}
    public boolean isPureImaginary(){return (!this.isNull()) && (Math.abs(this.reNum) < EPSILON);}

    public void setComplex(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) {
            throw new IllegalArgumentException("Real and imaginary parts must be valid numbers.");
        }
        this.reNum = a;
        this.imNum = b;
    }

    public static Complex inputComplex(String prompt) {

        while (true) {
            String input = IO.getString(prompt);

            String[] parts = input.split(",");

            try {
                if (parts.length == 1) {
                    // Single number (real part only)
                    double real = Double.parseDouble(parts[0].trim());
                    return new Complex(real, 0);
                } else if (parts.length == 2) {
                    // Two numbers (real and imaginary parts)
                    double real = Double.parseDouble(parts[0].trim());
                    double imaginary = Double.parseDouble(parts[1].trim());
                    return new Complex(real, imaginary);
                } else {
                    System.out.println("Invalid format! Please enter one or two values separated by a comma.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter valid numeric values.");
            }
        }
    }

    public void printComplex(int precision) {
        if (this.isNull()) {
            System.out.print("0");
        }

        // Format string for real and imaginary parts
        String format = "%." + precision + "f";

        if (this.isPureReal()) {
            // Pure real number
            System.out.printf(format, reNum);
        } else if (this.isPureImaginary()) {
            // Pure imaginary number
            if (Math.abs(imNum) == 1) {
                System.out.print(imNum > EPSILON ? "i" : "-i");
            } else {
                System.out.printf(imNum > EPSILON ? "i%s" : "-i%s", String.format(format, Math.abs(imNum)));
            }
        } else {
            // General complex number
            System.out.print(String.format(format, reNum) + (imNum > 0 ? " + " : " - ") +
                    (Math.abs(imNum) == 1 ? "i" : String.format(format, Math.abs(imNum)) + "i"));
        }
    }

    public void printComplex() {
        this.printComplex(3);
    }

    public void printPolar(int precision) {
        if (this.isNull()) {
            System.out.print("0");
            return;
        }

        double mod = this.getMod();
        double angle = this.getAngle();

        // Dynamically construct format string for precision
        String format = "%." + precision + "f";
        // Print magnitude
        System.out.print(String.format(format, mod) + "(cos(");
        // Print angle in cosine term
        System.out.print(String.format(format, angle) + ") ");
        // Print sine term with correct sign
        System.out.print((angle < 0 ? "- i sin(" : "+ i sin(") + String.format(format, Math.abs(angle)) + "))");
    }

    public void printPolar() {
        this.printPolar(3);
    }

    protected String formatComplex(int precision) {
        if (this.isNull()) return "0";

        // Construct format strings dynamically
        String realFormat = "%." + precision + "f";
        String imagFormat = "%." + precision + "fi";

        boolean isPureReal = (this.imNum == 0);
        boolean isPureImaginary = (this.reNum == 0);
        boolean isUnitImaginary = (Math.abs(this.imNum) == 1);

        if (isPureReal) {
            return String.format(realFormat, this.reNum);
        } else if (isPureImaginary) {
            return isUnitImaginary ? (this.imNum > 0 ? "i" : "-i") :
                    String.format(imagFormat, this.imNum);
        } else {
            return String.format("%s %s %s",
                    String.format(realFormat, this.reNum),
                    (this.imNum > 0 ? "+" : "-"),
                    isUnitImaginary ? "i" : String.format(imagFormat, Math.abs(this.imNum))
            );
        }
    }

    public double getMod() {
        return Math.sqrt(reNum * reNum + imNum * imNum);
    }

    public double getAngle() {
        if (isNull()) {
            throw new ArithmeticException("Angle is undefined for the origin (0 + 0i)");
        }

        return Math.atan2(imNum, reNum); // Automatically handles all quadrants
    }

    public double getStandardAngle() {
        if (isNull()) {
            throw new ArithmeticException("Angle is undefined for the origin (0 + 0i)");
        }

        double angle = Math.atan2(imNum, reNum); // Principal argument (-π, π]
        return (angle < 0) ? angle + 2 * Math.PI : angle; // Convert negative angles to [0, 2π)
    }

    public double getAngleToDegrees() {
        return Math.toDegrees(this.getAngle());
    }

    public double getStandardAngleToDegrees() {
        return Math.toDegrees(this.getStandardAngle());
    }


    public Complex getConjugate(){
        return new Complex(this.reNum, -this.imNum);
    }

    public Complex getReciprocal(){
        if(isNull()){
            throw new ArithmeticException("Reciprocal is undefined for zero complex number.");
        }

        double modSqr = this.reNum*this.reNum + this.imNum*this.imNum;
        return new Complex(reNum / modSqr, -imNum / modSqr);
    }

    private static double getRandomDouble(double min, double max) {
        Random rand = new Random();
        return min + (max - min) * rand.nextDouble();
    }

    private static int getRandomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

    // Random complex number with both real and imaginary parts
    public static Complex getRandomComplex(double minLimit, double maxLimit, boolean isInteger) {
        if (isInteger) {
            return new Complex(getRandomInt((int) minLimit, (int) maxLimit),
                    getRandomInt((int) minLimit, (int) maxLimit));
        } else {
            return new Complex(getRandomDouble(minLimit, maxLimit),
                    getRandomDouble(minLimit, maxLimit));
        }
    }

    // Random purely real or purely imaginary complex number
    public static Complex getRandomComplex(double minLimit, double maxLimit, boolean isInteger, boolean isPureReal) {
        if (isInteger) {
            return isPureReal
                    ? new Complex(getRandomInt((int) minLimit, (int) maxLimit), 0)
                    : new Complex(0, getRandomInt((int) minLimit, (int) maxLimit));
        } else {
            return isPureReal
                    ? new Complex(getRandomDouble(minLimit, maxLimit), 0)
                    : new Complex(0, getRandomDouble(minLimit, maxLimit));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Complex complex = (Complex) obj;
        return Double.compare(complex.reNum, reNum) == 0 &&
                Double.compare(complex.imNum, imNum) == 0;
    }


    // Constants
    static final double EPSILON = 1e-10;

    static final Complex IOTA = new Complex(0, 1);
    static final Complex NEG_IOTA = new Complex(0, -1);
    static final Complex ZERO = new Complex(0.0, 0.0);
    static final Complex ONE = new Complex(1.0, 0.0);
    static final Complex NEG_ONE = new Complex(-1.0, 0.0);
    static final Complex OMEGA = new Complex(-0.5, Math.sqrt(3) / 2.0);
    static final Complex OMEGA_SQR = new Complex(-0.5, -Math.sqrt(3) / 2.0);

    static final Complex LOG_OF_NEG_UNITY = new Complex(0, Math.PI);
    static final Complex LOG_OF_IOTA = new Complex(0, Math.PI / 2.0);
    static final Complex LOG_OF_OMEGA = new Complex(0, 2 * Math.PI / 3.0);
}