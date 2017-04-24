import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Misc {
    
    public static void test(final ArrayList list) {
        for (final Object/*String*/ s : list) {
            System.out.println(s);
        }
    }
    
    public static void main(final String[] args) {
        final ArrayList<String> strings = new ArrayList<>(Arrays.asList("hello", "world"));
        test(strings);
        System.out.println((int) (-3.5 - 0.5));
        System.out.println("holy".substring(4));
        // doesn't compile final int[] list = int[5];
    }
    
}
