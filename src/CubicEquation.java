public class CubicEquation {
    private final Complex a, b, c, d;
    private final Complex root1, root2, root3;

    CubicEquation(Complex root1, Complex root2, Complex root3){
        this.root1 = root1;
        this.root2 = root2;
        this.root3 = root3;

        this.a = Complex.ONE;
        this.b = ComplexMath.multiply(Complex.NEG_ONE,
                ComplexMath.add(ComplexMath.add(root1, root2), root3)
        );
        this.c = ComplexMath.add(
                ComplexMath.add(
                        ComplexMath.multiply(root1, root2),ComplexMath.multiply(root2, root3)
                ),ComplexMath.multiply(root3, root1)
        );
        this.d = ComplexMath.multiply(Complex.NEG_ONE,
                ComplexMath.multiply(root1, ComplexMath.multiply(root2, root3))
        );

    }

    CubicEquation(double root1, double root2, double root3){
        this(new Complex(root1, 0), new Complex(root2, 0), new Complex(root3, 0));
    }

    CubicEquation(boolean aHasValue, Complex a, Complex root1, Complex root2, Complex root3){
        if (a.isZero()) {
            throw new ArithmeticException("Coefficient 'a' cannot be zero for a cubic equation.");
        }
        this.root1 = root1;
        this.root2 = root2;
        this.root3 = root3;

        this.a = a;
        this.b = ComplexMath.multiply(a, ComplexMath.multiply(Complex.NEG_ONE,
                ComplexMath.add(ComplexMath.add(root1, root2), root3)
        ));

        this.c = ComplexMath.multiply(a, ComplexMath.add(
                ComplexMath.add(
                        ComplexMath.multiply(root1, root2),ComplexMath.multiply(root2, root3)
                ),ComplexMath.multiply(root3, root1)
        ));
        this.d = ComplexMath.multiply(a, ComplexMath.multiply(Complex.NEG_ONE,
                ComplexMath.multiply(root1, ComplexMath.multiply(root2, root3))
        ));
    }

    CubicEquation(boolean aHasValue, double a, double root1, double root2, double root3){
        this(aHasValue, new Complex(a, 0), new Complex(root1, 0), new Complex(root2, 0), new Complex(root3, 0));
    }

    CubicEquation(Complex a, Complex b, Complex c, Complex d) {
        if (a.isZero()) {
            throw new ArithmeticException("Coefficient 'a' cannot be zero for a cubic equation.");
        }
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        Complex[] roots = new Complex[3];
        if (this.b.isZero()){
            if(this.c.isZero()){
                roots[0] = ComplexPower.cbrt(ComplexMath.multiply(Complex.NEG_ONE, ComplexMath.divide(d, a)));
                roots[1] = ComplexMath.multiply(Complex.OMEGA, roots[0]);
                roots[2] = ComplexMath.multiply(Complex.OMEGA_SQR, roots[0]);
            }
            else{
                roots = getDepressedRoots(a, c, d);
            }
        }
        else{
            Complex[] depressed = getDepressedCoffs(a, b, c, d);
            Complex[] UV = getUV(depressed[0], depressed[1]);

            roots = cardanoRoots(a, b, UV[0], UV[1]);
        }

        this.root1 = roots[0];
        this.root2 = roots[1];
        this.root3 = roots[2];
    }

    CubicEquation(double a, double b, double c, double d){
        this(new Complex(a, 0), new Complex(b, 0), new Complex(c, 0), new Complex(d, 0));
    }

    public Complex getRoot1() {return this.root1;}
    public Complex getRoot2() {return this.root2;}
    public Complex getRoot3() {return this.root3;}

    public Complex getA(){return this.a;}
    public Complex getB(){return this.b;}
    public Complex getC(){return this.c;}
    public Complex getD(){return this.d;}

    public void printRoots() {
        System.out.print("Root1: ");
        this.root1.printComplex();
        System.out.println();
        System.out.print("Root2: ");
        this.root2.printComplex();
        System.out.println();
        System.out.print("Root3: ");
        this.root3.printComplex();
    }

    protected static Complex[] getDepressedCoffs(Complex a, Complex b, Complex c, Complex d) {
        if (a.isZero()) {
            throw new ArithmeticException("Coefficient 'a' cannot be zero for a cubic equation.");
        }

        Complex a2 = ComplexPower.power(a, 2); // a^2
        Complex a3 = ComplexMath.multiply(a, a2); // a^3

        // p = (3ac - b^2) / (3a^2)
        Complex p = ComplexMath.divide(
                ComplexMath.subtract(
                        ComplexMath.multiply(3.0, ComplexMath.multiply(a, c)),
                        ComplexPower.power(b, 2.0)
                ),
                ComplexMath.multiply(3.0, a2) // Directly compute denominator
        );

        // q = (2b^3 - 9abc + 27a^2d) / (27a^3)
        Complex q = ComplexMath.divide(
                ComplexMath.add(
                        ComplexMath.subtract(
                                ComplexMath.multiply(2.0, ComplexPower.power(b, 3)), // 2b^3
                                ComplexMath.multiply(9.0, ComplexMath.multiply(a, ComplexMath.multiply(b, c))) // 9abc
                        ),
                        ComplexMath.multiply(27.0, ComplexMath.multiply(a2, d)) // 27a^2d
                ),
                ComplexMath.multiply(27.0, a3) // Directly compute denominator
        );

        return new Complex[]{p, q};
    }

    protected static Complex[] getUV(Complex p, Complex q) {
        Complex discriminant = getDiscriminant(p, q);
        Complex sqrtDiscriminant = ComplexPower.sqrt(discriminant);
        Complex negHalfQ = ComplexMath.divide(q, -2);

        Complex[] uRoots = ComplexPower.cbrtAll(ComplexMath.subtract(negHalfQ, sqrtDiscriminant));
        Complex[] vRoots = ComplexPower.cbrtAll(ComplexMath.add(negHalfQ, sqrtDiscriminant));

        // Choose the pair (u, v) that satisfies uv = -p/3
        for (Complex u : uRoots) {
            for (Complex v : vRoots) {
                if (ComplexMath.multiply(u, v).equals(ComplexMath.divide(p, -3))) {
                    return new Complex[]{u, v};
                }
            }
        }
        throw new RuntimeException("No valid (u, v) pair found.");
    }

    protected static Complex[] cardanoRoots(Complex a, Complex b, Complex u, Complex v) {
        Complex bOver3a = ComplexMath.divide(b, ComplexMath.multiply(3, a));
        Complex addUV = ComplexMath.add(u, v);

        // Precompute omega multiplications
        Complex omegaU = ComplexMath.multiply(Complex.OMEGA, u);
        Complex omegaSqrV = ComplexMath.multiply(Complex.OMEGA_SQR, v);
        Complex omegaSqrU = ComplexMath.multiply(Complex.OMEGA_SQR, u);
        Complex omegaV = ComplexMath.multiply(Complex.OMEGA, v);

        return new Complex[]{
                ComplexMath.subtract(addUV, bOver3a),
                ComplexMath.subtract(ComplexMath.add(omegaU, omegaSqrV), bOver3a),
                ComplexMath.subtract(ComplexMath.add(omegaSqrU, omegaV), bOver3a)
        };
    }

    public static Complex[] getDepressedRoots(Complex a, Complex c, Complex d){
        Complex p = ComplexMath.divide(c, a);
        Complex q =ComplexMath.divide(d, a);


        Complex theta = ComplexTrigono.arcCos(ComplexMath.multiply(
                ComplexMath.divide(
                        ComplexMath.multiply(3.0, q),
                        ComplexMath.multiply(2.0, p)
                ),
                ComplexPower.sqrt(
                        ComplexMath.divide(-3.0, p)
                )
        ));

        Complex pTerm = ComplexMath.multiply(2.0, ComplexPower.sqrt(
                ComplexMath.divide(p, -3.0)
        ));

        Complex[] roots = new Complex[3];

        roots[0] = ComplexMath.multiply(
                pTerm,
                ComplexTrigono.cos(ComplexMath.divide(theta, 3.0))
        );

        roots[1] = ComplexMath.multiply(
                pTerm,
                ComplexTrigono.cos(ComplexMath.divide(
                        ComplexMath.add(theta, 2*Math.PI),
                        3.0))
        );

        roots[2] = ComplexMath.multiply(
                pTerm,
                ComplexTrigono.cos(ComplexMath.divide(
                        ComplexMath.add(theta, 4*Math.PI),
                        3.0))
        );

        return roots;
    }

    public static Complex getDiscriminant(Complex a, Complex b, Complex c, Complex d){
        Complex[] Val = getDepressedCoffs(a, b, c, d);
        return getDiscriminant(Val[0], Val[1]);
    }

    protected static Complex getDiscriminant(Complex p, Complex q){
        return ComplexMath.add(
                ComplexPower.power(ComplexMath.divide(q, 2.0), 2), // q^2 / 4
                ComplexPower.power(ComplexMath.divide(p, 3.0), 3)  // p^3 / 27
        );
    }

    public static Complex getDiscriminant(double a, double b, double c, double d){
        return getDiscriminant(new Complex(a, 0), new Complex(b, 0), new Complex(c, 0), new Complex(d, 0));
    }

    public static CubicEquation solveCubic(Complex a, Complex b, Complex c, Complex d){
        if (a.isZero()) {
            throw new ArithmeticException("Coefficient 'a' cannot be zero for a cubic equation.");
        }

        Complex[] roots = new Complex[3];
        if (b.isZero()){
            if(c.isZero()){
                roots[0] = ComplexPower.cbrt(ComplexMath.multiply(Complex.NEG_ONE, ComplexMath.divide(d, a)));
                roots[1] = ComplexMath.multiply(Complex.OMEGA, roots[0]);
                roots[2] = ComplexMath.multiply(Complex.OMEGA_SQR, roots[0]);
            }
            else{
                roots = getDepressedRoots(a, c, d);
            }
        }
        else{
            Complex[] depressed = getDepressedCoffs(a, b, c, d);
            Complex[] UV = getUV(depressed[0], depressed[1]);

            roots = cardanoRoots(a, b, UV[0], UV[1]);
        }

        return new CubicEquation(roots[0], roots[1], roots[2]);
    }

    public static CubicEquation solveCubic(CubicEquation Eq){
        return solveCubic(Eq.a, Eq.b, Eq.c, Eq.d);
    }

    public static CubicEquation solveCubic(double a, double b, double c, double d){
        return solveCubic(new Complex(a, 0), new Complex(b, 0), new Complex(c, 0), new Complex(d, 0));
    }

    private static void printTerm(Complex num, String term, int precision){
        boolean isNegative = num.reNum < 0.0 || (num.reNum == 0.0 && num.imNum < 0.0);
        Complex absSum = isNegative ? ComplexMath.multiply(num, Complex.NEG_ONE) : num;

        System.out.print(isNegative ? " - " : " + ");
        System.out.print("(");
        absSum.printComplex(precision);
        System.out.print(")" + term);
    }

    public void printEquation(int precision){
        printEquation(this.a, this.b, this.c, this.d, precision);
    }

    public void printEquation(){
        printEquation(this.a, this.b, this.c, this.d, 3);
    }

    public void printStandardEquation(int precision){
        printStandardEquation(this.root1, this.root2, this.root3, precision);
    }

    public void printStandardEquation(){
        printStandardEquation(this.root1, this.root2, this.root3, 3);
    }

    public static void printStandardEquation(Complex root1, Complex root2, Complex root3, int precision){
        CubicEquation Eq = new CubicEquation(root1, root2, root3);

        System.out.print("x³");

        if(!(Eq.b.isZero())){
            if(Eq.b.equals(Complex.ONE)){
                System.out.print(" + x²");
            }
            else if(Eq.b.equals(Complex.NEG_ONE)){
                System.out.print(" - x²");
            }else{
                printTerm(Eq.b, "x²", precision);
            }
        }

        if(!(Eq.c.isZero())){
            if(Eq.c.equals(Complex.ONE)){
                System.out.print(" + x");
            }
            else if(Eq.c.equals(Complex.NEG_ONE)){
                System.out.print(" - x");
            }else{
                printTerm(Eq.c, "x", precision);
            }
        }

        if(!(Eq.d.isZero())){
            printTerm(Eq.d, " = 0", precision);
        }
    }

    public static void printStandardEquation(Complex root1, Complex root2, Complex root3){
        printStandardEquation(root1, root2, root3, 3);
    }

    public static void printStandardEquation(double root1, double root2, double root3, int precision){
        CubicEquation Eq = new CubicEquation(root1, root2, root3);
        Eq.printStandardEquation(precision);
    }

    public static void printStandardEquation(double root1, double root2, double root3){
        CubicEquation Eq = new CubicEquation(root1, root2, root3);
        Eq.printStandardEquation(3);
    }

    public static void printEquation(Complex a, Complex b, Complex c, Complex d, int precision){
        if (a.isZero()) {
            throw new ArithmeticException("Coefficient 'a' cannot be zero for a cubic equation.");
        }

        CubicEquation Eq = new CubicEquation(a, b, c, d);

        if(Eq.a.equals(Complex.ONE)){
            System.out.print("x³");
        }
        else if(Eq.a.equals(Complex.NEG_ONE)){
            System.out.print("-x³");
        }else{
            System.out.print("(");
            Eq.a.printComplex(precision);
            System.out.print(")");
            System.out.print("x³");
        }

        if(!(Eq.b.isZero())){
            if(Eq.b.equals(Complex.ONE)){
                System.out.print(" + x²");
            }
            else if(Eq.b.equals(Complex.NEG_ONE)){
                System.out.print(" - x²");
            }else{
                printTerm(Eq.b, "x²", precision);
            }
        }

        if(!(Eq.c.isZero())){
            if(Eq.c.equals(Complex.ONE)){
                System.out.print(" + x");
            }
            else if(Eq.c.equals(Complex.NEG_ONE)){
                System.out.print(" - x");
            }else{
                printTerm(Eq.c, "x", precision);
            }
        }

        if(!(Eq.d.isZero())){
            printTerm(Eq.d, " = 0", precision);
        }
    }

    public static void printEquation(Complex a, Complex b, Complex c, Complex d){
        printEquation(a, b, c, d, 3);
    }
}