import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MergeSort {
    
    private static final int INSERTION_THRESHOLD = 7;
    
    private static void swap(final int[] a, final int i, final int j) {
        final int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    private static void mergeSort(final int[] src, final int[] dest, final int from, final int to,
            final int offset) {
        final int length = to - from;
        
        if (length < INSERTION_THRESHOLD) {
            for (int i = from; i < to; i++) {
                for (int j = i; j > from && dest[j - 1] > dest[j]; j--) {
                    swap(dest, j, j - 1);
                }
            }
            return;
        }
        
    }
    
    private static void mergeSort(final int[] a, final int from, final int to) {
        final int[] copy = Arrays.copyOfRange(a, from, to);
        mergeSort(copy, a, from, to, -from);
    }
    
    private static void mergeSort(final int[] a) {
        mergeSort(a, 0, a.length);
    }
    
}
