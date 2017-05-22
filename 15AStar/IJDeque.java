/**
 * 
 * 
 * @author Khyber Sen
 */
public final class IJDeque implements Frontier {
    
    private static final int MIN_INITIAL_CAPACITY = 8;
    
    private static final int DEFAULT_CAPACITY = 16;
    
    private IJ[] a;
    
    private int first = 0;
    private int last = 0;
    private int mask;
    
    private final void allocateElements(final int size) {
        int capacity = MIN_INITIAL_CAPACITY;
        if (size >= capacity) {
            final int leadingBitIndex = Integer.SIZE - 1 - Integer.numberOfLeadingZeros(size);
            capacity = 1 << leadingBitIndex;
            if (capacity != size) { // size is a power of 2
                capacity <<= 1;
            }
            if (capacity < 0) {
                capacity >>>= 1;// Good luck allocating 2 ^ 30 elements
            }
        }
        a = new IJ[capacity];
        mask = capacity - 1;
    }
    
    private final IJ[] copyElements(final IJ[] dest) {
        final IJ[] src = a;
        if (first < last) {
            System.arraycopy(src, first, dest, 0, size());
        } else {
            final int rightLength = src.length - first;
            System.arraycopy(src, first, dest, 0, rightLength);
            System.arraycopy(src, 0, dest, rightLength, last);
        }
        return dest;
    }
    
    private final void reallocateElements(final int newCapacity, final int size) {
        a = copyElements(new IJ[newCapacity]);
        first = 0;
        last = size;
        mask = newCapacity - 1;
    }
    
    private final void doubleCapacity() {
        final int oldCapacity = a.length;
        final int newCapacity = oldCapacity << 1;
        if (newCapacity < 0) {
            throw new OutOfMemoryError("array size too big");
        }
        reallocateElements(newCapacity, oldCapacity);
    }
    
    private final void tryToDoubleCapacity() {
        if (first == last) {
            doubleCapacity();
        }
    }
    
    public void trimToSize() {
        final int size = size();
        int newCapacity = a.length;
        while (newCapacity > size) {
            newCapacity >>>= 1;
        }
        if (newCapacity != size) {
            newCapacity <<= 1;
        }
        reallocateElements(newCapacity, size);
    }
    
    public IJDeque() {
        a = new IJ[DEFAULT_CAPACITY];
        mask = DEFAULT_CAPACITY - 1;
    }
    
    public IJDeque(final int capacity) {
        allocateElements(capacity);
    }
    
    @Override
    public int size() {
        return last - first & mask;
    }
    
    @Override
    public boolean isEmpty() {
        return first == last;
    }
    
    @Override
    public void clear() {
        first = last = 0;
    }
    
    private void addLast(final IJ ij) {
        a[last] = ij;
        last = last + 1 & mask;
        tryToDoubleCapacity();
    }
    
    @Override
    public void add(final IJ ij) {
        addLast(ij);
    }
    
    @Override
    public IJ remove() {
        final int first = this.first;
        final IJ removed = a[first];
        a[first] = null;
        this.first = first + 1 & mask;
        return removed;
    }
    
    private static final void toString(final StringBuilder sb, final IJ[] a,
            final int fromIndex, final int toIndex) {
        for (int i = fromIndex; i < toIndex; i++) {
            sb.append(a[i]);
            sb.append(',');
            sb.append(' ');
        }
    }
    
    @Override
    public String toString() {
        if (first == last) {
            return "[]";
        }
        final IJ[] a = this.a;
        final int last = this.last;
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (first < last) {
            toString(sb, a, first, last);
        } else {
            toString(sb, a, first, a.length);
            toString(sb, a, 0, last);
        }
        sb.setCharAt(sb.length() - 2, ']');
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
}
