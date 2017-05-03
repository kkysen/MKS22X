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
public class Heap<E extends Comparable<? super E>> extends AbstractQueue<E> {
    
    private final int min;
    
    private int size;
    private E[] elements;
    
    @SuppressWarnings("unchecked")
    public Heap(final boolean max) {
        min = max ? -1 : 1;
        elements = (E[]) new Object[11];
    }
    
    public Heap() {
        this(true);
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
     * @param e element to insert at bottom
     */
    private void siftUp(final E e) {
        final E[] a = elements;
        int j = ++size;
        if (j == 1) {
            a[1] = e;
            return;
        }
        int i = j >>> 1;
        // a[i] is parent
        while (a[i].compareTo(e) * min > 0) {
            a[j] = a[i];
            j = i;
            i >>>= 1;
            if (i == 1) {
                break;
            }
        }
        a[i] = e;
    }
    
    /**
     * inserts element by first inserting at root, then swapping with left child
     * if leftChild > element
     * if leftChild < element, try rightChild
     * 
     * @param e element to insert at root
     */
    private void siftDown(final E e) {
        final E[] a = elements;
        final int i = 1;
        final int j = i << 1;
        size++;
        while (a[j].compareTo(e) * min > 0) {
            // TODO
        }
    }
    
    @Override
    public E peek() {
        return elements[1];
    }
    
    @Override
    public boolean add(final E e) {
        Objects.requireNonNull(e);
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, size << 1);
        }
        siftUp(e);
        return true;
    }
    
    @Override
    public boolean offer(final E e) {
        return add(e);
    }
    
    public E removeNotEmpty() {
        final E removed = elements[1];
        siftDown(elements[size]);
        elements[size] = null;
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
    protected Heap<E> clone() throws CloneNotSupportedException {
        return new Heap<>(this);
    }
    
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        final E[] a = elements;
        for (int i = 1;; i++) {
            sb.append(a[i].toString());
            if (i == size) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(',');
            sb.append(' ');
        }
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
    
}
