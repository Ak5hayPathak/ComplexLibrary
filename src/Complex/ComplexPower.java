package Complex;

public final class ComplexPower {

    private ComplexPower() {
        throw new UnsupportedOperationException("Cannot instantiate ComplexPower.");
    }

    // e^(ix) = cos(x) + i*sin(x)  → Euler's formula
    public static Complex eulersFormula(double pow) {
        return new Complex(Math.cos(pow), Math.sin(pow));
    }

    //e^i(ix) = e^(-x) → Exponential decay when given a purely imaginary exponent
    public static Complex eulersFormula(double pow, boolean isPowerImaginary) {
        return isPowerImaginary ? new Complex(Math.exp(-pow), 0.0) : eulersFormula(pow);
    }

    // e^(x + iy) = e^x * (cos(y) + i*sin(y))
    public static Complex expComplex(Complex pow) {
        double expReal = Math.exp(pow.reNum);  // Compute e^x
        return new Complex(expReal * Math.cos(pow.imNum), expReal * Math.sin(pow.imNum));
    }

    // Directly calls expComplex() for efficiency
    public static Complex eulersFormula(Complex pow) {
        return expComplex(new Complex(-pow.imNum, pow.reNum));
    }

    // (a+ib)^(c+id)
    public static Complex power(Complex base, Complex pow) {
        if (base.isZero() && pow.isZero()) {
            throw new ArithmeticException("0 raised to the power 0 is undefined.");
        } else if (base.isZero()) {
            return new Complex();
        }
        return expComplex(ComplexMath.multiply(pow, ComplexLog.ln(base)));
    }

    // a^(c+id)
    public static Complex power(double base, Complex pow) {
        return power(new Complex(base, 0.0), pow);
    }

    // (ia)^(c+id)
    public static Complex powerImaginaryBase(double base, Complex pow) {
        return expComplex(ComplexMath.multiply(pow, ComplexLog.ln(Math.abs(base), true)));
    }

    // (a+ib)^c
    public static Complex power(Complex base, double pow) {
        return power(base, new Complex(pow, 0));
    }

    // (a+ib)^(ic)
    public static Complex powerImaginaryExponent(Complex base, double pow) {
        return expComplex(ComplexMath.multiply(new Complex(0, pow), ComplexLog.ln(base)));
    }

    // sqrt(a+ib) using complex power
    public static Complex sqrt(Complex num) {
        return power(num, 0.5);
    }

    // sqrt(ib)
    public static Complex sqrtImaginary(double val) {
        return sqrt(new Complex(0, val));
    }

    public static Complex[] sqrtAll(Complex num) {
        double r = Math.sqrt(num.getMod()); // Correct square root computation
        double theta = num.getAngle(); // Compute argument (angle)

        Complex[] roots = new Complex[2];
        for (int k = 0; k < 2; k++) {
            double angle = (theta + k * Math.PI) / 2.0; // Roots are separated by π/2
            roots[k] = new Complex(r * Math.cos(angle), r * Math.sin(angle));
        }
        return roots;
    }

    // cbrt(a+ib) using complex power
    public static Complex cbrt(Complex num) {
        return power(num, 1.0 / 3.0);
    }

    // cbrt(ib)
    public static Complex cbrtImaginary(double val) {
        return cbrt(new Complex(0, val));
    }

    public static Complex[] cbrtAll(Complex num) {
        double r = Math.cbrt(num.getMod()); // Use Math.cbrt() for better precision
        double theta = Math.atan2(num.imNum, num.reNum); // Compute argument (angle)

        Complex[] roots = new Complex[3];
        for (int k = 0; k < 3; k++) {
            double angle = (theta + 2 * Math.PI * k) / 3.0;
            roots[k] = new Complex(r * Math.cos(angle), r * Math.sin(angle));
        }
        return roots;
    }

    // (a+ib)^(1/(c+id)) → Complex root of a complex number
    public static Complex nrt(Complex num, Complex root) {
        if (root.isZero()) {
            throw new ArithmeticException("Undefined root (division by zero)");
        }
        return power(num, root.getReciprocal());
    }

    // (a+ib)^(1/n) → nth root of a complex number
    public static Complex nrt(Complex num, double root) {
        return nrt(num, new Complex(root, 0));
    }

    // (a+ib)^(1/in) → imaginary nth root of a complex number
    public static Complex nrt(Complex num, double root, boolean isRootImaginary) {
        return isRootImaginary? nrt(num, new Complex(0, root)):nrt(num, new Complex(root, 0));
    }

    // (ib)^(1/n) → nth root of an imaginary number
    public static Complex nrt(double num, boolean isBaseImaginary, double root) {
        return isBaseImaginary? nrt(new Complex(0, num), new Complex(root, 0))
                : nrt(new Complex(num, 0), new Complex(root, 0));
    }

    // (ib)^(1/(id)) → Root of an imaginary number with an imaginary root
    public static Complex nrt(boolean areBothImaginary, double num, double root) {
        return areBothImaginary? nrt(new Complex(0, num), new Complex(0, root))
                :nrt(new Complex(num, 0), new Complex(root, 0));
    }

    // (a)^1/(c+id) → Complex root of a real number
    public static Complex nrt(double num, Complex root){
        return nrt(new Complex(num, 0), root);
    }

    // (a)^1/(id) → imaginary root of a real number
    public static Complex nrt(double num, double root, boolean isRootImaginary){
        return isRootImaginary? nrt(new Complex(num, 0), new Complex(0, root))
                :nrt(new Complex(num, 0), new Complex(root, 0));
    }

    public static Complex[] nrtAll(Complex num, int root) {
        double r = Math.pow(num.getMod(), 1.0 / root); // Compute nth root of modulus
        double theta = num.getAngle(); // Compute argument (angle)

        Complex[] roots = new Complex[root];
        for (int k = 0; k < root; k++) {
            double angle = (theta + 2 * Math.PI * k) / root; // Correct nth root formula
            roots[k] = new Complex(r * Math.cos(angle), r * Math.sin(angle));
        }
        return roots;
    }

}