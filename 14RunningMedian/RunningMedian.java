import java.util.Arrays;
import java.util.Random;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class RunningMedian extends MedianHeap<Integer> {
    
    public RunningMedian() {}
    
    public RunningMedian(final int initialCapacity) {
        super(initialCapacity);
    }
    
    @Override
    public Integer median() {
        final int cmp = lesser.size() - greater.size();
        if (cmp > 0) {
            return lesser.peek();
        } else if (cmp < 0) {
            return greater.peek();
        } else {
            return lesser.peek() + greater.peek() / 2;
        }
    }
    
    public void add(final int i) {
        add(i);
    }
    
    public int getMedian() {
        return median();
    }
    
    public static void main(final String[] args) {
        final RunningMedian medians = new RunningMedian();
        final int size = 1000001;
        final int[] a = new int[size];
        final Random random = new Random();
        for (int i = 0; i < size; i++) {
            final int x = random.nextInt();
            a[i] = x;
            medians.add(x);
        }
        Arrays.sort(a);
        final int sortedMedian = a[size >>> 1];
        final int heapMedian = medians.median();
        System.out.println(sortedMedian + " == " + heapMedian);
    }
    
}
