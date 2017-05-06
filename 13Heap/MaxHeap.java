/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class MaxHeap<E extends Comparable<? super E>> extends Heap<E> {
    
    public MaxHeap(final int initialCapacity) {
        super(initialCapacity, Order.MAX);
    }
    
    public MaxHeap() {
        super(Order.MAX);
    }
    
}
