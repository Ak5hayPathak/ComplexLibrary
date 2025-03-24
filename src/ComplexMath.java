public final class ComplexMath {

    // Private constructor prevents object creation
    private ComplexMath() {
        throw new UnsupportedOperationException("Cannot instantiate ComplexMath.");
    }

    //Checking if a complex number is 0+i0
    public static boolean isCompNull(Complex comp) {
        return Math.abs(comp.reNum) < Complex.EPSILON && Math.abs(comp.imNum) < Complex.EPSILON;
    }

    public static boolean areEqual(Complex num1, Complex num2) {
         // Tolerance for floating-point precision errors
        return Math.abs(num1.reNum - num2.reNum) < Complex.EPSILON &&
                Math.abs(num1.imNum - num2.imNum) < Complex.EPSILON;
    }

    // (a+ib) + (c+id) → Standard Complex Addition
    public static Complex add(Complex comp1, Complex comp2){
        return new Complex(comp1.reNum + comp2.reNum, comp1.imNum + comp2.imNum);
    }

    // (a+ib) + c → Adding a real number to a complex number
    public static Complex add(Complex comp, double real){
        return new Complex(real + comp.reNum, comp.imNum);
    }

    // c + (a+ib) → Adding a complex number to a real number
    public static Complex add( double real, Complex comp){
        return new Complex(real + comp.reNum, comp.imNum);
    }

    // (a+ib) + ic → Adding an imaginary number to a complex number
    public static Complex add(Complex comp,double imaginary, boolean isImaginary){
        return (isImaginary)? new Complex(comp.reNum, comp.imNum+imaginary) : add(comp, imaginary);
    }

    // ic + (a+ib) → Adding a complex number to an imaginary number
    public static Complex add(double imaginary, boolean isImaginary, Complex comp){
        return (isImaginary)? new Complex(comp.reNum, comp.imNum+imaginary) : add(comp, imaginary);
    }

    // (a+ib) - (c+id) → Standard Complex Subtraction
    public static Complex subtract(Complex comp1, Complex comp2) {
        return new Complex(comp1.reNum - comp2.reNum, comp1.imNum - comp2.imNum);
    }

    // (c+id) - a → Subtracting a real number from a complex number
    public static Complex subtract(Complex comp, double real) {
        return new Complex(comp.reNum - real, comp.imNum);
    }

    // a - (c+id) → Subtracting a complex number from a real number
    public static Complex subtract(double real, Complex comp) {
        return new Complex(real - comp.reNum, comp.imNum);
    }

    // (c+id) - ib → Subtracting an imaginary number from a complex number
    public static Complex subtract(Complex comp, double imaginary, boolean isImaginary) {
        return isImaginary ? new Complex(comp.reNum, comp.imNum - imaginary) : subtract(comp, imaginary);
    }

    // ib - (c+id) → Subtracting a complex number from an imaginary number
    public static Complex subtract(double imaginary, boolean isImaginary, Complex comp) {
        return isImaginary ? new Complex(comp.reNum, imaginary - comp.imNum) : subtract(imaginary ,comp);
    }

    // (a+ib) * (c+id) → Standard Complex Multiplication
    public static Complex multiply(Complex comp1, Complex comp2) {
        double a = comp1.reNum, b = comp1.imNum;
        double c = comp2.reNum, d = comp2.imNum;

        double real = Math.fma(a, c, -b * d);  // a*c - b*d using FMA
        double imag = Math.fma(a, d, b * c);   // a*d + b*c using FMA

        return new Complex(real, imag);
    }

    // Real number * Complex number → (a) * (c+id) = (ac) + (a*id)
    public static Complex multiply(double real, Complex comp) {
        return new Complex(real * comp.reNum, real * comp.imNum);
    }

    // Complex number * Real number → (c+id) * (a) = (ac) + (a*id)
    public static Complex multiply(Complex comp, double real) {
        return new Complex(real * comp.reNum, real * comp.imNum);
    }

    // Imaginary number * Complex number → (ib) * (c+id) = b*(-d + ic)
    public static Complex multiply(double imaginary,  boolean isImaginary, Complex comp) {
        return isImaginary
                ? new Complex(-imaginary * comp.imNum, imaginary * comp.reNum)
                : multiply(imaginary, comp);
    }

    // Complex number * Imaginary number → (c+id) * (ib) = b*(-d + ic)
    public static Complex multiply(Complex comp, double imaginary, boolean isImaginary) {
        return isImaginary
                ? new Complex(-imaginary * comp.imNum, imaginary * comp.reNum)
                : multiply(imaginary, comp);
    }

    // (a+ib)/(c+id) → Standard Complex Division
    public static Complex divide(Complex numerator, Complex denominator) {
        if (denominator.isZero()) {
            throw new ArithmeticException("Division by zero is undefined");
        }
        Complex conjugate = denominator.getConjugate();
        Complex numeratorModified = multiply(numerator, conjugate);
        double denom = (denominator.reNum * denominator.reNum) + (denominator.imNum * denominator.imNum);
        return new Complex(numeratorModified.reNum / denom, numeratorModified.imNum / denom);
    }

    // (a+ib) / c → Complex divided by real number
    public static Complex divide(Complex numerator, double denominator) {
        if (Math.abs(denominator) < Complex.EPSILON) {
            throw new ArithmeticException("Division by zero in complex number division");
        }
        return new Complex(numerator.reNum / denominator, numerator.imNum / denominator);
    }

    // (a+ib) / (ic) → Complex divided by an imaginary number
    public static Complex divide(Complex numerator, double denominator, boolean isDenominatorImaginary) {
        if (isDenominatorImaginary) {
            return multiply(Complex.NEG_IOTA, divide(numerator, denominator));
        }
        return divide(numerator, denominator);
    }

    // a / (c+id) → Real number divided by a complex number
    public static Complex divide(double numerator, Complex denominator) {
        return multiply(numerator, denominator.getReciprocal());
    }

    // ia / (c+id) → Imaginary number divided by a complex number
    public static Complex divide(double numerator,  boolean isNumeratorImaginary, Complex denominator) {
        return isNumeratorImaginary
                ? multiply(Complex.IOTA, divide(numerator, denominator))
                : divide(numerator, denominator);
    }

    // Polar form: magnitude * e^(i * angle) = a + ib
    public static Complex polarForm(double magnitude, double angle) {
        return new Complex(magnitude, angle, true);
    }
}