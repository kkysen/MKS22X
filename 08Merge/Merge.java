import java.util.Arrays;
import java.util.Random;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Merge {
    
    private static final int INSERTION_SORT_THRESHOLD = 7;
    
    private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();
    
    private static final int PARALLEL_THRESHOLD = 100000000;
    
    private static void swap(final int[] a, final int i, final int j) {
        final int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    // fastest sort if short length and cache locality is high
    private static void insertionSort(final int[] a, final int from, final int to) {
        for (int i = from; i < to; i++) {
            for (int j = i; j > from && a[j - 1] > a[j]; j--) {
                swap(a, j, j - 1);
            }
        }
    }
    
    private static void merge(final int[] src, final int[] dest, final int low, final int mid,
            final int high, final int destLow, final int destHigh) {
        // if already sorted, just copy instead of merging
        if (src[mid - 1] < src[mid]) {
            System.arraycopy(src, low, dest, destLow, high - low);
            return;
        }
        
        for (int k = destLow, i = low, j = mid; k < destHigh; k++) {
            if (j >= high || i < mid && src[i] < src[j]) {
                dest[k] = src[i++];
            } else {
                dest[k] = src[j++];
            }
        }
    }
    
    private static void mergeSort(final int[] src, final int[] dest, int low, int high,
            final int offset) {
        if (high - low < INSERTION_SORT_THRESHOLD) {
            insertionSort(dest, low, high);
            return;
        }
        
        final int mid = low + high >>> 1;
        final int destLow = low;
        final int destHigh = high;
        low += offset;
        high += offset;
        mergeSort(dest, src, low, mid, -offset);
        mergeSort(dest, src, mid, high, -offset);
        merge(src, dest, low, mid, high, destLow, destHigh);
    }
    
    // WARNING: Do not debug multi-threaded code.  It crashed Eclipse.
    private static void mergeSortParallel(final int[] src, final int[] dest, final int low,
            final int high, final int offset, final int numCoresAvailable) {
        if (numCoresAvailable <= 1) {
            mergeSort(src, dest, low, high, offset);
            return;
        }
        final int numCoresRemaining = numCoresAvailable / 2;
        final Thread thread = new Thread(new Runnable() {
            
            @Override
            public void run() {
                final int mid = low + high >>> 1;
                final int srcLow = low + offset;
                final int srcHigh = high + offset;
                mergeSortParallel(dest, src, srcLow, mid, -offset, numCoresRemaining);
                mergeSortParallel(dest, src, mid, srcHigh, -offset, numCoresRemaining);
                merge(src, dest, srcLow, mid, srcHigh, low, high);
            }
            
        });
        System.out.println("starting new thread");
        thread.start();
        try {
            thread.join();
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void mergesort(final int[] a) {
        if (a.length < PARALLEL_THRESHOLD) {
            mergeSort(a.clone(), a, 0, a.length, 0);
        } else {
            mergeSortParallel(a.clone(), a, 0, a.length, 0, NUM_CORES);
        }
    }
    
    private static final int CONSOLE_LIMIT = 4000;
    
    private static String arrayToString(final int[] a) {
        final String s = Arrays.toString(a);
        return s.length() < CONSOLE_LIMIT ? s : s.substring(0, CONSOLE_LIMIT);
    }
    
    public static void main(final String[] args) {
        final int n = 1000000;
        final Random random = new Random();
        //random.setSeed(123456789);
        final int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1000);
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
        mergesort(a);
        final long time1 = System.nanoTime() - start1;
        System.out.println(time1 / 1e9 + " sec");
        System.out.println("mergesorted: " + arrayToString(a));
        if (!Arrays.equals(a, b)) {
            new AssertionError().printStackTrace();
        }
    }
    
}
