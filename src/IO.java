import java.util.Scanner;

public class IO {

    private static final Scanner input = new Scanner(System.in);

    // Prevent instantiation
    private IO() {
        throw new UnsupportedOperationException("Cannot instantiate IO.");
    }

    public static int getInt(String prompt) {
        System.out.print(prompt);
        while (!input.hasNextInt()) {
            System.out.println("Invalid input. Please enter an integer.");
            input.nextLine(); // Discard invalid input
            System.out.print(prompt);
        }
        int value = input.nextInt();
        input.nextLine(); // Consume newline character
        return value;
    }

    public static int getIntInRange(int lowerLimit, int upperLimit, String prompt) {
        int value;
        do {
            value = getInt(prompt);
            if (value < lowerLimit || value > upperLimit) {
                System.out.println("Error: Enter a number between " + lowerLimit + " and " + upperLimit + ".");
            }
        } while (value < lowerLimit || value > upperLimit);
        return value;
    }

    public static int getIntInRange(int lowerLimit, String prompt) {
        int value;
        do {
            value = getInt(prompt);
            if (value < lowerLimit) {
                System.out.println("Error: Enter a number greater than or equal to " + lowerLimit + ".");
            }
        } while (value < lowerLimit);
        return value;
    }

    public static double getDouble(String prompt) {
        System.out.print(prompt);
        while (!input.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a decimal number.");
            input.nextLine(); // Discard invalid input
            System.out.print(prompt);
        }
        double value = input.nextDouble();
        input.nextLine(); // Consume newline character
        return value;
    }

    public static double getDoubleInRange(double lowerLimit, double upperLimit, String prompt) {
        double value;
        do {
            value = getDouble(prompt);
            if (value < lowerLimit || value > upperLimit) {
                System.out.println("Error: Enter a number between " + lowerLimit + " and " + upperLimit + ".");
            }
        } while (value < lowerLimit || value > upperLimit);
        return value;
    }

    public static double getDoubleInRange(double lowerLimit, String prompt) {
        double value;
        do {
            value = getDouble(prompt); // Fixed incorrect method call
            if (value < lowerLimit) {
                System.out.println("Error: Enter a number greater than or equal to " + lowerLimit + ".");
            }
        } while (value < lowerLimit);
        return value;
    }

    public static char getChar(String prompt) {
        System.out.print(prompt);
        while (true) {
            String value = input.nextLine().trim();
            if (value.length() == 1) {
                return value.charAt(0);
            }
            System.out.print("Invalid input. Enter a single character: ");
        }
    }

    public static String getString(String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }
}
