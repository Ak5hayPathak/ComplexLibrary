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

    public QuadraticEquation(double root1, double root2) {
        this(new Complex(root1, 0), new Complex(root2, 0));
    }

    public QuadraticEquation(Complex a, Complex b, Complex c) {

        if (a.isNull()) {
            throw new ArithmeticException("Coefficient 'a' cannot be 0.");
        }

        this.a = a;
        this.b = b;
        this.c = c;

        Complex discriminant = getDiscriminant(a, b, c);
        Complex sqrtDiscriminant = ComplexMath.sqrt(discriminant);
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

        if(a.isNull()){
            throw new ArithmeticException("Coefficient 'a' cannot be 0.");
        }
        Complex discriminant = getDiscriminant(a, b, c);
        Complex sqrtDiscriminant = ComplexMath.sqrt(discriminant);
        Complex negB = ComplexMath.multiply(Complex.NEG_ONE, b);
        Complex denominator = ComplexMath.multiply(2, a);

        Complex root1 = ComplexMath.divide(ComplexMath.add(negB, sqrtDiscriminant), denominator);
        Complex root2 = ComplexMath.divide(ComplexMath.subtract(negB, sqrtDiscriminant), denominator);

        return new QuadraticEquation(root1, root2);
    }

    //Solving Quadratic Roots for Real numbers ax² + bx + c = 0
    public static QuadraticEquation solveEquation(double a, double b, double c) {
        return solveEquation(new Complex(a, 0), new Complex(b, 0), new Complex(c, 0));
    }

    public void printEquation(int precision){
        printEquation(this.a, this.b, this.c, precision);
    }

    public void printEquation(){
        printEquation(this.a, this.b, this.c);
    }

    public static void printEquation(Complex root1, Complex root2, int precision) {
        Complex sum = ComplexMath.add(root1, root2);  // -(root1 + root2)
        Complex product = ComplexMath.multiply(root1, root2);  // (root1 * root2)

        System.out.print("x²");

        // Print -sum * x term
        if (!sum.isNull()) {
            boolean isNegative = sum.reNum < 0.0 || (sum.reNum == 0.0 && sum.imNum < 0.0);
            Complex absSum = isNegative ? ComplexMath.multiply(sum, Complex.NEG_ONE) : sum;

            System.out.print(isNegative ? " + " : " - ");
            System.out.print("(");
            absSum.printComplex(precision);
            System.out.print(")x ");
        }

        // Print +product term
        if (!product.isNull()) {
            boolean isNegative = product.reNum < 0.0 || (product.reNum == 0.0 && product.imNum < 0.0);
            Complex absProduct = isNegative ? ComplexMath.multiply(product, Complex.NEG_ONE) : product;

            System.out.print(isNegative ? " - " : " + ");
            System.out.print("(");
            absProduct.printComplex(precision);
            System.out.print(")");
        }

        System.out.println(" = 0");
    }

    public static void printEquation(Complex root1, Complex root2){
        printEquation(root1, root2, 3);
    }

    public static void printEquation(double root1, double root2, int precision){
        printEquation(new Complex(root1, 0.0), new Complex(root2, 0.0), precision);
    }

    public static void printEquation(double root1, double root2){
        printEquation(new Complex(root1, 0.0), new Complex(root2, 0.0));
    }

    public static void printEquation(Complex a, Complex b, Complex c, int precision) {
        System.out.print("(");
        a.printComplex(precision);
        System.out.print(")x²");

        // Print b * x term
        if (!b.isNull()) {
            boolean isNegative = b.reNum < 0.0 || (b.reNum == 0.0 && b.imNum < 0.0);
            Complex absB = isNegative ? ComplexMath.multiply(b, Complex.NEG_ONE) : b;

            System.out.print(isNegative ? " - " : " + ");
            System.out.print("(");
            absB.printComplex(precision);
            System.out.print(")x ");
        }

        // Print c term
        if (!c.isNull()) {
            boolean isNegative = c.reNum < 0.0 || (c.reNum == 0.0 && c.imNum < 0.0);
            Complex absC = isNegative ? ComplexMath.multiply(c, Complex.NEG_ONE) : c;

            System.out.print(isNegative ? " - " : " + ");
            System.out.print("(");
            absC.printComplex(precision);
            System.out.print(")");
        }

        System.out.println(" = 0");
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

    public static QuadraticEquation randomEquation(double minLimit, double maxLimit, boolean isInteger){
        if(isInteger){
                return new QuadraticEquation(Complex.getRandomComplex(minLimit, maxLimit, true),
                        Complex.getRandomComplex(minLimit, maxLimit, true),
                        Complex.getRandomComplex(minLimit, maxLimit, true));
        }else{
                return new QuadraticEquation(Complex.getRandomComplex(minLimit, maxLimit, false),
                        Complex.getRandomComplex(minLimit, maxLimit, false),
                        Complex.getRandomComplex(minLimit, maxLimit, false));
        }
    }

    public static QuadraticEquation randomEquation(double minLimit, double maxLimit, boolean isInteger, boolean ispureReal){
        if(isInteger){
            if(ispureReal){
                return new QuadraticEquation(Complex.getRandomComplex(minLimit, maxLimit, true, true),
                        Complex.getRandomComplex(minLimit, maxLimit, true, true),
                        Complex.getRandomComplex(minLimit, maxLimit, true, true));
            }else{
                return new QuadraticEquation(Complex.getRandomComplex(minLimit, maxLimit, true, false),
                        Complex.getRandomComplex(minLimit, maxLimit, true, false),
                        Complex.getRandomComplex(minLimit, maxLimit, true, false));
            }
        }else{
            if(ispureReal){
                return new QuadraticEquation(Complex.getRandomComplex(minLimit, maxLimit, false, true),
                        Complex.getRandomComplex(minLimit, maxLimit, false, true),
                        Complex.getRandomComplex(minLimit, maxLimit, false, true));
            }else{
                return new QuadraticEquation(Complex.getRandomComplex(minLimit, maxLimit, false, false),
                        Complex.getRandomComplex(minLimit, maxLimit, false, false),
                        Complex.getRandomComplex(minLimit, maxLimit, false, false));
            }
        }
    }
}