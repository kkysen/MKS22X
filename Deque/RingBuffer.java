import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * A null-containing ArrayDeque like {@link java.util.ArrayDeque} that
 * implements {@link List} and extends {@link AbstractList}.
 * 
 * @see java.util.ArrayDeque
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class RingBuffer<E> extends AbstractList<E>
        implements Deque<E>, RandomAccess, Cloneable, Serializable {
    
    public enum WrappingMode {
        DEQUE,
        STACK,
        REVERSE_STACK,
        QUEUE,
        REVERSE_QUEUE;
    }
    
    private static final long serialVersionUID = 2852255322378711691L;
    
    private static final int MIN_INITIAL_CAPACITY = 8;
    
    private static final int DEFAULT_CAPACITY = 16;
    
    private static final WrappingMode DEFAULT_WRAPPING_MODE = WrappingMode.DEQUE;
    
    private final WrappingMode wrappingMode;
    
    private Object[] elements;
    
    private int first = 0;
    private int last = 0;
    private int mask; // all 1111s bit mask
    
    private final void positionPointersAfterAllocation() {
        switch (wrappingMode) {
            case DEQUE:
                first = elements.length >>> 1;
                break;
            case STACK:
            case QUEUE:
                first = mask;
                break;
            case REVERSE_STACK:
            case REVERSE_QUEUE:
                first = 0;
                break;
            default:
                first = -1; // can't happen
        }
        last = first;
    }
    
    // elements.length is a power of 2
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
        elements = new Object[capacity];
        mask = capacity - 1;
        positionPointersAfterAllocation();
    }
    
    private final <T> T[] copyElements(final T[] a, final int offset) {
        if (first < last) {
            System.arraycopy(elements, first, a, offset, size());
        } else if (first > last) {
            final int rightLength = elements.length - first;
            System.arraycopy(elements, first, a, offset, rightLength);
            System.arraycopy(elements, 0, a, offset + rightLength, last);
        }
        return a;
    }
    
    private final <T> T[] copyElements(final T[] a) {
        return copyElements(a, 0);
    }
    
    private final void reallocateElements(final int newCapacity, final int size,
            final WrappingMode wrappingMode) {
        final int newFirst;
        final int newLast;
        switch (wrappingMode) {
            case DEQUE:
                // center pointers in array to minimize wrapping
                final int mid = newCapacity >>> 1;
                final int half = size >>> 1;
                newFirst = mid - half;
                newLast = newFirst + size;
                break;
            case STACK:
            case QUEUE:
                newLast = newCapacity - 1;
                newFirst = newLast - size;
                break;
            case REVERSE_STACK:
            case REVERSE_QUEUE:
                newFirst = 0;
                newLast = size;
                break;
            default:
                newFirst = newLast = -1; // can't happen
        }
        
        elements = copyElements(new Object[newCapacity], newFirst);
        first = newFirst;
        last = newLast;
        mask = newCapacity - 1;
    }
    
    private final void reallocateElements(final int newCapacity, final int size) {
        reallocateElements(newCapacity, size, wrappingMode);
    }
    
    private final void doubleCapacity() {
        final int oldCapacity = elements.length;
        final int newCapacity = oldCapacity << 1;
        if (newCapacity < 0) {
            throw new OutOfMemoryError("array size too big");
        }
        reallocateElements(newCapacity, oldCapacity);
    }
    
    private final void tryDoubleCapacity() {
        if (first == last) {
            doubleCapacity();
        }
    }
    
    /**
     * trims underlying array to smallest power of 2
     */
    public void trimToSize() {
        final int size = size();
        int newCapacity = elements.length;
        while (newCapacity > size) {
            newCapacity >>>= 1;
        }
        if (newCapacity != size) {
            newCapacity <<= 1;
        }
        reallocateElements(newCapacity, size);
    }
    
    private final void initEmpty() {
        elements = new Object[DEFAULT_CAPACITY];
        mask = DEFAULT_CAPACITY - 1;
        positionPointersAfterAllocation();
    }
    
    public RingBuffer() {
        this(DEFAULT_WRAPPING_MODE);
    }
    
    public RingBuffer(final int size) {
        this(size, DEFAULT_WRAPPING_MODE);
    }
    
    public RingBuffer(final Collection<? extends E> c) {
        this(c, DEFAULT_WRAPPING_MODE);
    }
    
    public RingBuffer(final WrappingMode wrappingMode) {
        this.wrappingMode = wrappingMode;
        initEmpty();
    }
    
    public RingBuffer(final int size, final WrappingMode wrappingMode) {
        this(wrappingMode);
        allocateElements(size);
    }
    
    public RingBuffer(final E[] a, final WrappingMode wrappingMode) {
        this(wrappingMode);
        addAll(a);
    }
    
    public RingBuffer(final Collection<? extends E> c, final WrappingMode wrappingMode) {
        this(c.size(), wrappingMode);
        addAll(c);
    }
    
    /**
     * @see java.util.AbstractCollection#size()
     */
    @Override
    public int size() {
        return last - first & mask;
    }
    
    /**
     * @see java.util.AbstractCollection#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return first == last;
    }
    
    private final void checkEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }
    
    private static final void checkIndexForSize(final int index, final int size) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
    
    private final void rangeCheck(final int index) {
        checkIndexForSize(index, size());
    }
    
    private final void rangeCheckForAdd(final int index) {
        checkIndexForSize(index, size() + 1);
    }
    
    /**
     * @see java.util.Deque#getFirst()
     */
    @SuppressWarnings("unchecked")
    @Override
    public E getFirst() {
        checkEmpty();
        return (E) elements[first];
    }
    
    /**
     * @see java.util.Deque#getLast()
     */
    @SuppressWarnings("unchecked")
    @Override
    public E getLast() {
        checkEmpty();
        return (E) elements[last];
    }
    
    /**
     * @see java.util.Deque#element()
     */
    @Override
    public E element() {
        return getFirst();
    }
    
    /**
     * @see java.util.AbstractList#get(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public E get(final int index) {
        rangeCheck(index);
        return (E) elements[first + index & mask];
    }
    
    /**
     * @see java.util.AbstractList#set(int, java.lang.Object)
     */
    @Override
    public E set(final int index, final E element) {
        rangeCheck(index);
        final int i = first + index & mask;
        @SuppressWarnings("unchecked")
        final E removed = (E) elements[i];
        elements[i] = element;
        return removed;
    }
    
    /**
     * @see java.util.Deque#addFirst(java.lang.Object)
     */
    @Override
    public void addFirst(final E e) {
        elements[first = first - 1 & mask] = e;
        tryDoubleCapacity();
    }
    
    /**
     * @see java.util.Deque#addLast(java.lang.Object)
     */
    @Override
    public void addLast(final E e) {
        elements[last] = e;
        last = last + 1 & mask;
        tryDoubleCapacity();
    }
    
    /**
     * @see java.util.Deque#offerFirst(java.lang.Object)
     */
    @Override
    public boolean offerFirst(final E e) {
        addFirst(e);
        return true;
    }
    
    /**
     * @see java.util.Deque#offerLast(java.lang.Object)
     */
    @Override
    public boolean offerLast(final E e) {
        addLast(e);
        return true;
    }
    
    /**
     * @see java.util.AbstractList#add(java.lang.Object)
     */
    @Override
    public boolean add(final E e) {
        addLast(e);
        return true;
    }
    
    /**
     * @see java.util.Deque#offer(java.lang.Object)
     */
    @Override
    public boolean offer(final E e) {
        return offerLast(e);
    }
    
    /**
     * @see java.util.Deque#push(java.lang.Object)
     */
    @Override
    public void push(final E e) {
        addFirst(e);
    }
    
    /**
     * @see java.util.AbstractList#add(int, java.lang.Object)
     */
    @Override
    public void add(final int index, final E element) {
        if (index == 0) {
            addFirst(element);
            return;
        } else if (index == size()) {
            addLast(element);
            return;
        }
        
        rangeCheckForAdd(index);
        
        final Object[] a = elements;
        final int first = this.first;
        final int mask = this.mask;
        
        final int i = first + index & mask;
        
        if (first < last) {
            System.arraycopy(a, i, a, i + 1, last - i);
            a[i] = element;
            last = last + 1 & mask;
        } else {
            if (i >= first) {
                // TODO
            } else {
                final Object lastElem = a[mask];
                System.arraycopy(a, first, a, first + 1, mask - first);
                System.arraycopy(a, i, a, i + 2, mask - i);
                a[i + 1] = a;
                System.arraycopy(a, 0, a, 1, i);
                a[0] = lastElem;
                last++;
            }
        }
        tryDoubleCapacity();
    }
    
    /**
     * @see java.util.Deque#removeFirst()
     */
    @Override
    public E removeFirst() {
        checkEmpty();
        final int f = first;
        @SuppressWarnings("unchecked")
        final E removed = (E) elements[f];
        elements[f] = null;
        first = f + 1 & mask;
        return removed;
    }
    
    /**
     * @see java.util.Deque#removeLast()
     */
    @SuppressWarnings("unchecked")
    @Override
    public E removeLast() {
        checkEmpty();
        final E removed = (E) elements[last = last - 1 & mask];
        elements[last] = null;
        return removed;
    }
    
    /**
     * @see java.util.Deque#remove()
     */
    @Override
    public E remove() {
        return removeFirst();
    }
    
    /**
     * @see java.util.Deque#pop()
     */
    @Override
    public E pop() {
        return removeFirst();
    }
    
    private final boolean delete(final int i) {
        if (i == 0) {
            removeFirst();
            return false;
        }
        if (i == size()) {
            removeLast();
            return false;
        }
        
        final Object[] a = elements;
        final int first = this.first;
        final int mask = this.mask;
        
        if (first < last) {
            System.arraycopy(a, i, a, i - 1, last - i);
            last--;
            a[last] = null;
        } else if (i <= first) {
            // TODO
        } else {
            // TODO
        }
        
        System.arraycopy(a, i + 1, a, i, mask - i);
        a[0] = a[mask];
        
        final Object lastElem = a[mask];
        System.arraycopy(a, first, a, first + 1, a.length - first);
        System.arraycopy(a, i, a, i + 2, last - i);
        a[i + 1] = a;
        System.arraycopy(a, 0, a, 1, i);
        a[0] = lastElem;
        last++;
        
        return true;
    }
    
    /**
     * @see java.util.AbstractList#remove(int)
     */
    @Override
    public E remove(final int index) {
        rangeCheck(index);
        final int i = first + index & mask;
        @SuppressWarnings("unchecked")
        final E removed = (E) elements[i];
        delete(index);
        return removed;
    }
    
    /**
     * @see java.util.AbstractList#removeRange(int, int)
     */
    @Override
    public void removeRange(final int fromIndex, final int toIndex) {
        // TODO
    }
    
    /**
     * @see java.util.Deque#removeFirstOccurrence(java.lang.Object)
     */
    @Override
    public boolean removeFirstOccurrence(final Object o) {
        final int i = indexOf(o);
        if (i == -1) {
            return false;
        }
        remove(i);
        return true;
    }
    
    /**
     * @see java.util.Deque#removeLastOccurrence(java.lang.Object)
     */
    @Override
    public boolean removeLastOccurrence(final Object o) {
        final int i = lastIndexOf(o);
        if (i == -1) {
            return false;
        }
        remove(i);
        return true;
    }
    
    /**
     * @see java.util.AbstractCollection#remove(java.lang.Object)
     */
    @Override
    public boolean remove(final Object o) {
        return removeFirstOccurrence(o);
    }
    
    /**
     * @see java.util.AbstractList#indexOf(java.lang.Object)
     */
    @Override
    public int indexOf(final Object o) {
        final int first = this.first;
        final int last = this.last;
        final Object[] a = elements;
        if (first < last) {
            if (o == null) {
                for (int i = first; i < last; i++) {
                    if (a[i] == null) {
                        return i - first;
                    }
                }
            } else {
                for (int i = first; i < last; i++) {
                    if (o.equals(a[i])) {
                        return i - first;
                    }
                }
            }
        } else {
            if (o == null) {
                for (int i = first; i < a.length; i++) {
                    if (a[i] == null) {
                        return i - first;
                    }
                }
                for (int i = 0; i < last; i++) {
                    if (a[i] == null) {
                        return a.length - first + i;
                    }
                }
            } else {
                for (int i = first; i < a.length; i++) {
                    if (o.equals(a[i])) {
                        return i - first;
                    }
                }
                for (int i = 0; i < last; i++) {
                    if (o.equals(a[i])) {
                        return a.length - first + i;
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * @see java.util.AbstractList#lastIndexOf(java.lang.Object)
     */
    @Override
    public int lastIndexOf(final Object o) {
        final int first = this.first;
        final Object[] a = elements;
        if (first < last) {
            if (o == null) {
                for (int i = last; i >= first; i--) {
                    if (a[i] == null) {
                        return i - first;
                    }
                }
            } else {
                for (int i = last; i >= first; i--) {
                    if (o.equals(a[i])) {
                        return i - first;
                    }
                }
            }
        } else {
            if (o == null) {
                for (int i = last; i >= 0; i--) {
                    if (a[i] == null) {
                        return a.length - first + i;
                    }
                }
                for (int i = a.length - 1; i >= first; i--) {
                    if (a[i] == null) {
                        return i - first;
                    }
                }
            } else {
                for (int i = last; i >= 0; i--) {
                    if (o.equals(a[i])) {
                        return a.length - first + i;
                    }
                }
                for (int i = a.length - 1; i >= first; i--) {
                    if (o.equals(a[i])) {
                        return i - first;
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * @see java.util.AbstractCollection#contains(java.lang.Object)
     */
    @Override
    public boolean contains(final Object o) {
        return indexOf(o) != -1;
    }
    
    private final boolean addAllUnchecked(final int index, final Object[] a) {
        // TODO
        return false;
    }
    
    public <T extends E> boolean addAll(final int index, final T[] a) {
        return addAllUnchecked(index, a);
    }
    
    public boolean addAll(final E[] a) {
        return addAll(size(), a);
    }
    
    /**
     * @see java.util.AbstractCollection#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        return addAll(size(), c);
    }
    
    /**
     * @see java.util.AbstractList#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        rangeCheckForAdd(index);
        return addAllUnchecked(index, c.toArray());
    }
    
    private final boolean batchRemove(final Collection<?> c, final boolean remove) {
        // TODO
        return false;
    }
    
    /**
     * @see java.util.AbstractCollection#removeAll(java.util.Collection)
     */
    @Override
    public boolean removeAll(final Collection<?> c) {
        return batchRemove(c, true);
    }
    
    /**
     * @see java.util.AbstractCollection#retainAll(java.util.Collection)
     */
    @Override
    public boolean retainAll(final Collection<?> c) {
        return batchRemove(c, false);
    }
    
    /**
     * @see java.util.AbstractList#clear()
     */
    @Override
    public void clear() {
        if (first < last) {
            Arrays.fill(elements, first, last, null);
        } else if (first > last) {
            Arrays.fill(elements, first, elements.length, null);
            Arrays.fill(elements, 0, last, null);
        }
        first = last = 0;
    }
    
    /**
     * @see java.util.AbstractCollection#toArray()
     */
    @Override
    public Object[] toArray() {
        return copyElements(new Object[size()]);
    }
    
    /**
     * @see java.util.AbstractCollection#toArray(java.lang.Object[])
     */
    @Override
    public <T> T[] toArray(T[] a) {
        final int size = size();
        if (a.length < size) {
            a = Arrays.copyOf(a, size);
        }
        return copyElements(a);
    }
    
    /**
     * @see java.util.AbstractList#iterator()
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            
            int i = first;
            int last = RingBuffer.this.last;
            int prev = -1;
            
            @Override
            public boolean hasNext() {
                return i != last;
            }
            
            @Override
            public E next() {
                if (i == last) {
                    throw new NoSuchElementException();
                }
                @SuppressWarnings("unchecked")
                final E next = (E) elements[i];
                if (last != RingBuffer.this.last) {
                    throw new ConcurrentModificationException();
                }
                prev = i;
                i = i + 1 & mask;
                return next;
            }
            
            @Override
            public void remove() {
                if (prev == -1) {
                    throw new IllegalStateException();
                }
                if (delete(prev)) {
                    i = i - 1 & mask;
                    last = RingBuffer.this.last;
                }
                prev = -1;
            }
            
        };
    }
    
    /**
     * @see java.util.Deque#descendingIterator()
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            
            int i = last;
            int first = RingBuffer.this.first;
            int prev = -1;
            
            @Override
            public boolean hasNext() {
                return i != first;
            }
            
            @Override
            public E next() {
                if (i == first) {
                    throw new NoSuchElementException();
                }
                @SuppressWarnings("unchecked")
                final E next = (E) elements[i = i - 1 & mask];
                if (first != RingBuffer.this.first) {
                    throw new ConcurrentModificationException();
                }
                prev = i;
                return next;
            }
            
            @Override
            public void remove() {
                if (prev == -1) {
                    throw new IllegalStateException();
                }
                if (delete(prev)) {
                    i = i + 1 & mask;
                    first = RingBuffer.this.first;
                }
                prev = -1;
            }
            
        };
    }
    
    /**
     * @see java.util.AbstractList#listIterator()
     */
    @Override
    public ListIterator<E> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.AbstractList#listIterator(int)
     */
    @Override
    public ListIterator<E> listIterator(final int index) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.AbstractList#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 1;
        final int prime = 31;
        final Object[] a = elements;
        final int first = this.first;
        final int last = this.last;
        if (first < last) {
            for (int i = first; i < last; i++) {
                final Object e = a[i];
                result = prime * result + (e == null ? 0 : e.hashCode());
            }
        } else if (first > last) {
            for (int i = first; i < a.length; i++) {
                final Object e = a[i];
                result = prime * result + (e == null ? 0 : e.hashCode());
            }
            for (int i = 0; i < a.length; i++) {
                final Object e = a[i];
                result = prime * result + (e == null ? 0 : e.hashCode());
            }
        }
        return result;
    }
    
    /**
     * @see java.util.AbstractList#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        
        final Object[] a = elements;
        final int size = size();
        final int first = this.first;
        final int last = this.last;
        final int mask = this.mask;
        
        final List<?> list = (List<?>) obj;
        if (size != list.size()) {
            return false;
        }
        if (size == 0) {
            return true;
        }
        
        if (obj instanceof RingBuffer) {
            final RingBuffer<?> other = (RingBuffer<?>) obj;
            final Object[] b = other.elements;
            final int offset = first - other.first;
            if (first < last) {
                for (int i = first; i < last; i++) {
                    if (!a[i].equals(b[i + offset])) {
                        return false;
                    }
                }
            } else {
                for (int i = first; i < a.length; i++) {
                    if (!a[i].equals(b[i + offset & mask])) {
                        return false;
                    }
                }
                for (int i = 0; i < last; i++) {
                    if (!a[i].equals(b[i + offset & mask])) {
                        return false;
                    }
                }
            }
        }
        
        if (list instanceof RandomAccess) {
            for (int i = 0; i < size; i++) {
                if (!a[i + first & mask].equals(list.get(i))) {
                    return false;
                }
            }
            return true;
        }
        
        final Iterator<?> iter = list.iterator();
        if (first < last) {
            for (int i = first; i < last; i++) {
                if (!a[i].equals(iter.next())) {
                    return false;
                }
            }
        } else {
            for (int i = first; i < a.length; i++) {
                if (!a[i].equals(iter.next())) {
                    return false;
                }
            }
            for (int i = 0; i < last; i++) {
                if (!a[i].equals(iter.next())) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * @see java.util.AbstractCollection#toString()
     */
    @Override
    public String toString() {
        if (first == last) {
            return "[]";
        }
        final Object[] a = elements;
        final int last = this.last;
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (first < last) {
            for (int i = first; i < last; i++) {
                sb.append(a[i].toString());
                sb.append(',');
                sb.append(' ');
            }
        } else {
            for (int i = first; i < a.length; i++) {
                sb.append(a[i].toString());
                sb.append(',');
                sb.append(' ');
            }
            for (int i = 0; i < last; i++) {
                sb.append(a[i].toString());
                sb.append(',');
                sb.append(' ');
            }
        }
        sb.setCharAt(sb.length() - 2, ']');
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    private final void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size());
        final Object[] a = elements;
        final int first = this.first;
        final int last = this.last;
        if (first < last) {
            for (int i = first; i < last; i++) {
                s.writeObject(a[i]);
            }
        } else if (first > last) {
            for (int i = first; i < a.length; i++) {
                s.writeObject(a[i]);
            }
            for (int i = 0; i < a.length; i++) {
                s.writeObject(a[i]);
            }
        }
    }
    
    private final void readObject(final ObjectInputStream s)
            throws ClassNotFoundException, IOException {
        s.defaultReadObject();
        final int size = s.readInt();
        allocateElements(size);
        final Object[] a = elements;
        first = 0;
        last = size;
        for (int i = 0; i < a.length; i++) {
            a[i] = s.readObject();
        }
    }
    
    /**
     * @see java.util.Deque#pollFirst()
     */
    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * @see java.util.Deque#pollLast()
     */
    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * @see java.util.Deque#peekFirst()
     */
    @Override
    public E peekFirst() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * @see java.util.Deque#peekLast()
     */
    @Override
    public E peekLast() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * @see java.util.Deque#poll()
     */
    @Override
    public E poll() {
        return pollFirst();
    }
    
    /**
     * @see java.util.Deque#peek()
     */
    @Override
    public E peek() {
        return peekFirst();
    }
    
    public static void main(final String[] args) {
        final RingBuffer<Integer> ring = new RingBuffer<>(1000, WrappingMode.STACK);
        ring.add(5);
        ring.add(10);
        ring.addFirst(0);
        ring.addFirst(Integer.MAX_VALUE);
        System.out.println(ring.first);
        System.out.println(ring.contains(null));
        System.out.println(ring.contains(10));
        System.out.println(ring);
    }
    
}
