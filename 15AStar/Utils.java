
/**
 * 
 * 
 * @author Khyber Sen
 */
public class Utils {
    
    private Utils() {}
    
    public static int abs(final int i) {
        final int mask = i >> 31;
        return (i ^ mask) - mask;
    }
    
    public static int manhattanDistance(final int i1, final int j1, final int i2, final int j2) {
        return abs(i1 - i2) + abs(j1 - j2);
    }
    
    public static void main(final String[] args) {
        final int i = 5;
        System.out.println(Integer.toBinaryString(i));
        System.out.println(Integer.toBinaryString(~i));
        System.out.println(i + ~i);
        System.out.println(abs(50));
        System.out.println(abs(-123));
    }
    
}
