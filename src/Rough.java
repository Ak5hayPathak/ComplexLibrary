public class Rough {
    public static void main(String[] args) {
        CubicEquation eq = new CubicEquation(new Complex(2, 3), new Complex(), new Complex(5, -2), new Complex(-3, 1));

        eq.printEquation();
        System.out.println();
        eq.printStandardEquation();
        System.out.println();
        System.out.println();
        eq.printRoots();
        System.out.println();
        System.out.println();

        CubicEquation.printStandardEquation(eq.getRoot1(), eq.getRoot2(), eq.getRoot3());
    }
}
