package Complex;

public final class ComplexTrigono {

    private ComplexTrigono() {
        throw new UnsupportedOperationException("Cannot instantiate ComplexTrigono.");
    }

    public static Complex toDegrees(Complex radians){
        return ComplexMath.divide(ComplexMath.multiply(radians, new Complex(180.0, 0)),new Complex(Math.PI, 0));
    }

    public static double toDegrees(double radians){
        return (radians*180.0)/Math.PI;
    }

    public static Complex toRadian(Complex degrees){
        return ComplexMath.divide(ComplexMath.multiply(degrees, new Complex(Math.PI, 0)),new Complex(180, 0));
    }

    public static double toRadian(double degrees){
        return (degrees*Math.PI)/180.0;
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

    //sin(a in degrees)
    public static Complex sinDegrees(Complex num){
        return sin(toRadian(num));
    }

    //sin(ib in degrees)
    public static Complex sinDegrees(double num, boolean isImaginary){
        return sin(toRadian(num), isImaginary);
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

    //cos(a in degrees)
    public static Complex cosDegrees(Complex num){
        return cos(toRadian(num));
    }

    //cos(ib in degrees)
    public static Complex cosDegrees(double num, boolean isImaginary){
        return cos(toRadian(num), isImaginary);
    }

    // tan(a+ib)
    public static Complex tan(Complex num) {
        return ComplexMath.divide(sin(num), cos(num));
    }

    // tan(ib)
    public static Complex tan(double num, boolean isImaginary) {
        return isImaginary ? tan(new Complex(0, num)) : new Complex(Math.tan(num), 0.0);
    }

    //tan(a in degrees)
    public static Complex tanDegrees(Complex num){
        return tan(toRadian(num));
    }

    //tan(ib in degrees)
    public static Complex tanDegrees(double num, boolean isImaginary){
        return tan(toRadian(num), isImaginary);
    }

    // cot(a + ib)
    public static Complex cot(Complex num) {
        Complex tanValue = tan(num);
        if (tanValue.isZero()) {
            throw new ArithmeticException("Cotangent is undefined for tan(z) = 0");
        }
        return ComplexMath.divide(Complex.ONE, tanValue);
    }

    // cot(ib)
    public static Complex cot(double num, boolean isImaginary) {
        Complex tanValue = tan(num, isImaginary);
        if (tanValue.isZero()) {
            throw new ArithmeticException("Cotangent is undefined for tan(z) = 0");
        }
        return ComplexMath.divide(Complex.ONE, tanValue);
    }

    // cot(a in degrees)
    public static Complex cotDegrees(Complex num) {
        return cot(toRadian(num));
    }

    // cot(ib in degrees)
    public static Complex cotDegrees(double num, boolean isImaginary) {
        return cot(toRadian(num), isImaginary);
    }

    // sec(a+ib)
    public static Complex sec(Complex num) {
        Complex cosValue = cos(num);
        if (cosValue.isZero()) {
            throw new ArithmeticException("Sec is undefined for cos(num) = 0");
        }
        return ComplexMath.divide(Complex.ONE, cosValue);
    }

    // sec(ib)
    public static Complex sec(double num, boolean isImaginary) {
        return isImaginary ? sec(new Complex(0, num)) : new Complex(1.0 / Math.cos(num), 0.0);
    }

    //sec(a in degrees)
    public static Complex secDegrees(Complex num){
        return sec(toRadian(num));
    }

    //sec(ib in degrees)
    public static Complex secDegrees(double num, boolean isImaginary){
        return sec(toRadian(num), isImaginary);
    }

    // cosec(a + ib)
    public static Complex cosec(Complex num) {
        Complex sinValue = sin(num);
        if (sinValue.isZero()) {
            throw new ArithmeticException("Cosecant is undefined for sin(z) = 0");
        }
        return ComplexMath.divide(Complex.ONE, sinValue);
    }

    // cosec(ib)
    public static Complex cosec(double num, boolean isImaginary) {
        Complex sinValue = sin(num, isImaginary);
        if (sinValue.isZero()) {
            throw new ArithmeticException("Cosecant is undefined for sin(z) = 0");
        }
        return ComplexMath.divide(Complex.ONE, sinValue);
    }

    // cosec(a in degrees)
    public static Complex cosecDegrees(Complex num) {
        return cosec(toRadian(num));
    }

    // cosec(ib in degrees)
    public static Complex cosecDegrees(double num, boolean isImaginary) {
        return cosec(toRadian(num), isImaginary);
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

    public static Complex arcSinDegrees(Complex value){
        return toDegrees(arcSin(value));
    }

    public static Complex arcSinDegrees(double value, boolean isValueImaginary){
        return toDegrees(arcSin(value, isValueImaginary));
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

    public static Complex arcCosDegrees(Complex value){
        return toDegrees(arcCos(value));
    }

    public static Complex arcCosDegrees(double value, boolean isValueImaginary){
        return toDegrees(arcCos(value, isValueImaginary));
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

    public static Complex arcTanDegrees(Complex value){
        return toDegrees(arcTan(value));
    }

    public static Complex arcTanDegrees(double value, boolean isValueImaginary){
        return toDegrees(arcTan(value, isValueImaginary));
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

    public static Complex arcSecDegrees(Complex value){
        return toDegrees(arcSec(value));
    }

    public static Complex arcSecDegrees(double value, boolean isValueImaginary){
        return toDegrees(arcSec(value, isValueImaginary));
    }

    // arcCsc(a + ib)
    public static Complex arcCosec(Complex value) {
        if (value.getMod() < 1) {
            throw new ArithmeticException("arcCsc is undefined for |z| < 1");
        }
        Complex invZ = ComplexMath.divide(Complex.ONE, value);
        Complex sqrtTerm = ComplexPower.sqrt(ComplexMath.subtract(Complex.ONE, ComplexMath.multiply(invZ, invZ)));
        Complex val = ComplexMath.add(Complex.IOTA, ComplexMath.multiply(invZ, sqrtTerm));
        return ComplexMath.multiply(Complex.NEG_IOTA, ComplexLog.ln(val));
    }

    public static Complex arcCosec(double value, boolean isValueImaginary) {
        return arcCosec(isValueImaginary ? new Complex(0.0, value) : new Complex(value, 0.0));
    }

    public static Complex arcCosecDegrees(Complex value){
        return toDegrees(arcCosec(value));
    }

    public static Complex arcCosecDegrees(double value, boolean isValueImaginary){
        return toDegrees(arcCosec(value, isValueImaginary));
    }

    // arcCot(a + ib)
    public static Complex arcCot(Complex value) {
        if (value.isZero()) {
            throw new ArithmeticException("arcCot is undefined for z = 0");
        }
        Complex iZ = ComplexMath.multiply(Complex.IOTA, value);
        Complex lnTerm = ComplexLog.ln(ComplexMath.divide(ComplexMath.add(Complex.ONE, iZ), ComplexMath.subtract(Complex.ONE, iZ)));
        return ComplexMath.multiply(new Complex(0, -0.5), lnTerm);
    }

    public static Complex arcCot(double value, boolean isValueImaginary) {
        return arcCot(isValueImaginary ? new Complex(0.0, value) : new Complex(value, 0.0));
    }

    public static Complex arcCotDegrees(Complex value){
        return toDegrees(arcCot(value));
    }

    public static Complex arcCotDegrees(double value, boolean isValueImaginary){
        return toDegrees(arcCot(value, isValueImaginary));
    }
}
