public class Rough {
    public static void main(String[] args) {

        for(int i=0; i<100; i++){
            Complex a = Complex.getRandomComplex(1, 5, true, true);
            Complex b = Complex.getRandomComplex(1, 5, true, true);
            Complex c = Complex.getRandomComplex(1, 5, true, true);

            if(Math.pow(b.reNum, 2) < 4*a.reNum* c.reNum){
                QuadraticEquation eq = new QuadraticEquation(a, b, c);
                eq.printEquation(0);
                System.out.println();
                eq.printRoots();
                System.out.println("\n");
            }

        }




    }
}
