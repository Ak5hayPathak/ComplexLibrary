public final class ComplexLog {
    private ComplexLog() {
        throw new UnsupportedOperationException("Cannot instantiate ComplexLog.");
    }

    // ln(-x) = ln(x) + iπ
    public static Complex ln(double num) {
        if (num == 0) {
            throw new ArithmeticException("Log of zero is undefined");
        }
        return new Complex(Math.log(Math.abs(num)), num > 0 ? 0 : Math.PI);
    }

    // ln(i*x) = ln(x) + iπ/2  &  ln(-i*x) = ln(x) - iπ/2
    public static Complex ln(double num, boolean isNumImaginary) {
        if (num == 0) {
            throw new ArithmeticException("Log of zero is undefined");
        }
        return isNumImaginary ? new Complex(Math.log(Math.abs(num)), Math.copySign(Math.PI / 2, num)) : ln(num);
    }

    // ln(a+ib) = ln(|z|) + i*arg(z)
    public static Complex ln(Complex num) {
        if (num.isZero()) {
            throw new ArithmeticException("Log of zero is undefined");
        }
        return new Complex(Math.log(num.getMod()), num.getAngle());
    }

    public static Complex log10(Complex num){
        return ComplexMath.divide(ln(num), Math.log(10));
    }

    public static Complex log10(double num, boolean isImaginary) {
        return ComplexMath.divide(ln(num, isImaginary), Math.log(10));
    }

    // log_(a+ib)_(c+id) = ln(c+id) / ln(a+ib)
    public static Complex log(Complex base, Complex value) {
        if (base.isZero() || value.isZero()) {
            throw new ArithmeticException("Log with zero base or value is undefined.");
        }
        if (ComplexMath.areEqual(base, Complex.ONE)) {
            throw new ArithmeticException("Logarithm base 1 is undefined.");
        }
        if (ComplexMath.areEqual(base, Complex.NEG_ONE)) {
            return ComplexMath.divide(ln(value), new Complex(0.0, Math.PI));
        }
        return ComplexMath.divide(ln(value), ln(base));
    }


    // log_(a)_(c+id) = ln(c+id) / ln(a)
    public static Complex log(double base, Complex value) {
        if (value.isZero()) {
            throw new ArithmeticException("Log of zero is undefined");
        }
        return ComplexMath.divide(ln(value), ln(base));
    }

    // log_(a+ib)_(c) = ln(c) / ln(a+ib)
    public static Complex log(Complex base, double value) {
        if (base.isZero()) {
            throw new ArithmeticException("Log with zero base is undefined");
        }
        return ComplexMath.divide(ln(value), ln(base));
    }

    // log_(a+ib)_(ic) = ln(ic) / ln(a+ib)
    public static Complex log(Complex base, double value, boolean isValueImaginary) {
        return log(base, isValueImaginary ? ln(value, true) : new Complex(value, 0.0));
    }

    // log_(ia)_(c+id) = ln(c+id) / ln(ia)
    public static Complex log(double base, Complex value, boolean isBaseImaginary) {
        return log(isBaseImaginary ? ln(base, true) : new Complex(base, 0.0), value);
    }

    // log_(a)_(id) = ln(id) / ln(a)
    public static Complex log(boolean isBaseImaginary, double base, double value) {
        return log(isBaseImaginary ? ln(base, true) : new Complex(base, 0.0), ln(value));
    }

    // log_(ia)_(d) = ln(d) / ln(ia)
    public static Complex log(double base, boolean isValueImaginary, double value) {
        return log(ln(base), isValueImaginary ? ln(value, true) : new Complex(value, 0.0));
    }

    // log_(ia)_(id) = ln(id) / ln(ia)
    public static Complex log(double base, double value, boolean areBothImaginary) {
        return log(areBothImaginary ? ln(base, true) : new Complex(base, 0.0), areBothImaginary ? ln(value, true) : new Complex(value, 0.0));
    }

    public static double logModulus(Complex num) {
        return Math.log(num.getMod());
    }

    //Lambert W is to be added
}