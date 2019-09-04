import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Quiz2Redux {
    
    public static String name() {
        return "Sen,Khyber";
    }
    
    public static ArrayList<String> combinations(final String s) {
        final ArrayList<String> words = new ArrayList<>(1 << s.length());
        help(s, words, 0, "");
        Collections.sort(words);
        return words;
    }
    
    private static void help(final String s, final List<String> words, final int i,
            final String sub) {
        if (i == s.length()) {
            words.add(sub);
            return;
        }
        help(s, words, i + 1, sub);
        help(s, words, i + 1, sub + s.charAt(i));
    }
    
    public static void main(final String[] args) {
        //        final long start = System.currentTimeMillis();
        //        System.out.println(combinations("abcdefghijklmnopqrst").size());
        //        System.out.println((System.currentTimeMillis() - start) / 1e4);
        System.out.println(combinations("abcd"));
    }
    
}
