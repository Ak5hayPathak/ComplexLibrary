public class CubicEquation {
    private final Complex a, b, c, d;
    private final Complex root1, root2, root3;

    CubicEquation(Complex root1, Complex root2, Complex root3){
        this.root1 = root1;
        this.root2 = root2;
        this.root3 = root3;

        this.a = new Complex(1, 0);
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
        if (a.isNull()) {
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
        if (a.isNull()) {
            throw new ArithmeticException("Coefficient 'a' cannot be zero for a cubic equation.");
        }
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        Complex[] depressed = getDepressedCoffs(a, b, c, d);
        Complex discriminant = getDiscriminant(a, b, c, d);

        Complex negHalfQ = ComplexMath.multiply(Complex.NEG_ONE, ComplexMath.divide(depressed[1], 2));

        Complex u;
        Complex v;
        if (discriminant.isNull()) {
            u = ComplexMath.cbrt(negHalfQ);
            v = u; // Both roots are the same
        } else {
            Complex sqrtDiscriminant = ComplexMath.sqrt(discriminant);
            u = ComplexMath.cbrt(ComplexMath.subtract(negHalfQ, sqrtDiscriminant));
            v = ComplexMath.cbrt(ComplexMath.add(negHalfQ, sqrtDiscriminant));
        }

        Complex bOver3a = ComplexMath.divide(b, ComplexMath.multiply(3, a));
        this.root1 = ComplexMath.subtract(
                ComplexMath.add(u, v),
                bOver3a
        );

        this.root2 = ComplexMath.subtract(
                ComplexMath.add(
                        ComplexMath.multiply(Complex.OMEGA, u),
                        ComplexMath.multiply(Complex.OMEGA_SQR, v)
                ),
                bOver3a
        );

        this.root3 = ComplexMath.subtract(
                ComplexMath.add(
                        ComplexMath.multiply(Complex.OMEGA_SQR, u),
                        ComplexMath.multiply(Complex.OMEGA, v)
                ),
                bOver3a
        );
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

    public static Complex[] getDepressedCoffs(Complex a, Complex b, Complex c, Complex d) {
        if (a.isNull()) {
            throw new ArithmeticException("Coefficient 'a' cannot be zero for a cubic equation.");
        }

        Complex a2 = ComplexMath.power(a, 2); // a^2
        Complex a3 = ComplexMath.multiply(a, a2); // a^3

        // p = (3ac - b^2) / (3a^2)
        Complex p = ComplexMath.divide(
                ComplexMath.subtract(
                        ComplexMath.multiply(3.0, ComplexMath.multiply(a, c)),
                        ComplexMath.power(b, 2.0)
                ),
                ComplexMath.multiply(3.0, a2) // Directly compute denominator
        );

        // q = (2b^3 - 9abc + 27a^2d) / (27a^3)
        Complex q = ComplexMath.divide(
                ComplexMath.add(
                        ComplexMath.subtract(
                                ComplexMath.multiply(2.0, ComplexMath.power(b, 3)), // 2b^3
                                ComplexMath.multiply(9.0, ComplexMath.multiply(a, ComplexMath.multiply(b, c))) // 9abc
                        ),
                        ComplexMath.multiply(27.0, ComplexMath.multiply(a2, d)) // 27a^2d
                ),
                ComplexMath.multiply(27.0, a3) // Directly compute denominator
        );

        return new Complex[]{p, q};
    }

    public static Complex getDiscriminant(Complex a, Complex b, Complex c, Complex d){
        Complex[] Val = getDepressedCoffs(a, b, c, d);
        return ComplexMath.add(
                ComplexMath.power(ComplexMath.divide(Val[1], 2.0), 2), // q^2 / 4
                ComplexMath.power(ComplexMath.divide(Val[0], 3.0), 3)  // p^3 / 27
        );
    }

    public static Complex getDiscriminant(double a, double b, double c, double d){
        return getDiscriminant(new Complex(a, 0), new Complex(b, 0), new Complex(c, 0), new Complex(d, 0));
    }

    public static CubicEquation solveCubic(Complex a, Complex b, Complex c, Complex d){
        Complex[] depressed = getDepressedCoffs(a, b, c, d);
        Complex discriminant = getDiscriminant(a, b, c, d);

        Complex negHalfQ = ComplexMath.multiply(Complex.NEG_ONE, ComplexMath.divide(depressed[1], 2));

        Complex u;
        Complex v;
        if (discriminant.isNull()) {
            u = ComplexMath.cbrt(negHalfQ);
            v = u; // Both roots are the same
        } else {
            Complex sqrtDiscriminant = ComplexMath.sqrt(discriminant);
            u = ComplexMath.cbrt(ComplexMath.subtract(negHalfQ, sqrtDiscriminant));
            v = ComplexMath.cbrt(ComplexMath.add(negHalfQ, sqrtDiscriminant));
        }

        Complex bOver3a = ComplexMath.divide(b, ComplexMath.multiply(3, a));
        Complex root1 = ComplexMath.subtract(
                ComplexMath.add(u, v),
                bOver3a
        );

        Complex root2 = ComplexMath.subtract(
                ComplexMath.add(
                        ComplexMath.multiply(Complex.OMEGA, u),
                        ComplexMath.multiply(Complex.OMEGA_SQR, v)
                ),
                bOver3a
        );

        Complex root3 = ComplexMath.subtract(
                ComplexMath.add(
                        ComplexMath.multiply(Complex.OMEGA_SQR, u),
                        ComplexMath.multiply(Complex.OMEGA, v)
                ),
                bOver3a
        );

        return new CubicEquation(root1, root2, root3);
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

    public void printEquation(boolean isStandardEquation, int precision){
        if(isStandardEquation){
            printEquation(this.root1, this.root2, this.root3, precision);
        }
        else{
            printEquation(this.a, this.b, this.c, this.d, precision);
        }
    }

    public void printEquation(boolean isStandardEquation){
        if(isStandardEquation){
            printEquation(this.root1, this.root2, this.root3, 3);
        }
        else{
            printEquation(this.a, this.b, this.c, this.d, 3);
        }
    }

    public static void printEquation(Complex root1, Complex root2, Complex root3, int precision){
        CubicEquation Eq = new CubicEquation(root1, root2, root3);

        System.out.print("x³");

        if(!(Eq.b.isNull())){
            if(Eq.b.equals(Complex.ONE)){
                System.out.print(" + x²");
            }
            else if(Eq.b.equals(Complex.NEG_ONE)){
                System.out.print(" - x²");
            }else{
                printTerm(Eq.b, "x²", precision);
            }
        }

        if(!(Eq.c.isNull())){
            if(Eq.c.equals(Complex.ONE)){
                System.out.print(" + x");
            }
            else if(Eq.c.equals(Complex.NEG_ONE)){
                System.out.print(" - x");
            }else{
                printTerm(Eq.c, "x", precision);
            }
        }

        if(!(Eq.d.isNull())){
            printTerm(Eq.d, " = 0", precision);
        }
    }

    public static void printEquation(Complex root1, Complex root2, Complex root3){
        printEquation(root1, root2, root3, 3);
    }

    public static void printEquation(double root1, double root2, double root3, int precision){
        CubicEquation Eq = new CubicEquation(root1, root2, root3);
        Eq.printEquation(true, precision);
    }

    public static void printEquation(double root1, double root2, double root3){
        CubicEquation Eq = new CubicEquation(root1, root2, root3);
        Eq.printEquation(true, 3);
    }

    public static void printEquation(Complex a, Complex b, Complex c, Complex d, int precision){
        if (a.isNull()) {
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

        if(!(Eq.b.isNull())){
            if(Eq.b.equals(Complex.ONE)){
                System.out.print(" + x²");
            }
            else if(Eq.b.equals(Complex.NEG_ONE)){
                System.out.print(" - x²");
            }else{
                printTerm(Eq.b, "x²", precision);
            }
        }

        if(!(Eq.c.isNull())){
            if(Eq.c.equals(Complex.ONE)){
                System.out.print(" + x");
            }
            else if(Eq.c.equals(Complex.NEG_ONE)){
                System.out.print(" - x");
            }else{
                printTerm(Eq.c, "x", precision);
            }
        }

        if(!(Eq.d.isNull())){
            printTerm(Eq.d, " = 0", precision);
        }
    }

    public static void printEquation(Complex a, Complex b, Complex c, Complex d){
        CubicEquation Eq = new CubicEquation(a, b, c, d);
        printEquation(Eq.root1, Eq.root2, Eq.root3, 3);
    }

    public static CubicEquation randomEquation(double minLimit, double maxLimit, boolean isInteger){
        if(isInteger){
            return new CubicEquation(Complex.getRandomComplex(minLimit, maxLimit, true),
                    Complex.getRandomComplex(minLimit, maxLimit, true),
                    Complex.getRandomComplex(minLimit, maxLimit, true)
                    ,Complex.getRandomComplex(minLimit, maxLimit, true)
                    );
        }else{
            return new CubicEquation(Complex.getRandomComplex(minLimit, maxLimit, false),
                    Complex.getRandomComplex(minLimit, maxLimit, false),
                    Complex.getRandomComplex(minLimit, maxLimit, false)
                    ,Complex.getRandomComplex(minLimit, maxLimit, false)
                    );
        }
    }

    public static CubicEquation randomEquation(double minLimit, double maxLimit, boolean isInteger, boolean ispureReal){
        if(isInteger){
            if(ispureReal){
                return new CubicEquation(Complex.getRandomComplex(minLimit, maxLimit, true, true),
                        Complex.getRandomComplex(minLimit, maxLimit, true, true),
                        Complex.getRandomComplex(minLimit, maxLimit, true, true)
                        ,Complex.getRandomComplex(minLimit, maxLimit, true, true)
                );
            }else{
                return new CubicEquation(Complex.getRandomComplex(minLimit, maxLimit, true, false),
                        Complex.getRandomComplex(minLimit, maxLimit, true, false),
                        Complex.getRandomComplex(minLimit, maxLimit, true, false)
                        /*,Complex.getRandomComplex(minLimit, maxLimit, true, false)*/
                );
            }
        }else{
            if(ispureReal){
                return new CubicEquation(Complex.getRandomComplex(minLimit, maxLimit, false, true),
                        Complex.getRandomComplex(minLimit, maxLimit, false, true),
                        Complex.getRandomComplex(minLimit, maxLimit, false, true)
                        ,Complex.getRandomComplex(minLimit, maxLimit, false, true)
                );
            }else{
                return new CubicEquation(Complex.getRandomComplex(minLimit, maxLimit, false, false),
                        Complex.getRandomComplex(minLimit, maxLimit, false, false),
                        Complex.getRandomComplex(minLimit, maxLimit, false, false)
                        ,Complex.getRandomComplex(minLimit, maxLimit, false, false)
                );
            }
        }
    }
}
