import java.util.Arrays;

/**
 * 
 * 
 * @author Samuel Konstantinovich
 */
public class Test08 {
    
    private static final int INCREASE = 0;
    private static final int DECREASE = 1;
    private static final int STANDARD = 2;
    private static final int SMALL_RANGE = 3;
    private static final int EMPTY = 4;
    
    private static String name(final int i) {
        if (i == 0) {
            return "Increassing";
        }
        if (i == 1) {
            return "Decreassing";
        }
        if (i == 2) {
            return "Normal Random";
        }
        if (i == 3) {
            return "Random with Few Values";
        }
        if (i == 4) {
            return "size 0 array";
        }
        return "Error stat array";
    }
    
    private static int create(final int min, final int max) {
        return min + (int) (Math.random() * (max - min));
    }
    
    private static int[] makeArray(final int size, final int type) {
        int[] ans = new int[size];
        if (type == STANDARD) {
            for (int i = 0; i < size; i++) {
                ans[i] = create(-1000000, 1000000);
            }
        }
        if (type == INCREASE) {
            int current = -5 * size;
            for (int i = 0; i < size; i++) {
                ans[i] = create(current, current + 10);
                current += 10;
            }
        }
        if (type == DECREASE) {
            int current = 5 * size;
            for (int i = 0; i < size; i++) {
                ans[i] = create(current, current + 10);
                current -= 10;
            }
        }
        if (type == SMALL_RANGE) {
            for (int i = 0; i < size; i++) {
                ans[i] = create(-5, 5);
            }
        }
        if (type == EMPTY) {
            ans = new int[0];
        }
        return ans;
    }
    
    public static void test(int arg) {
        arg--;
        int size = arg % 3;
        if (size >= 0) {
            size = 20000 + size * 60000;
        } else {
            size = 0;
        }
        final int type = arg / 3;
        arg += 1;
        final int[] start = makeArray(size, type);
        final int[] result = Arrays.copyOf(start, start.length);
        Arrays.sort(result);
        final long startTime = System.currentTimeMillis();
        Merge.mergesort(start);
        final long elapsedTime = System.currentTimeMillis() - startTime;
        if (Arrays.equals(start, result)) {
            System.out.println(elapsedTime / 1000.0 + "sec PASS Mergesort Case " + arg + ". "
                    + name(type) + " array, size:" + size);
        } else {
            System.out.println("   ERROR! Case " + arg + " ERROR! " + name(type) + " array, size:"
                    + size + "  ERROR!");
        }
    }
    
    public static void main(final String[] args) {
        for (int arg = 0; arg <= 12; arg++) {
            test(arg);
        }
    }
    
}