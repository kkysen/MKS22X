import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Sorts {
    
    private static void swap(final int[] a, final int i, final int j) {
        final int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    private static int partition(final int[] a, final int low, final int high,
            final int pivotIndex) {
        final int pivot = a[pivotIndex];
        int i = low;
        int j = high;
        while (i <= j) {
            while (a[i] <= pivot && i < high) {
                i++;
            }
            while (a[j] >= pivot && j > low) {
                j--;
            }
            if (i <= j) {
                swap(a, i++, j--);
            }
        }
        i--;
        swap(a, i, pivotIndex);
        return i;
    }
    
    public static void partition(final int[] a, final int pivotIndex) {
        partition(a, 0, a.length - 1, pivotIndex);
    }
    
    public static int kthSmallest(final int[] a, final int k) {
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
    
    private static final int L1_CACHE_BLOCK_SIZE = 64 * 8; // bits
    private static final int INT_ARRAY_BLOCK_SIZE = L1_CACHE_BLOCK_SIZE / Integer.SIZE;
    
    // fastest sort if short length and cache locality is high
    private static void insertionSort(final int[] a, final int from, final int to) {
        for (int i = from; i < to; i++) {
            for (int j = i; j > from && a[j - 1] > a[j]; j--) {
                swap(a, j, j - 1);
            }
        }
    }
    
    private static void mergeSort(final int[] a) {
        final int numFullBlocks = a.length / INT_ARRAY_BLOCK_SIZE;
        final int length = numFullBlocks * INT_ARRAY_BLOCK_SIZE;
        // sort all blocks using insertion sort first
        for (int i = 0; i < numFullBlocks;) {
            insertionSort(a, i * INT_ARRAY_BLOCK_SIZE, ++i * INT_ARRAY_BLOCK_SIZE);
        }
        
        // merge these sorted blocks of same size
        final int[] b = Arrays.copyOf(a, length);
        
        // sort and insert last, cut-off block
        insertionSort(a, numFullBlocks * INT_ARRAY_BLOCK_SIZE, a.length);
        
    }
    
    public static void main(final String[] args) {
        final int[] a = {2, 5, 3, 7, 6, 8, 9, 1};
        final int[] b = a.clone();
        partition(a, 6);
        System.out.println(Arrays.toString(a));
        for (int i = 0; i < b.length; i++) {
            System.out.println(kthSmallest(b, i));
        }
    }
    
}
