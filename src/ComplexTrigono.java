public class ComplexTrigono {

    private ComplexTrigono() {
        throw new UnsupportedOperationException("Cannot instantiate ComplexTrigono.");
    }

    // sin(a+ib)
    public static Complex sin(Complex num) {
        Complex expIZ = ComplexPower.expComplex(ComplexMath.multiply(Complex.IOTA, num));
        Complex expNegIZ = ComplexPower.expComplex(ComplexMath.multiply(Complex.NEG_IOTA, num));
        return ComplexMath.divide(ComplexMath.subtract(expIZ, expNegIZ), new Complex(0, 2));
    }

    // sin(ib)
    public static Complex sin(double num, boolean isImaginary) {
        return isImaginary ? sin(new Complex(0, num)) : new Complex(Math.sin(num), 0.0);
    }

    // cos(a+ib)
    public static Complex cos(Complex num) {
        Complex expIZ = ComplexPower.expComplex(ComplexMath.multiply(Complex.IOTA, num));
        Complex expNegIZ = ComplexPower.expComplex(ComplexMath.multiply(Complex.NEG_IOTA, num));
        return ComplexMath.divide(ComplexMath.add(expIZ, expNegIZ), 2.0);
    }

    // cos(ib)
    public static Complex cos(double num, boolean isImaginary) {
        return isImaginary ? cos(new Complex(0, num)) : new Complex(Math.cos(num), 0.0);
    }

    // tan(a+ib)
    public static Complex tan(Complex num) {
        return ComplexMath.divide(sin(num), cos(num));
    }

    // tan(ib)
    public static Complex tan(double num, boolean isImaginary) {
        return isImaginary ? tan(new Complex(0, num)) : new Complex(Math.tan(num), 0.0);
    }

    // sec(a+ib)
    public static Complex sec(Complex num) {
        Complex cosValue = cos(num);
        if (cosValue.isNull()) {
            throw new ArithmeticException("Sec is undefined for cos(num) = 0");
        }
        return ComplexMath.divide(Complex.ONE, cosValue);
    }

    // sec(ib)
    public static Complex sec(double num, boolean isImaginary) {
        return isImaginary ? sec(new Complex(0, num)) : new Complex(1.0 / Math.cos(num), 0.0);
    }

    // arcSin(a+ib)
    public static Complex arcSin(Complex value) {
        Complex sqrtTerm = ComplexPower.sqrt(ComplexMath.subtract(Complex.ONE, ComplexMath.multiply(value, value)));
        Complex val = ComplexMath.add(ComplexMath.multiply(Complex.IOTA, value), sqrtTerm);
        return ComplexMath.multiply(Complex.NEG_IOTA, ComplexLog.ln(val));
    }

    public static Complex arcSin(double value, boolean isValueImaginary) {
        return arcSin(isValueImaginary ? new Complex(0.0, value) : new Complex(value, 0.0));
    }

    // arcCos(a+ib)
    public static Complex arcCos(Complex value) {
        Complex sqrtTerm = ComplexPower.sqrt(ComplexMath.subtract(Complex.ONE, ComplexMath.multiply(value, value)));
        Complex val = ComplexMath.add(value, ComplexMath.multiply(Complex.IOTA, sqrtTerm));
        return ComplexMath.multiply(Complex.NEG_IOTA, ComplexLog.ln(val));
    }

    public static Complex arcCos(double value, boolean isValueImaginary) {
        return arcCos(isValueImaginary ? new Complex(0.0, value) : new Complex(value, 0.0));
    }

    // arcTan(a+ib)
    public static Complex arcTan(Complex value) {
        if (ComplexMath.areEqual(value, new Complex(0, 1))) {
            throw new ArithmeticException("arcTan is undefined for sqrt(-1)");
        }
        Complex val = ComplexMath.divide(ComplexMath.subtract(Complex.ONE, ComplexMath.multiply(Complex.IOTA, value)), ComplexMath.add(Complex.ONE, ComplexMath.multiply(Complex.IOTA, value)));
        return ComplexMath.multiply(ComplexMath.divide(Complex.IOTA, 2.0), ComplexLog.ln(val));
    }

    public static Complex arcTan(double value, boolean isValueImaginary) {
        return arcTan(isValueImaginary ? new Complex(0.0, value) : new Complex(value, 0.0));
    }

    // arcSec(a+ib)
    public static Complex arcSec(Complex value) {
        if (value.getMod() < 1) {
            throw new ArithmeticException("arcSec is undefined for |z| < 1");
        }
        Complex invZ = ComplexMath.divide(Complex.ONE, value);
        Complex sqrtTerm = ComplexPower.sqrt(ComplexMath.subtract(Complex.ONE, ComplexMath.multiply(invZ, invZ)));
        Complex val = ComplexMath.add(invZ, ComplexMath.multiply(Complex.IOTA, sqrtTerm));
        return ComplexMath.multiply(Complex.NEG_IOTA, ComplexLog.ln(val));
    }

    public static Complex arcSec(double value, boolean isValueImaginary) {
        return arcSec(isValueImaginary ? new Complex(0.0, value) : new Complex(value, 0.0));
    }
}
