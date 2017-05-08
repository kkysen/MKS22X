
/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class MinHeap<E extends Comparable<? super E>> extends GenericHeap<E> {
    
    public MinHeap(final int initialCapacity) {
        super(initialCapacity, Order.MIN);
    }
    
    public MinHeap() {
        super(Order.MIN);
    }
    
}
