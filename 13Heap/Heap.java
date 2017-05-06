import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class Heap<E extends Comparable<? super E>> extends AbstractQueue<E> implements Cloneable {
    
    /**
     * 
     * 
     * @author Khyber Sen
     */
    public enum Order {
        MIN,
        MAX
    };
    
    private final int min;
    
    private int size;
    private E[] elements;
    
    @SuppressWarnings("unchecked")
    public Heap(final int initialCapacity, final Order order) {
        this.min = order == Order.MIN ? 1 : -1;
        elements = (E[]) new Comparable[initialCapacity + 1];
    }
    
    public Heap(final Order order) {
        this(10, order);
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public void clear() {
        Arrays.fill(elements, 1, size + 1, null);
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
            elements[1] = e;
            return;
        }
        final E[] a = elements;
        int parent = child >>> 1;
        // a[i] is parent
        while (parent > 0 && e.compareTo(a[parent]) * min <= 0) {
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
        final E[] a = elements;
        for (int child = parent << 1; child <= size; a[parent] = a[child], parent = child, child <<= 1) {
            if (child < size && a[child].compareTo(a[child + 1]) * min > 0) {
                child++;
            }
            if (e.compareTo(a[child]) * min <= 0) {
                break;
            }
        }
        a[parent] = e;
    }
    
    @Override
    public E peek() {
        return elements[1];
    }
    
    @Override
    public boolean add(final E e) {
        Objects.requireNonNull(e);
        if (elements.length == ++size) {
            elements = Arrays.copyOf(elements, size << 1);
        }
        siftUp(size, e);
        return true;
    }
    
    @Override
    public boolean offer(final E e) {
        return add(e);
    }
    
    public E removeNotEmpty() {
        final E removed = elements[1];
        siftDown(1, elements[size]);
        elements[size--] = null;
        return removed;
    }
    
    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return removeNotEmpty();
    }
    
    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        return removeNotEmpty();
    }
    
    private int indexOf(final Object o) {
        final E[] a = this.elements;
        for (int i = 1; i <= size; i++) {
            if (o.equals(a[i])) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean contains(final Object o) {
        return indexOf(o) != -1;
    }
    
    private void removeAt(final int i) {
        // TODO
    }
    
    @Override
    public boolean remove(final Object o) {
        final int i = indexOf(o);
        if (i == -1) {
            return false;
        }
        removeAt(i);
        return true;
    }
    
    @Override
    public Object[] toArray() {
        return Arrays.copyOfRange(elements, 1, size + 1);
    }
    
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = Arrays.copyOf(a, size);
        }
        System.arraycopy(elements, 1, a, 0, size);
        return a;
    }
    
    private Heap(final Heap<E> clone) {
        min = clone.min;
        size = clone.size;
        elements = Arrays.copyOf(clone.elements, size);
    }
    
    @Override
    public Heap<E> clone() {
        return new Heap<>(this);
    }
    
    void toStringBuilder(final StringBuilder sb) {
        final E[] a = elements;
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
    
    public String toTreeString() {
        if (size == 0) {
            return "[]";
        }
        final StringBuilder sb = new StringBuilder();
        final E[] a = elements;
        for (int from = 1, to = 2;; from = to, to <<= 1) {
            for (int i = from; i < to; i++) {
                if (i > size) {
                    sb.append("null");
                } else {
                    sb.append(String.valueOf(a[i]));
                }
                if (i + 1 == to) {
                    sb.append('\n');
                    break;
                }
                sb.append(',');
                sb.append(' ');
            }
            if (to > size) {
                break;
            }
        }
        return sb.toString();
    }
    
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            
            int i = 1;
            
            @Override
            public boolean hasNext() {
                return i <= size;
            }
            
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements[i++];
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
            
        };
    }
    
    public static <T extends Comparable<? super T>> void heapSort(final T[] a) {
        final Heap<T> heap = new Heap<>(a.length, Order.MIN);
        for (final T e : a) {
            heap.add(e);
        }
        for (int i = 0; i < a.length; i++) {
            a[i] = heap.remove();
        }
    }
    
    public static <T extends Comparable<? super T>> boolean isSorted(final T[] a) {
        T prev = a[0];
        for (int i = 1; i < a.length; i++) {
            final T cur = a[i];
            if (prev.compareTo(cur) > 0) {
                return false;
            }
            prev = cur;
        }
        return true;
    }
    
}
