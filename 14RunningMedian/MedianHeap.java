import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class MedianHeap<E extends Comparable<? super E>> extends AbstractQueue<E>
        implements Cloneable {
    
    /*
     * if heaps are equal, median is taken from lesser heap
     */
    
    protected final MaxHeap<E> lesser;
    protected final MinHeap<E> greater;
    
    public MedianHeap(final int initialCapacity) {
        final int half = initialCapacity >>> 1;
        lesser = new MaxHeap<>(initialCapacity - half);
        greater = new MinHeap<>(half);
    }
    
    public MedianHeap() {
        this(20);
    }
    
    @Override
    public int size() {
        return lesser.size() + greater.size();
    }
    
    @Override
    public void clear() {
        lesser.clear();
        greater.clear();
    }
    
    private int cmp() {
        return lesser.size() - greater.size();
    }
    
    private E internalMedian() {
        if (cmp() > 0) {
            return lesser.peek();
        } else {
            return greater.peek();
        }
    }
    
    public E median() {
        return internalMedian();
    }
    
    @Override
    public E peek() {
        return internalMedian();
    }
    
    @Override
    public boolean add(final E e) {
        // insert first two
        if (lesser.isEmpty()) {
            lesser.add(e);
            return true;
        }
        if (greater.isEmpty()) {
            greater.add(e);
            return true;
        }
        
        // insert if both non empty and rebalance
        final int cmp = cmp();
        if (e.compareTo(internalMedian()) < 0) {
            if (cmp > 0) {
                greater.add(lesser.replace(e));
            } else {
                lesser.add(e);
            }
        } else {
            if (cmp < 0) {
                lesser.add(greater.replace(e));
            } else {
                greater.add(e);
            }
        }
        return true;
    }
    
    @Override
    public boolean offer(final E e) {
        return add(e);
    }
    
    @Override
    public E remove() {
        if (cmp() > 0) {
            return lesser.remove();
        } else {
            return greater.remove();
        }
    }
    
    @Override
    public E poll() {
        if (cmp() > 0) {
            return lesser.poll();
        } else {
            return greater.poll();
        }
    }
    
    @Override
    public boolean contains(final Object o) {
        return lesser.contains(o) && greater.contains(o);
    }
    
    @Override
    public boolean remove(final Object o) {
        return lesser.remove(o) || greater.remove(o);
    }
    
    @Override
    public Object[] toArray() {
        final Object[] a = new Object[size()];
        lesser.toArray(a);
        greater.toArray(a);
        return a;
    }
    
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size()) {
            a = Arrays.copyOf(a, size());
        }
        lesser.toArray(a);
        greater.toArray(a);
        return a;
    }
    
    private MedianHeap(final MedianHeap<E> clone) {
        lesser = clone.lesser;
        greater = clone.greater;
    }
    
    @Override
    public MedianHeap<E> clone() {
        return new MedianHeap<>(this);
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        lesser.toStringBuilder(sb);
        if (!greater.isEmpty()) {
            sb.append(',');
            sb.append(' ');
            greater.toStringBuilder(sb);
        }
        sb.append(']');
        return sb.toString();
    }
    
    public String toStringMinMaxHeaps() {
        return lesser.toString() + ", " + greater.toString();
    }
    
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            
            private final Iterator<E> lesserIter = lesser.iterator();
            private final Iterator<E> greaterIter = greater.iterator();
            
            @Override
            public boolean hasNext() {
                return lesserIter.hasNext() && greaterIter.hasNext();
            }
            
            @Override
            public E next() {
                if (lesserIter.hasNext()) {
                    return lesserIter.next();
                } else if (greaterIter.hasNext()) {
                    return greaterIter.next();
                }
                throw new NoSuchElementException();
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
            
        };
    }
    
    public static <E extends Comparable<? super E>> E median(final E[] a) {
        final MedianHeap<E> medians = new MedianHeap<>(a.length);
        for (final E e : a) {
            medians.add(e);
        }
        return medians.median();
    }
    
    public static void main(final String[] args) {
        final MedianHeap<Integer> medians = new MedianHeap<>();
        final Random random = new Random(2);
        for (int i = 0; i < 21; i++) {
            final int n = random.nextInt(20);
            System.out.println(n);
            medians.add(n);
        }
        System.out.println(medians.toStringMinMaxHeaps());
        System.out.println("median: " + medians.median());
    }
    
}
