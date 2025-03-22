public class Rough {
    public static void main(String[] args) {
//        CubicEquation Eq = new CubicEquation(1, 0, 1, -2);
//        Eq.printRoots();

//        CubicEquation Eq = new CubicEquation(2, 9, 13, 6);
//        Eq.printRoots();

//        CubicEquation Eq = new CubicEquation(1, 0, -7, 6);
//        Eq.printRoots();

//        Complex r1 = new Complex(1.396, 0.228);
//        Complex r2 = new Complex(-0.500, 0.866);
//        Complex r3 = new Complex(-0.896, 1.094);
//
//        CubicEquation Eq = new CubicEquation(r1, r2, r3);
//        System.out.println();
//        Eq.printStandardEquation();
        //CubicEquation Eq = new CubicEquation(r1, r2, r3);

        //CubicEquation Eq = new CubicEquation(1, 0, 1, -2);
        //CubicEquation.getDiscriminant(1, 0, 1, -2).printComplex();
        //CubicEquation.solveCubic(1, 0, 1, -2).printRoots();


//        Complex[] array = CubicEquation.getDepressedCoffs(new Complex(2, 0), new Complex(-5, 0), new Complex(3, 0), new Complex(-7, 0));
//        array[0].printComplex();
//        System.out.println();
//        array[1].printComplex();
//        System.out.println();
//        CubicEquation.getDiscriminant(2, -5, 3, -7).printComplex();

        CubicEquation eq = new CubicEquation(1, 0, 0, -1);
        eq.printEquation(0);
        System.out.println();
        eq.printRoots();

        Complex r1 = eq.getRoot1();
        Complex r2 = eq.getRoot2();
        Complex r3 = eq.getRoot3();

        CubicEquation eq2 = new CubicEquation(r1, r2, r3);
        System.out.println();
        eq2.printEquation(0);

    }
}
