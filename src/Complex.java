import java.util.Objects;

public class Complex{
    protected double reNum, imNum;

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

    public Complex(String value) {
        value = value.replaceAll("\\s", ""); // Remove spaces

        // Handle pure real numbers (e.g., "3", "-2.5")
        if (value.matches("[-+]?[0-9]*\\.?[0-9]+")) {
            this.reNum = Double.parseDouble(value);
            this.imNum = 0.0;
            return;
        }

        // Handle pure imaginary numbers (e.g., "4i", "-2.3i", "i", "-i")
        if (value.matches("[-+]?[0-9]*\\.?[0-9]*i")) {
            if (value.equals("i")) {
                this.reNum = 0.0;
                this.imNum = 1.0;
            } else if (value.equals("-i")) {
                this.reNum = 0.0;
                this.imNum = -1.0;
            } else {
                this.reNum = 0.0;
                this.imNum = Double.parseDouble(value.replace("i", ""));
            }
            return;
        }

        // Handle full complex numbers (e.g., "3+4i", "-2-5i", "3.1+i", "4-i")
        String regex = "([-+]?[0-9]*\\.?[0-9]+)?([-+][0-9]*\\.?[0-9]*)?i";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(value);

        if (matcher.matches()) {
            // Extract real part (if exists, otherwise default to 0)
            String realPart = matcher.group(1);
            this.reNum = (realPart != null && !realPart.isEmpty()) ? Double.parseDouble(realPart) : 0.0;

            // Extract imaginary part (handle "+i" or "-i" as special cases)
            String imagPart = matcher.group(2);
            if (imagPart == null || imagPart.isEmpty()) {
                this.imNum = 0.0; // No imaginary part means it's a real number
            } else if (imagPart.equals("+") || imagPart.equals("-")) {
                this.imNum = imagPart.equals("+") ? 1.0 : -1.0;
            } else {
                this.imNum = Double.parseDouble(imagPart);
            }
        } else {
            throw new IllegalArgumentException("Invalid complex number format: " + value);
        }
    }

    public boolean isZero() {
        return Math.abs(this.reNum) < EPSILON && Math.abs(this.imNum) < EPSILON;
    }
    public boolean isPureReal(){return (!this.isZero()) && (Math.abs(this.imNum) < EPSILON);}

    public boolean isPureImaginary(){return (!this.isZero()) && (Math.abs(this.reNum) < EPSILON);}

    public boolean isGreaterThan(Complex other) {
        return this.getMod() > other.getMod();
    }

    public boolean isLessThan(Complex other) {
        return this.getMod() < other.getMod();
    }

    public void setComplex(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) {
            throw new IllegalArgumentException("Real and imaginary parts must be valid numbers.");
        }
        this.reNum = a;
        this.imNum = b;
    }

    public static Complex inputComplex(String prompt) {
        while (true) {
            String input = IO.getString(prompt).replaceAll("\\s", ""); // Remove spaces

            // Handle comma-separated real and imaginary numbers (e.g., "3,4")
            if (input.contains(",")) {
                String[] parts = input.split(",");
                try {
                    if (parts.length == 2) {
                        double real = Double.parseDouble(parts[0].trim());
                        double imaginary = Double.parseDouble(parts[1].trim());
                        return new Complex(real, imaginary);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid format! Please enter the complex number in one of the following ways:");
                    System.out.println(" - As two values separated by a comma: (a, b) -> a + bi");
                    System.out.println(" - In standard complex format: a + bi or a - bi");
                    continue;
                }
                System.out.println("Invalid format! Please enter the complex number in one of the following ways:");
                System.out.println(" - As two values separated by a comma: (a, b) -> a + bi");
                System.out.println(" - In standard complex format: a + bi or a - bi");
                continue;
            }

            // Handle direct complex number formats (e.g., "3+4i", "i", "-i", "4.5", "-3.2i")
            try {
                return new Complex(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid complex number format! Try again.");
            }
        }
    }

    public void inputComplex(){
        Complex num = inputComplex("");
        this.reNum = num.reNum;
        this.imNum = num.imNum;
    }

    public void printComplex(int precision) {
        if (this.isZero()) {
            System.out.print("0");
            return;
        }

        // Format string for real and imaginary parts
        String format = "%." + precision + "f";

        if (this.isPureReal()) {
            // Pure real number
            System.out.printf(format, reNum);
        } else if (this.isPureImaginary()) {
            // Pure imaginary number
            if (Math.abs(this.imNum) == 1) {
                System.out.print(this.imNum > EPSILON ? "i" : "-i");
            } else {
                System.out.printf("%si", String.format(format, this.imNum));
            }
        } else {
            // General complex number
            System.out.print(String.format(format, reNum) + (imNum > 0 ? " + " : " - ") +
                    (Math.abs(imNum) == 1 ? "i" : String.format(format, Math.abs(imNum)) + "i"));
        }
    }

    public double getReal(){
        return this.reNum;
    }

    public double getImag(){
        return this.imNum;
    }

    public static Complex typecast(double val){
        return new Complex(val, 0);
    }

    public static double typecast(Complex num){
        return num.reNum;
    }

    public void printComplex() {
        this.printComplex(3);
    }

    public void printPolar(int precision) {
        if (this.isZero()) {
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
        if (this.isZero()) return "0";

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
        if (this.isZero()) {
            throw new ArithmeticException("Angle is undefined for the origin (0 + 0i)");
        }

        return Math.atan2(this.imNum, this.reNum); // Automatically handles all quadrants
    }

    public double getStandardAngle() {
        if (this.isZero()) {
            throw new ArithmeticException("Angle is undefined for the origin (0 + 0i)");
        }

        double angle = Math.atan2(this.imNum, this.reNum); // Principal argument (-π, π]
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
        if(this.isZero()){
            throw new ArithmeticException("Reciprocal is undefined for zero complex number.");
        }

        double modSqr = this.reNum*this.reNum + this.imNum*this.imNum;
        return new Complex(this.reNum / modSqr, -this.imNum / modSqr);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Complex complex = (Complex) obj;
        return Math.abs(complex.reNum - reNum) < EPSILON &&
                Math.abs(complex.imNum - imNum) < EPSILON;

    }

    @Override
    public int hashCode() {
        return Objects.hash(reNum, imNum);
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