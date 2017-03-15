import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Quick {
    
    // allow setSeed for ThreadLocalRandom.current()
    static {
        Field initialized = null;
        try {
            initialized = ThreadLocalRandom.class.getDeclaredField("initialized");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        initialized.setAccessible(true);
        try {
            initialized.set(ThreadLocalRandom.current(), false);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    private static void swap(final int[] a, final int i, final int j) {
        final int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    private static int partition(final int[] a, final int low, final int high,
            final int pivotIndex) {
        final int pivot = a[pivotIndex];
        int i = low;
        int j = high + 1;
        swap(a, low, pivotIndex);
        while (true) {
            while (a[++i] < pivot && i != high) {}
            while (a[--j] > pivot && j != low) {}
            if (i >= j) {
                break;
            }
            swap(a, i, j);
        }
        swap(a, j, low);
        return j;
    }
    
    public static int partition(final int[] a, final int pivotIndex) {
        return partition(a, 0, a.length - 1, pivotIndex);
    }
    
    public static int part(final int[] a, final int start, final int end) {
        return partition(a, start, end, ThreadLocalRandom.current().nextInt(start, end));
    }
    
    public static int quickselect(final int[] a, final int k) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        int sortedIndex = -1;
        int low = 0;
        int high = a.length;
        for (int pivotIndex = random.nextInt(high); sortedIndex != k; //
                pivotIndex = random.nextInt(low, high)) {
            sortedIndex = partition(a, low, high - 1, pivotIndex);
            if (sortedIndex == k) {
                return a[sortedIndex];
            }
            if (sortedIndex < k) {
                low = sortedIndex;
            } else {
                high = sortedIndex;
            }
        }
        return a[sortedIndex];
    }
    
    public static int[] quickSelectSort(final int[] a) {
        final int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = quickselect(a, i);
        }
        return b;
    }
    
    private static final int CONSOLE_LIMIT = 4000;
    
    private static String arrayToString(final int[] a) {
        final String s = Arrays.toString(a);
        return s.length() < CONSOLE_LIMIT ? s : s.substring(0, CONSOLE_LIMIT);
    }
    
    public static void sortTest(final int n) {
        final Random random = ThreadLocalRandom.current();
        //random.setSeed(123456789);
        final int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt();
        }
        System.out.println("original: " + arrayToString(a));
        final int[] b = a.clone();
        Arrays.sort(b);
        final int[] c = quickSelectSort(a);
        System.out.println("quickselect: " + arrayToString(c));
        System.out.println("quicksorted: " + arrayToString(b));
        if (!Arrays.equals(c, b)) {
            new AssertionError().printStackTrace();
        }
    }
    
    public static void main(final String[] args) {
        sortTest(10000);
    }
    
}
