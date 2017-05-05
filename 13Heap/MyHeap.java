import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MyHeap extends Heap<String> {
    
    private static final Random random = ThreadLocalRandom.current();
    
    public MyHeap() {
        this(true);
    }
    
    public MyHeap(final boolean max) {
        super(!max);
    }
    
    private static String randomString(final int length) {
        final byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return new String(bytes);
    }
    
    public static void main(final String[] args) {
        final String[] a = new String[1000000];
        for (int i = 0; i < a.length; i++) {
            a[i] = randomString(100);
        }
        Heap.heapSort(a);
        System.out.println(Heap.isSorted(a));
        assert Heap.isSorted(a);
    }
    
}