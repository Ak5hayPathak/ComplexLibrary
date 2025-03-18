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

    // (a+ib) + ic → Adding an imaginary number to a complex number
    public static Complex add(Complex comp,double imaginary, boolean isImaginary){
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

    // (c+id) - ib → Subtracting an imaginary number from a complex number
    public static Complex subtract(Complex comp, double imaginary, boolean isImaginary) {
        return isImaginary ? new Complex(comp.reNum, comp.imNum - imaginary) : subtract(comp, imaginary);
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

    // Imaginary number * Complex number → (ib) * (c+id) = b*(-d + ic)
    public static Complex multiply(double imaginary, Complex comp, boolean isImaginary) {
        return isImaginary
                ? new Complex(-imaginary * comp.imNum, imaginary * comp.reNum)
                : multiply(imaginary, comp);
    }

    // (a+ib) / (c+id) → Standard Complex Division
    public static Complex divide(Complex numerator, Complex denominator) {
        if (isCompNull(denominator)) {
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
    public static Complex divideNumerator(double numerator, Complex denominator) {
        return multiply(numerator, denominator.getReciprocal());
    }

    // ia / (c+id) → Imaginary number divided by a complex number
    public static Complex divideNumerator(double numerator, Complex denominator, boolean isNumeratorImaginary) {
        return isNumeratorImaginary
                ? multiply(Complex.IOTA, divideNumerator(numerator, denominator))
                : divideNumerator(numerator, denominator);
    }

    // e^(ix) = cos(x) + i*sin(x)  → Euler's formula
    public static Complex eulersFormula(double pow) {
        return new Complex(Math.cos(pow), Math.sin(pow));
    }

    // e^i(ix) = e^(-x) → Exponential decay when given a purely imaginary exponent
    public static Complex eulersFormula(double pow, boolean isPowerImaginary) {
        return isPowerImaginary ? new Complex(Math.exp(-pow), 0.0) : eulersFormula(pow);
    }

    // e^(x + iy) = e^x * (cos(y) + i*sin(y))
    public static Complex expComplex(Complex pow) {
        double expReal = Math.exp(pow.reNum);  // Compute e^x
        return new Complex(expReal * Math.cos(pow.imNum), expReal * Math.sin(pow.imNum));
    }

    // e^i(x + iy) = e^(-y + ix)  (Shifts imaginary component)
    public static Complex eulersFormula(Complex pow) {
        return expComplex(new Complex(-pow.imNum, pow.reNum));
    }

    // ln(-x) = ln(x) + iπ
    public static Complex ln(double num) {
        if (num == 0) {
            throw new ArithmeticException("Log of 0 is undefined");
        }
        return new Complex(Math.log(Math.abs(num)), num > 0 ? 0 : Math.PI);
    }

    // ln(i*x) = ln(x) + iπ/2  &  ln(-i*x) = ln(x) - iπ/2
    public static Complex ln(double num, boolean isNumImaginary) {
        if (num == 0) {
            throw new ArithmeticException("Log of 0 is undefined");
        }
        if (!isNumImaginary) {
            return ln(num);
        }
        return new Complex(Math.log(Math.abs(num)), num > 0 ? Math.PI / 2 : -Math.PI / 2);
    }

    // ln(a+ib) = ln(|z|) + i*arg(z)
    public static Complex ln(Complex num) {
        if (num.isNull()) {
            throw new ArithmeticException("Log of 0+i0 is undefined");
        }
        return new Complex(Math.log(num.getMod()), num.getAngle());
    }

    // log_(a+ib)_(c+id) = ln(c+id) / ln(a+ib)
    public static Complex log(Complex base, Complex value) {
        if (base.isNull() || value.isNull()) {
            throw new ArithmeticException("Log of 0+i0 is undefined");
        }
        if(areEqual(base, Complex.ONE)){
            return divide(ln(value), new Complex(0.0, 2.0*Math.PI));
        }
        if(areEqual(base, Complex.NEG_ONE)){
            return divide(ln(value), new Complex(0.0, Math.PI));
        }
        return divide(ln(value), ln(base));
    }

    // log_(a)_(c+id) = ln(c+id) / ln(a)
    public static Complex log(double base, Complex value){
        if (value.isNull()) {
            throw new ArithmeticException("Log of 0+i0 is undefined");
        }
        return divide(ln(value), ln(base));
    }

    // log_(a+ib)_(c) = ln(c) / ln(a+ib)
    public static Complex log(Complex base, double value){
        if (base.isNull()) {
            throw new ArithmeticException("Log of 0+i0 is undefined");
        }
        return divide(ln(value), ln(base));
    }

    // log_(a+ib)_(ic) = ln(ic) / ln(a+ib)
    public static Complex log(Complex base, double value, boolean isValueImaginary){
        if(isValueImaginary){
            if(base.isNull()){
                throw new ArithmeticException("Log of 0+i0 is undefined");
            }
            return divide(ln(value, true), ln(base));
        }
        return log(base, value);
    }

    // log_(ia)_(c+id) = ln(c+id) / ln(ia)
    public static Complex log(double base, Complex value, boolean isBaseImaginary){
        if(isBaseImaginary){
            if(value.isNull()){
                throw new ArithmeticException("Log of 0+i0 is undefined");
            }
            return divide(ln(value), ln(base, true));
        }
        return log(base, value);
    }

    // log_(a)_(id) = ln(id) / ln(a)
    public static Complex log(boolean isBaseImaginary, double base, double value){
        if(isBaseImaginary){
            return divide(ln(value), ln(base, true));
        }
        return log(base, new Complex(value, 0.0));
    }

    // log_(ia)_(d) = ln(d) / ln(ia)
    public static Complex log(double base, boolean isValueImaginary, double value){
        if(isValueImaginary){
            return divide(ln(value, true), ln(base));
        }
        return log(base, new Complex(value, 0.0));
    }

    // log_(ia)_(id) = ln(id) / ln(ia)
    public static Complex log(double base, double value, boolean areBothImaginary){
        if(areBothImaginary){
            return divide(ln(value, true), ln(base, true));
        }
        return log(base, new Complex(value, 0.0));
    }

    // (a+ib)^(c+id)
    public static Complex power(Complex base, Complex pow) {
        if (base.isNull() && pow.isNull()) {
            throw new ArithmeticException("0 raised to the power 0 is undefined.");
        }
        else if(base.isNull()){
            return new Complex();
        }
        return expComplex(multiply(pow, ln(base)));
    }

    // a^(c+id)
    public static Complex power(double base, Complex pow) {
        return power(new Complex(base, 0.0), pow);
    }

    // (ia)^(c+id)
    public static Complex powerImBase(double base, Complex pow) {
        return expComplex(multiply(pow, ln(Math.abs(base), true)));
    }

    // (a+ib)^c
    public static Complex power(Complex base, double pow) {
        return power(base, new Complex(pow, 0));
    }

    // (a+ib)^(ic)
    public static Complex powerImPow(Complex base, double pow) {
        return expComplex(multiply(new Complex(0, pow), ln(base)));
    }

    // sqrt(a+ib) using complex power
    public static Complex sqrt(Complex num) {
        return power(num, 0.5);
    }

    // cbrt(a+ib) using complex power
    public static Complex cbrt(Complex num) {
        return power(num, 1.0 / 3.0);
    }

    // sqrt(ib)
    public static Complex sqrtImaginary(double val) {
        return sqrt(new Complex(0, val));
    }

    // cbrt(ib)
    public static Complex cbrtImaginary(double val) {
        return cbrt(new Complex(0, val));
    }

    // (a+ib)^(1/(c+id)) → Complex root of a complex number
    public static Complex nrt(Complex num, Complex root) {
        if (root.isNull()) {
            throw new ArithmeticException("Undefined root (division by zero)");
        }
        return power(num, root.getReciprocal());
    }

    // (a+ib)^(1/n) → nth root of a complex number
    public static Complex nrt(Complex num, double root) {
        if (Math.abs(root) < Complex.EPSILON) {
            throw new ArithmeticException("Undefined root (division by zero)");
        }
        return power(num, 1.0 / root);
    }

    // (ib)^(1/n) → nth root of an imaginary number
    public static Complex nrt(double num, double root) {
        if (Math.abs(root) < Complex.EPSILON) {
            throw new ArithmeticException("Undefined root (division by zero)");
        }
        return power(new Complex(0, num), 1.0 / root);
    }

    // (ib)^(1/(id)) → Root of an imaginary number with an imaginary root
    public static Complex nrt(double num, double root, boolean isImaginary) {
        if (Math.abs(root) < Complex.EPSILON) {
            throw new ArithmeticException("Undefined root (division by zero)");
        }

        if (isImaginary) {
            Complex imaginaryReciprocal = new Complex(0, -1 / root);  // Reciprocal of (id) is (-i/d)
            return power(new Complex(0, num), imaginaryReciprocal);
        }
        return nrt(num, root);
    }

    // sin(a+ib)
    public static Complex sin(Complex num) {
        Complex expIZ = expComplex(multiply(Complex.IOTA, num));
        Complex expNegIZ = expComplex(multiply(Complex.NEG_IOTA, num));
        Complex numerator = subtract(expIZ, expNegIZ);
        Complex denominator = new Complex(0, 2);
        return divide(numerator, denominator);
    }

    // sin(ib)
    public static Complex sin(double num, boolean isImaginary) {
        if (isImaginary) {
            return sin(new Complex(0, num));
        }
        return new Complex(Math.sin(num), 0.0);
    }

    // cos(a+ib)
    public static Complex cos(Complex num) {
        Complex expIZ = expComplex(multiply(Complex.IOTA, num));
        Complex expNegIZ = expComplex(multiply(Complex.NEG_IOTA, num));
        Complex numerator = add(expIZ, expNegIZ);
        return divide(numerator, 2.0);
    }

    // cos(ib)
    public static Complex cos(double num, boolean isImaginary) {
        if (isImaginary) {
            return cos(new Complex(0, num));
        }
        return new Complex(Math.cos(num), 0.0);
    }

    // tan(a+ib)
    public static Complex tan(Complex num) {
        return divide(sin(num), cos(num));
    }

    // tan(ib)
    public static Complex tan(double num, boolean isImaginary) {
        if (isImaginary) {
            return tan(new Complex(0, num));
        }
        return new Complex(Math.tan(num), 0.0);
    }

    // cosec(a+ib)
    public static Complex cosec(Complex num) {
        Complex sinValue = sin(num);
        if (sinValue.isNull()) {
            throw new ArithmeticException("Cosec is undefined for sin(num) = 0");
        }
        return divide(new Complex(1.0, 0.0), sinValue);
    }

    // cosec(ib)
    public static Complex cosec(double num, boolean isImaginary) {
        if (isImaginary) {
            return cosec(new Complex(0, num));
        }
        if (Math.sin(num) == 0) {
            throw new ArithmeticException("Cosec is undefined for sin(num) = 0");
        }
        return new Complex(1.0 / Math.sin(num), 0.0);
    }

    // sec(a+ib)
    public static Complex sec(Complex num) {
        Complex cosValue = cos(num);
        if (cosValue.isNull()) {
            throw new ArithmeticException("Sec is undefined for cos(num) = 0");
        }
        return divide(new Complex(1.0, 0.0), cosValue);
    }

    // sec(ib)
    public static Complex sec(double num, boolean isImaginary) {
        if (isImaginary) {
            return sec(new Complex(0, num));
        }
        if (Math.cos(num) == 0) {
            throw new ArithmeticException("Sec is undefined for cos(num) = 0");
        }
        return new Complex(1.0 / Math.cos(num), 0.0);
    }

    // cot(a+ib)
    public static Complex cot(Complex num) {
        Complex sinValue = sin(num);
        if (sinValue.isNull()) {
            throw new ArithmeticException("Cot is undefined for sin(num) = 0");
        }
        return divide(cos(num), sinValue);
    }

    // cot(ib)
    public static Complex cot(double num, boolean isImaginary) {
        if (isImaginary) {
            return cot(new Complex(0, num));
        }
        if (Math.tan(num) == 0) {
            throw new ArithmeticException("Cot is undefined for tan(num) = 0");
        }
        return new Complex(1.0 / Math.tan(num), 0.0);
    }

    public static Complex arcSin(Complex value){
        Complex val = add(multiply(Complex.IOTA, value), sqrt(subtract(Complex.ONE, multiply(value,value))));
        return multiply(Complex.NEG_IOTA, ln(val));
    }

    public static Complex arcSin(double value, boolean isValueImaginary){
        if(isValueImaginary){
            return arcSin(new Complex(0.0, value));
        }
        return arcSin(new Complex(value, 0.0));
    }

    public static Complex arcCos(Complex value){
        Complex val = add(value, multiply(Complex.IOTA, sqrt(subtract(Complex.ONE, multiply(value, value)))));
        return multiply(Complex.NEG_IOTA, ln(val));
    }

    public static Complex arcCos(double value, boolean isValueImaginary){
        if(isValueImaginary){
            return arcCos(new Complex(0.0, value));
        }
        return arcCos(new Complex(value, 0.0));
    }

    public static Complex arcTan(Complex value){
        if(areEqual(value, new Complex(0, 1))){
            throw new ArithmeticException("arcTan is undefined for sqrt(-1)");
        }
        Complex val = divide(subtract(Complex.ONE, multiply(Complex.IOTA, value)), add(Complex.ONE, multiply(Complex.IOTA, value)));
        return multiply(divide(Complex.IOTA, 2.0), ln(val));
    }

    public static Complex arcTan(double value, boolean isValueImaginary){
        if(isValueImaginary){
            return arcTan(new Complex(0.0, value));
        }
        return arcTan(new Complex(value, 0.0));
    }

    public static Complex arcCot(Complex value){
        Complex val = divide(add(value, Complex.IOTA), subtract(value, Complex.IOTA));
        return multiply(divide(Complex.IOTA, 2.0), ln(val));
    }

    public static Complex arcCot(double value, boolean isValueImaginary){
        if(isValueImaginary){
            return arcCot(new Complex(0.0, value));
        }
        return arcCot(new Complex(value, 0.0));
    }

    public static Complex arcSec(Complex value) {
        Complex invZ = divide(Complex.ONE, value);
        Complex sqrtTerm = sqrt(subtract(Complex.ONE, multiply(invZ, invZ)));
        Complex val = add(invZ, multiply(Complex.IOTA, sqrtTerm));
        return multiply(Complex.NEG_IOTA, ln(val));
    }

    public static Complex arcSec(double value, boolean isValueImaginary){
        if(isValueImaginary){
            return arcSec(new Complex(0.0, value));
        }
        return arcSec(new Complex(value, 0.0));
    }

    public static Complex arcCosec(Complex value) {
        Complex invZ = divide(Complex.IOTA, value);
        Complex sqrtTerm = sqrt(subtract(Complex.ONE, multiply(invZ, invZ)));
        Complex val = add(invZ, sqrtTerm);
        return multiply(Complex.NEG_IOTA, ln(val));
    }

    public static Complex arcCosec(double value, boolean isValueImaginary){
        if(isValueImaginary){
            return arcCosec(new Complex(0.0, value));
        }
        return arcCosec(new Complex(value, 0.0));
    }

    // Polar form: magnitude * e^(i * angle) = a + ib
    public static Complex polarForm(double magnitude, double angle) {
        return new Complex(magnitude, angle, true);
    }

}