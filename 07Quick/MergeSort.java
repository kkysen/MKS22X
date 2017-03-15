import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MergeSort {
    
    private static final int L1_CACHE_BLOCK_SIZE = 64 * 8; // bits
    private static final int INT_ARRAY_BLOCK_SIZE = L1_CACHE_BLOCK_SIZE / Integer.SIZE;
    
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
    
}
