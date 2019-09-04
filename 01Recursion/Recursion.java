/**
 * Recursion Lab
 * 
 * implements sqrt recursively using Newton's method
 * 
 * @since 08-02-2017
 * 
 * @author Khyber Sen
 */
public class Recursion {
    
    public static String name() {
        return "Sen,Khyber";
    }
    
    /**
     * calculates the sqrt of a number recursively using Newton's method
     * 
     * @param n a number
     * @return the sqrt of n
     */
    public static double sqrt(final double n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        return sqrt(n, 1);
    }
    
    private static double sqrt(final double n, final double guess) {
        if (equals(guess * guess, n)) {
            return guess;
        }
        return sqrt(n, (n / guess + guess) / 2);
    }
    
    private static final double EPSILON = 1e-12;
    
    private static boolean equals(final double a, final double b) {
        final double diff = Math.abs(a - b);
        if (a == 0 && b == 0) {
            return diff < EPSILON;
        }
        final double divisor = a == 0 ? b : a;
        final double percent = diff / divisor;
        return percent < EPSILON;
    }
    
    public static void main(final String[] args) {
        double d = 0;
        for (int i = 0; i < 100; i++, d += 1.00000000000001) {
            System.out.println(equals(i, d));
        }
        System.out.println(equals(10000, 10000.000001));
        System.out.println(sqrt(4));
        System.out.println(equals(sqrt(4), 2));
    }
    
}
