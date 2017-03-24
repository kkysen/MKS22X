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
            while (a[--j] > pivot) {}
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
    
    private static final int INSERTION_SORT_THRESHOLD = 7;
    
    private static void insertionSort(final int[] a, final int low, final int high) {
        for (int i = low; i < high; i++) {
            for (int j = i; j > low && a[j - 1] > a[j]; j--) {
                swap(a, j, j - 1);
            }
        }
    }
    
    private static void quickSort(final int[] a, final int low, final int high) {
        if (high - low < INSERTION_SORT_THRESHOLD) {
            insertionSort(a, low, high + 1);
            return;
        }
        final int pivot = a[low];
        int i = low;
        int j = high + 1;
        while (true) {
            while (a[++i] < pivot && i != high) {}
            while (a[--j] > pivot) {}
            if (i >= j) {
                break;
            }
            swap(a, i, j);
        }
        swap(a, j, low);
        if (low == 0 && high == a.length - 1) {
            System.out.println(j);
        }
        quickSort(a, low, j - 1);
        quickSort(a, j + 1, high);
    }
    
    /*
     * Dutch National Flag
     *
     *   left part    center part              right part
     * +-------------------------------------------------+
     * |  < pivot  |   == pivot   |     ?    |  > pivot  |
     * +-------------------------------------------------+
     *              ^              ^        ^
     *              |              |        |
     *             less            i      great
     *
     * Invariants:
     *
     *   all in (left, less)   < pivot
     *   all in [less, i)     == pivot
     *   all in (great, right) > pivot
     *
     * Pointer i is the first index of ?-part.
     */
    private static void quickSortThreeWay(final int[] a, final int left, final int right) {
        if (right - left < INSERTION_SORT_THRESHOLD) {
            insertionSort(a, left, right + 1);
            return;
        }
        
        int less = left + 1;
        int great = right;
        final int pivot = a[left];
        
        for (int i = less; i <= great; i++) {
            if (a[i] == pivot) {
                continue;
            }
            final int ai = a[i];
            if (ai < pivot) { // Move a[i] to left part
                a[i] = a[less];
                a[less] = ai;
                less++;
            } else { // a[k] > pivot - Move a[i] to right part
                while (a[great] > pivot) {
                    great--;
                }
                if (a[great] < pivot) { // a[great] <= pivot
                    a[i] = a[less];
                    a[less] = a[great];
                    less++;
                } else { // a[great] == pivot
                    a[i] = pivot;
                }
                a[great] = ai;
                great--;
            }
        }
        swap(a, left, less);
        quickSortThreeWay(a, left, less == 0 ? 0 : left + 1);
        quickSortThreeWay(a, great == right ? right : right - 1, right);
        //        quickSortThreeWay(a, left, less + 1);
        //        quickSortThreeWay(a, great - 1, right);
    }
    
    private static void shuffle(final int[] a) {
        final Random random = ThreadLocalRandom.current();
        for (int i = a.length; i > 1; i--) {
            swap(a, i - 1, random.nextInt(i));
        }
    }
    
    public static void quicksort(final int[] a) {
        shuffle(a);
        quickSortThreeWay(a, 0, a.length - 1);
    }
    
    public static void quickSelectSort(final int[] a) {
        final int[] b = a.clone();
        for (int i = 0; i < a.length; i++) {
            a[i] = quickselect(b, i);
        }
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
            a[i] = random.nextInt(100);
        }
        System.out.println("original: " + arrayToString(a));
        final int[] b = a.clone();
        System.out.println();
        final long start0 = System.nanoTime();
        Arrays.sort(b);
        final long time0 = System.nanoTime() - start0;
        System.out.println(time0 / 1e9 + " sec");
        System.out.println("Arrays.sort: " + arrayToString(b));
        System.out.println();
        final long start1 = System.nanoTime();
        //quickSelectSort(a);
        quicksort(a);
        final long time1 = System.nanoTime() - start1;
        System.out.println(time1 / 1e9 + " sec");
        System.out.println("quicksorted: " + arrayToString(a));
        if (!Arrays.equals(a, b)) {
            new AssertionError().printStackTrace();
        }
    }
    
    public static void main(final String[] args) {
        sortTest(10);
    }
    
}
