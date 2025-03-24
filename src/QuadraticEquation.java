public class QuadraticEquation {
    private final Complex a, b, c;
    private final Complex root1, root2;

    public QuadraticEquation(Complex root1, Complex root2) {
        this.root1 = root1;
        this.root2 = root2;

        // Assume a = 1 for simplicity
        this.a = new Complex(1, 0);
        this.b = ComplexMath.multiply(Complex.NEG_ONE, ComplexMath.add(root1, root2)); // b = -(r1 + r2)
        this.c = ComplexMath.multiply(root1, root2); // c = r1 * r2
    }

    public QuadraticEquation(boolean aHasValue, Complex a, Complex root1, Complex root2) {
        if (a.isZero()) {
            throw new ArithmeticException("Coefficient 'a' cannot be zero for a quadratic equation.");
        }

        this.a = a;
        this.root1 = root1;
        this.root2 = root2;

        this.b = ComplexMath.multiply(a, ComplexMath.multiply(Complex.NEG_ONE, ComplexMath.add(root1, root2)));
        this.c = ComplexMath.multiply(a, ComplexMath.multiply(root1, root2));
    }

    public QuadraticEquation(double root1, double root2) {
        this(new Complex(root1, 0), new Complex(root2, 0));
    }

    public QuadraticEquation(Complex a, Complex b, Complex c) {

        if (a.isZero()) {
            throw new ArithmeticException("Coefficient 'a' cannot be zero for a quadratic equation.");
        }

        this.a = a;
        this.b = b;
        this.c = c;

        Complex discriminant = getDiscriminant(a, b, c);
        Complex sqrtDiscriminant = ComplexPower.sqrt(discriminant);
        Complex negB = ComplexMath.multiply(Complex.NEG_ONE, b);
        Complex denominator = ComplexMath.multiply(2, a);

        this.root1 = ComplexMath.divide(ComplexMath.add(negB, sqrtDiscriminant), denominator);
        this.root2 = ComplexMath.divide(ComplexMath.subtract(negB, sqrtDiscriminant), denominator);

    }

    public QuadraticEquation(double a, double b, double c) {
        this(new Complex(a, 0), new Complex(b, 0), new Complex(c, 0));
    }

    public Complex getRoot1() {return this.root1;}

    public Complex getRoot2() {return this.root2;}

    public Complex getA(){return this.a;}
    public Complex getB(){return this.b;}
    public Complex getC(){return this.c;}

    public void printRoots() {
        System.out.print("Root1: ");
        root1.printComplex();
        System.out.println();
        System.out.print("Root2: ");
        root2.printComplex();
    }

    public static Complex getDiscriminant(Complex a, Complex b, Complex c){
        return ComplexMath.subtract(ComplexMath.multiply(b, b), ComplexMath.multiply(4.0, ComplexMath.multiply(a, c)));
    }

    public static Complex getDiscriminant(double a, double b, double c){
        return getDiscriminant(new Complex(a, 0), new Complex(b, 0), new Complex(c, 0));
    }

    //Solving Quadratic Roots for Complex numbers (a+ib)x² + (c+id)x + (e+if) = 0
    public static QuadraticEquation solveEquation(Complex a, Complex b, Complex c) {

        if(a.isZero()){
            throw new ArithmeticException("Coefficient 'a' cannot be 0.");
        }
        Complex discriminant = getDiscriminant(a, b, c);
        Complex sqrtDiscriminant = ComplexPower.sqrt(discriminant);
        Complex negB = ComplexMath.multiply(Complex.NEG_ONE, b);
        Complex denominator = ComplexMath.multiply(2, a);

        Complex root1 = ComplexMath.divide(ComplexMath.add(negB, sqrtDiscriminant), denominator);
        Complex root2 = ComplexMath.divide(ComplexMath.subtract(negB, sqrtDiscriminant), denominator);

        return new QuadraticEquation(root1, root2);
    }

    public static QuadraticEquation solveEquation(QuadraticEquation Eq){
        return solveEquation(Eq.a, Eq.b, Eq.c);
    }

    //Solving Quadratic Roots for Real numbers ax² + bx + c = 0
    public static QuadraticEquation solveEquation(double a, double b, double c) {
        return solveEquation(new Complex(a, 0), new Complex(b, 0), new Complex(c, 0));
    }

    public void printEquation(int precision){
        printEquation(this.a, this.b, this.c, precision);
    }

    public void printEquation(){
            printEquation(this.a, this.b, this.c, 3);
    }

    public void printStandardEquation(int precision){
        printStandardEquation(this.root1, this.root2, precision);
    }

    public void printStandardEquation(){
        printStandardEquation(this.root1, this.root2, 3);
    }

    private static void printTerm(Complex num, String term, int precision){
        boolean isNegative = num.reNum < 0.0 || (num.reNum == 0.0 && num.imNum < 0.0);
        Complex absSum = isNegative ? ComplexMath.multiply(num, Complex.NEG_ONE) : num;

        System.out.print(isNegative ? " - " : " + ");
        System.out.print("(");
        absSum.printComplex(precision);
        System.out.print(")" + term);
    }

    public static void printStandardEquation(Complex root1, Complex root2, int precision) {
        QuadraticEquation Eq = new QuadraticEquation(root1, root2);

        System.out.print("x²");

        if(!(Eq.b.isZero())){
            if(Eq.b.equals(Complex.ONE)){
                System.out.print(" + x");
            }
            else if(Eq.b.equals(Complex.NEG_ONE)){
                System.out.print(" - x");
            }else{
                printTerm(Eq.b, "x", precision);
            }
        }

        if(!(Eq.c.isZero())){
            printTerm(Eq.c, " = 0", precision);
        }
    }

    public static void printStandardEquation(Complex root1, Complex root2){
        printStandardEquation(root1, root2, 3);
    }

    public static void printStandardEquation(double root1, double root2, int precision){
        printStandardEquation(new Complex(root1, 0.0), new Complex(root2, 0.0), precision);
    }

    public static void printStandardEquation(double root1, double root2){
        printStandardEquation(new Complex(root1, 0.0), new Complex(root2, 0.0));
    }

    public static void printEquation(Complex a, Complex b, Complex c, int precision) {
        if (a.isZero()) {
            throw new ArithmeticException("Coefficient 'a' cannot be zero for a quadratic equation.");
        }

        QuadraticEquation Eq = new QuadraticEquation(a, b, c);

        if(Eq.a.equals(Complex.ONE)){
            System.out.print("x²");
        }
        else if(Eq.a.equals(Complex.NEG_ONE)){
            System.out.print("-x²");
        }else{
            System.out.print("(");
            Eq.a.printComplex(precision);
            System.out.print(")");
            System.out.print("x²");
        }

        if(!(Eq.b.isZero())){
            if(Eq.b.equals(Complex.ONE)){
                System.out.print(" + x");
            }
            else if(Eq.b.equals(Complex.NEG_ONE)){
                System.out.print(" - x");
            }else{
                printTerm(Eq.b, "x", precision);
            }
        }

        if(!(Eq.c.isZero())){
            printTerm(Eq.c, " = 0", precision);
        }

    }

    public static void printEquation(Complex a, Complex b, Complex c){
            printEquation(a, b, c, 3);
    }

    public static void printEquation(double a, double b, double c, int precision){
        printEquation(new Complex(a, 0.0), new Complex(b, 0.0), new Complex(c, 0.0), precision);
    }

    public static void printEquation(double a, double b, double c){
        printEquation(new Complex(a, 0.0), new Complex(b, 0.0), new Complex(c, 0.0), 3);
    }

}