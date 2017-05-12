import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class PriorityQueue<E extends Comparable<? super E>> {
    
    private int size;
    private E[] a;
    
    @SuppressWarnings("unchecked")
    public PriorityQueue(final int capacity) {
        a = (E[]) new Comparable[capacity + 1];
    }
    
    public PriorityQueue() {
        this(10);
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void clear() {
        Arrays.fill(a, 1, size + 1, null);
        size = 0;
    }
    
    /**
     * inserts element by first inserting at bottom, then swapping with parent
     * if element < parent
     * 
     * @param child index to insert new element
     * @param e element to insert at i (usually bottom)
     */
    private void siftUp(int child, final E e) {
        if (child == 1) {
            a[1] = e;
            return;
        }
        final E[] a = this.a;
        int parent = child >>> 1;
        // a[i] is parent
        while (parent > 0 && e.compareTo(a[parent]) <= 0) {
            a[child] = a[parent];
            child = parent;
            parent >>>= 1;
        }
        a[child] = e;
    }
    
    /**
     * inserts element by first inserting at root, then swapping with greater
     * child
     * 
     * @param parent index to insert at
     * @param e element to insert at index i (usually root)
     */
    private void siftDown(int parent, final E e) {
        if (size == 1) {
            return;
        }
        final E[] a = this.a;
        for (int child = parent << 1; child <= size; a[parent] = a[child], parent = child, child <<= 1) {
            if (child < size && a[child].compareTo(a[child + 1]) > 0) {
                child++;
            }
            if (e.compareTo(a[child]) <= 0) {
                break;
            }
        }
        a[parent] = e;
    }
    
    public void add(final E e) {
        if (a.length == ++size) {
            a = Arrays.copyOf(a, size << 1);
        }
        siftUp(size, e);
    }
    
    public E remove() {
        final E removed = a[1];
        siftDown(1, a[size]);
        a[size--] = null;
        return removed;
    }
    
    public E get(final int index) {
        return a[index + 1];
    }
    
    void toStringBuilder(final StringBuilder sb) {
        final E[] a = this.a;
        for (int i = 1;; i++) {
            sb.append(a[i].toString());
            if (i == size) {
                return;
            }
            sb.append(',');
            sb.append(' ');
        }
    }
    
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        toStringBuilder(sb);
        sb.append(']');
        return sb.toString();
    }
    
}
