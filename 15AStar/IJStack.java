import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public final class IJStack implements Frontier {
    
    private int size;
    private IJ[] a;
    
    public IJStack() {
        this(10);
    }
    
    public IJStack(final int capacity) {
        size = capacity;
        a = new IJ[capacity];
    }
    
    @Override
    public final int size() {
        return size;
    }
    
    @Override
    public final boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public void clear() {
        size = 0;
    }
    
    private void push(final IJ ij) {
        if (a.length == size) {
            a = Arrays.copyOf(a, size << 1);
        }
        a[size++] = ij;
    }
    
    @Override
    public void add(final IJ ij, final int distance) {
        push(ij);
    }
    
    @Override
    public IJ remove() {
        final IJ removed = a[--size];
        a[size] = null;
        return removed;
    }
    
    @Override
    public final String toString() {
        if (size == 0) {
            return "[]";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0;; i++) {
            sb.append(a[i].toString());
            if (i == size) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(',');
            sb.append(' ');
        }
    }
    
}
