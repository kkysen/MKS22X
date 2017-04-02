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
        random.setSeed(123456789);
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
    
    public static int quickselect3(final int[] a, final int k) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        random.setSeed(123456789);
        int low = 0;
        int high = a.length - 1;
        while (true) {
            final int pivotIndex = random.nextInt(low, high + 1);
            final int pivot = a[pivotIndex];
            int i = low;
            int j = high + 1;
            // equal to pivot stored behind these pointers
            int p = low + 1;
            int q = high;
            swap(a, low, pivotIndex);
            while (true) {
                while (a[++i] < pivot && i != high) {}
                while (a[--j] > pivot) {}
                if (i >= j) {
                    break;
                }
                final int ai = a[i];
                final int aj = a[j];
                a[i] = aj;
                a[j] = ai;
                if (aj == pivot) {
                    a[i] = a[p];
                    a[p] = pivot;
                    p++;
                }
                if (ai == pivot) {
                    a[j] = a[q];
                    a[q] = pivot;
                    q--;
                }
            }
            if (a[i] == pivot) {
                i++;
            }
            if (a[j] == pivot) {
                j--;
            }
            for (int x = low; x < p; x++, j--) {
                swap(a, x, j);
            }
            for (int x = high; x > q; x--, i++) {
                swap(a, x, i);
            }
            if (k > j && k < i) {
                return a[j];
            }
            if (j < k) {
                low = i;
            } else {
                high = j;
            }
        }
    }
    
    private static final int INSERTION_SORT_THRESHOLD = 47; // taken from DualPivotQuicksort
    
    private static void insertionSort(final int[] a, final int low, final int high) {
        for (int i = low; i < high; i++) {
            for (int j = i; j > low && a[j - 1] > a[j]; j--) {
                swap(a, j, j - 1);
            }
        }
    }
    
    private static void quickSortTwoWay(final int[] a, final int low, final int high) {
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
        quickSortTwoWay(a, low, j - 1);
        quickSortTwoWay(a, j + 1, high);
    }
    
    private static void quickSortThreeWay(final int[] a, final int low, final int high) {
        if (high - low < INSERTION_SORT_THRESHOLD) {
            insertionSort(a, low, high + 1);
            return;
        }
        
        final int pivot = a[low];
        
        int i = low;
        int j = high + 1;
        
        // equal to pivot stored behind these pointers
        int p = low + 1;
        int q = high;
        
        while (true) {
            while (a[++i] < pivot && i != high) {}
            while (a[--j] > pivot) {}
            if (i >= j) {
                break;
            }
            final int ai = a[i];
            final int aj = a[j];
            a[i] = aj;
            a[j] = ai;
            if (aj == pivot) {
                a[i] = a[p];
                a[p] = pivot;
                p++;
            }
            if (ai == pivot) {
                a[j] = a[q];
                a[q] = pivot;
                q--;
            }
        }
        
        //System.out.println(p - low + high - q);
        for (int k = low; k < p; k++, j--) {
            swap(a, k, j);
        }
        for (int k = high; k > q; k--, i++) {
            swap(a, k, i);
        }
        
        quickSortThreeWay(a, low, j);
        quickSortThreeWay(a, i, high);
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
        final Random random = new Random();
        random.setSeed(123456789);
        final int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(10);
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
        quickSelectSort(a);
        //quicksort(a);
        final long time1 = System.nanoTime() - start1;
        System.out.println(time1 / 1e9 + " sec");
        System.out.println("quicksorted: " + arrayToString(a));
        if (!Arrays.equals(a, b)) {
            new AssertionError().printStackTrace();
        }
    }
    
    public static void main(final String[] args) {
        sortTest(10000);
        sortTest(10000);
        sortTest(10000);
        sortTest(10000);
    }
    
}
