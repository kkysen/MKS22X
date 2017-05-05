
/**
 * 
 * 
 * @author Khyber Sen
 */
public class IntHeap extends Heap<Integer> {
    
    public IntHeap() {
        this(false);
    }
    
    public IntHeap(final boolean max) {
        super(!max);
    }
    
    public static void main(final String[] args) {
        final IntHeap heap = new IntHeap(false);
        heap.add(5);
        heap.add(6);
        heap.add(4);
        heap.add(10);
        heap.add(11);
        heap.add(-1);
        heap.add(Integer.MAX_VALUE);
        heap.add(Integer.MIN_VALUE);
        for (int i = 0; i < 100; i++) {
            heap.add(i * 10);
        }
        System.out.println(heap);
        System.out.println(heap.toTreeString());
        
        System.out.println();
        System.out.println(heap.remove());
        System.out.println(heap);
        System.out.println(heap.toTreeString());
        
        while (!heap.isEmpty()) {
            System.out.println(heap.remove());
        }
    }
    
}
