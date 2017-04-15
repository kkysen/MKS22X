import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;

/**
 * A better {@link java.util.ArrayDeque}.
 * It implements the {@link List} interface unlike {@link java.util.ArrayDeque},
 * allows null elements, and contains addAll(E[]) and similar methods for faster
 * array adding.
 * 
 * @see java.util.ArrayDeque
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class RingBuffer<E> extends AbstractList<E>
        implements Deque<E>, RandomAccess, Cloneable, Serializable {
    
    private static final long serialVersionUID = 2852255322378711691L;
    
    private static final int MIN_INITIAL_CAPACITY = 8;
    
    private static final int DEFAULT_CAPACITY = 16;
    
    private Object[] elements;
    
    private int first = 0;
    private int last = 0;
    private int mask; // all 1111s bit mask
    
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
    }
    
    private final <T> T[] copyElements(final T[] a) {
        if (first < last) {
            System.arraycopy(elements, first, a, 0, size());
        } else {
            final int rightLength = elements.length - first;
            System.arraycopy(elements, first, a, 0, rightLength);
            System.arraycopy(elements, 0, a, rightLength, last);
        }
        return a;
    }
    
    private final void reallocateElements(final int newCapacity, final int size) {
        elements = copyElements(new Object[newCapacity]);
        first = 0;
        last = size;
        mask = newCapacity - 1;
    }
    
    private final void doubleCapacity() {
        final int oldCapacity = elements.length;
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
    }
    
    public RingBuffer() {
        initEmpty();
    }
    
    public RingBuffer(final int size) {
        allocateElements(size);
    }
    
    @SafeVarargs
    public RingBuffer(final E... a) {
        addAll(a);
    }
    
    public RingBuffer(final Collection<? extends E> c) {
        this(c.size());
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
    
    private final void checkIndex(final int index) {
        checkIndexForSize(index, size());
    }
    
    private final void checkIndexForAdd(final int index) {
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
        checkIndex(index);
        return (E) elements[first + index & mask];
    }
    
    /**
     * @see java.util.AbstractList#set(int, java.lang.Object)
     */
    @Override
    public E set(final int index, final E element) {
        checkIndex(index);
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
        tryToDoubleCapacity();
    }
    
    /**
     * @see java.util.Deque#addLast(java.lang.Object)
     */
    @Override
    public void addLast(final E e) {
        elements[last] = e;
        last = last + 1 & mask;
        tryToDoubleCapacity();
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
    
    public void enqueue(final E e) {
        add(e);
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
    
    private final void add(final Object[] a, final int size, final int first, final int last,
            final int index, final int i, final E e) {
        // will have to resize anyways,
        // so resize before copying
        final int oldCapacity = a.length;
        final int newCapacity = oldCapacity << 1;
        if (newCapacity < 0) {
            throw new OutOfMemoryError("array size too big");
        }
        final Object[] c = new Object[newCapacity];
        if (first < last) {
            System.arraycopy(a, first, c, 0, index);
            c[index] = e;
            System.arraycopy(a, first + index, c, index + 1, size - index);
        } else {
            if (i >= first) {
                System.arraycopy(a, first, c, 0, index);
                c[index] = e;
                System.arraycopy(a, first + index, c, index + 1, a.length - index);
            } else {
                final int rightLength = a.length - first;
                System.arraycopy(a, first, c, 0, rightLength);
                System.arraycopy(a, 0, c, rightLength, i);
                c[rightLength + i] = e;
                System.arraycopy(a, i, c, rightLength + i + 1, last - i);
            }
        }
        this.first = 0;
        this.last = a.length;
        mask = newCapacity - 1;
        elements = c;
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
        
        checkIndexForAdd(index);
        
        final Object[] a = elements;
        final int first = this.first;
        final int last = this.last;
        final int i = first + index & mask;
        final int size = size();
        final int newSize = size + 1;
        if (newSize == a.length) {
            add(a, size, first, last, index, i, element);
            return;
        }
        
        if (i >= first) {
            // i is in the third part
            // instead of moving everything to the right and wrapping,
            // move to the left to avoid wrapping
            System.arraycopy(a, first, a, first - 1, i - first); // TODO check length
            this.first = first - 1; // no wrap checking
        } else {
            // else if (first < last || i < first)
            // if first < last, there will always be empty element at end of array
            // if i < first, i is in the first part, do the same thing
            System.arraycopy(a, i, a, i + 1, last - i);
            this.last = last + 1 & mask; // only last++ if i < first, but extra branch slower than bit &
        }
        a[i] = element;
    }
    
    /**
     * @see java.util.Deque#removeFirst()
     */
    @Override
    public E removeFirst() {
        checkEmpty();
        final int first = this.first;
        @SuppressWarnings("unchecked")
        final E removed = (E) elements[first];
        elements[first] = null;
        this.first = first + 1 & mask;
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
    
    public E dequeue() {
        return remove();
    }
    
    /**
     * @see java.util.Deque#pop()
     */
    @Override
    public E pop() {
        return removeFirst();
    }
    
    /**
     * @param i index in underlying array to delete
     * @return 0 if no array copying
     *         1 if array to the left was moved forward by 1
     *         -1 if array to the right was moved backward by 1
     */
    private final int delete(final int i) {
        if (i == 0) {
            removeFirst();
            return 0;
        }
        if (i == size()) {
            removeLast();
            return 0;
        }
        
        final Object[] a = elements;
        
        if (i >= first) {
            // i is in third part
            final int first = this.first;
            System.arraycopy(a, first, a, first + 1, i - first);
            a[first] = null;
            this.first = first + 1;
            return 1;
        }
        
        // else if (first < last || i < first)
        // different cases, but do the same thing (see add(int, E) comments)
        final int last = this.last;
        System.arraycopy(a, i + 1, a, i, last - i);
        a[this.last = last - 1 & mask] = null;
        return -1;
    }
    
    /**
     * @see java.util.AbstractList#remove(int)
     */
    @Override
    public E remove(final int index) {
        checkIndex(index);
        final int i = first + index & mask;
        @SuppressWarnings("unchecked")
        final E removed = (E) elements[i];
        delete(i);
        return removed;
    }
    
    /**
     * @see java.util.AbstractList#removeRange(int, int)
     */
    @Override
    public void removeRange(final int fromIndex, final int toIndex) {
        final int removeLen = toIndex - fromIndex;
        if (removeLen < 0) {
            throw new IllegalArgumentException("fromIndex: " + fromIndex
                    + " must be less than or equal to toIndex: " + toIndex);
        }
        checkIndex(fromIndex);
        checkIndexForAdd(toIndex);
        
        if (removeLen == 0) {
            return;
        }
        
        final int first = this.first;
        final int i = first + fromIndex & mask;
        if (removeLen == 1) {
            delete(i);
            return;
        }
        
        final Object[] a = elements;
        
        if (i >= first) {
            final int j = i + removeLen;
            if (j < a.length) {
                System.arraycopy(a, first, a, first + removeLen, i - first);
                Arrays.fill(a, first, first + removeLen, null);
                this.first = j;
            } else if (j == a.length) {
                Arrays.fill(a, first, a.length, null);
                this.first = 0;
            } else {
                // j > a.length, must wrap around, removing from both ends
                // TODO
                this.first = j & mask;
            }
        } else {
            final int last = this.last;
            System.arraycopy(a, i + removeLen, a, i, last - i);
            Arrays.fill(a, i, i + removeLen, null);
            this.last = last - removeLen & mask;
        }
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
        delete(i);
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
        delete(i);
        return true;
    }
    
    /**
     * @see java.util.AbstractCollection#remove(java.lang.Object)
     */
    @Override
    public boolean remove(final Object o) {
        return removeFirstOccurrence(o);
    }
    
    private static final int indexOf(final Object[] a, final Object o, final int fromIndex,
            final int toIndex) {
        if (o == null) {
            for (int i = fromIndex; i < toIndex; i++) {
                if (a[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = fromIndex; i < toIndex; i++) {
                if (o.equals(a[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * @see java.util.AbstractList#indexOf(java.lang.Object)
     */
    @Override
    public int indexOf(final Object o) {
        final Object[] a = elements;
        final int first = this.first;
        final int last = this.last;
        if (first < last) {
            final int i = indexOf(a, o, first, last);
            if (i != -1) {
                return i - first;
            }
        } else {
            int i = indexOf(a, o, first, a.length);
            if (i != -1) {
                return i - first;
            }
            i = indexOf(a, o, 0, last);
            if (i != -1) {
                return a.length - first + i;
            }
        }
        return -1;
    }
    
    private static final int lastIndexOf(final Object[] a, final Object o, final int fromIndex,
            final int toIndex) {
        if (o == null) {
            for (int i = fromIndex - 1; i >= toIndex; i--) {
                if (a[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = fromIndex - 1; i >= toIndex; i--) {
                if (o.equals(a[i])) {
                    return i;
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
        final Object[] a = elements;
        final int first = this.first;
        if (first < last) {
            final int i = lastIndexOf(a, o, last, first);
            if (i != -1) {
                return i - first;
            }
        } else {
            int i = lastIndexOf(a, o, last, 0);
            if (i != -1) {
                return a.length - first + i;
            }
            i = lastIndexOf(a, o, a.length, first);
            if (i != -1) {
                return i - first;
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
    
    private final void addAll(final Object[] a, final int size, final int first, final int last,
            final int index, final int i, final Object[] b, final int bLen) {
        // could have just doubled capacity and run same algo as above,
        // but that copies stuff twice unnecessarily,
        // so write own reallocation here
        final int newSize = size + bLen;
        final int oldCapacity = a.length;
        int newCapacity = oldCapacity << 1;
        while (newCapacity < newSize) {
            newCapacity <<= 1;
        }
        if (newCapacity < 0) {
            throw new OutOfMemoryError("array size too big");
        }
        final Object[] c = new Object[newCapacity];
        
        if (first < last) {
            System.arraycopy(a, first, c, 0, index);
            System.arraycopy(b, 0, c, index, b.length);
            System.arraycopy(a, first + index, c, index + bLen, size - index);
        } else {
            if (i >= first) {
                System.arraycopy(a, first, c, 0, index);
                System.arraycopy(b, 0, c, index, b.length);
                System.arraycopy(a, first + index, c, index + bLen, a.length - index);
            } else {
                final int rightLength = a.length - first;
                System.arraycopy(a, first, c, 0, rightLength);
                System.arraycopy(a, 0, c, rightLength, i);
                System.arraycopy(b, 0, c, rightLength + i, b.length);
                System.arraycopy(a, i, c, rightLength + i + bLen, last - i);
            }
        }
        this.first = 0;
        this.last = newSize;
        mask = newCapacity - 1;
        elements = c;
    }
    
    @SuppressWarnings("unchecked")
    private final boolean addAllUnchecked(final int index, final Object[] b) {
        Objects.requireNonNull(b);
        final int bLen = b.length;
        if (bLen == 0) {
            return false;
        }
        if (bLen == 1) {
            add(index, (E) b[0]);
            return true;
        }
        
        checkIndexForAdd(index);
        
        final Object[] a = elements;
        final int size = size();
        final int space = a.length - size; // space left
        
        final int first = this.first;
        final int last = this.last;
        final int i = first + index & mask;
        
        if (bLen > space) {
            addAll(a, size, first, last, index, i, b, bLen);
            return false;
        }
        
        if (first < last) {
            final int j = i + bLen;
            int newLast = last + bLen;
            if (newLast <= a.length) {
                System.arraycopy(a, i, a, i + bLen, last - i);
            } else {
                // wrapping required
                newLast &= mask;
                System.arraycopy(a, i, a, i + bLen - newLast, a.length - last);
                System.arraycopy(a, 0, a, newLast, newLast);
                
                return true;
            }
            this.last = newLast & mask;
        } else if (i >= first) {
            // i is in the third part
            // instead of moving everything to the right and wrapping,
            // move to the left to avoid wrapping
            System.arraycopy(a, first, a, first - bLen, i - first); // TODO check length
            this.first = first - bLen; // no wrap checking
        } else { // i < first
            // i is in the first part
            // there will always be room, no wrapping needed
            System.arraycopy(a, i, a, i + bLen, last - i);
            this.last = last + bLen;
        }
        System.arraycopy(b, 0, a, i, b.length);
        // tryToDoubleCapacity(); won't happen, if needed, below case will run
        return true;
    }
    
    @SafeVarargs
    public final boolean addAll(final int index, final E... a) {
        return addAllUnchecked(index, a);
    }
    
    @SafeVarargs
    public final boolean addAll(final E... a) {
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
        checkIndexForAdd(index);
        return addAllUnchecked(index, c.toArray());
    }
    
    /**
     * undefined behavior if c.contains() throws an exception
     * 
     * @param c collections whose elements are to be removed or retained
     * @param notRemove if elements should be removed if contained in c
     * @return true if any elements removed
     */
    private final boolean batchRemove(final Collection<?> c, final boolean notRemove) {
        Objects.requireNonNull(c);
        
        final Object[] a = elements;
        final int last = this.last;
        final int mask = this.mask;
        
        int i = first;
        int j = i;
        for (; i != last; i = i + 1 & mask) {
            if (c.contains(a[i]) && notRemove) {
                a[j] = a[i];
                j = j + 1 & mask;
            }
        }
        
        if (j == i) {
            return false;
        }
        
        if (j < i) {
            Arrays.fill(a, j, i, null);
        } else {
            Arrays.fill(a, j, a.length, null);
            Arrays.fill(a, 0, i, null);
        }
        
        this.last = j;
        return true;
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
    
    private RingBuffer(final RingBuffer<E> clone) {
        final Object[] a = clone.elements;
        elements = Arrays.copyOf(a, a.length);
        first = clone.first;
        last = clone.last;
        mask = clone.mask;
        modCount = clone.modCount;
    }
    
    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public RingBuffer<E> clone() {
        return new RingBuffer<>(this);
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
                if (delete(prev) == -1) {
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
                if (delete(prev) == 1) {
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
    
    private static final int hashCode(final Object[] a, final int fromIndex, final int toIndex,
            int result) {
        for (int i = fromIndex; i < toIndex; i++) {
            final Object e = a[i];
            result = 31 * result + (e == null ? 0 : e.hashCode());
        }
        return result;
    }
    
    /**
     * @see java.util.AbstractList#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 1;
        final Object[] a = elements;
        final int first = this.first;
        final int last = this.last;
        if (first < last) {
            result = hashCode(a, first, last, result);
        } else if (first > last) {
            result = hashCode(a, first, a.length, result);
            result = hashCode(a, 0, last, result);
        }
        return result;
    }
    
    private static final boolean equals(final Object[] a, final RingBuffer<?> other,
            final int first, final int last, final int mask) {
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
        return true;
    }
    
    public boolean equals(final RingBuffer<?> other) {
        return equals(elements, other, first, last, mask);
    }
    
    private static final boolean equals(final Object[] a, final Iterator<?> iter,
            final int fromIndex, final int toIndex) {
        for (int i = fromIndex; i < toIndex; i++) {
            if (!a[i].equals(iter.next())) {
                return false;
            }
        }
        return true;
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
        
        if (obj instanceof RingBuffer && equals(a, (RingBuffer<?>) obj, first, last, mask)) {
            return true;
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
            return equals(a, iter, first, last);
        } else {
            if (!equals(a, iter, first, a.length)) {
                return false;
            }
            return equals(a, iter, 0, last);
        }
    }
    
    private static final void toString(final StringBuilder sb, final Object[] a,
            final int fromIndex, final int toIndex) {
        for (int i = fromIndex; i < toIndex; i++) {
            sb.append(String.valueOf(a[i]));
            sb.append(',');
            sb.append(' ');
        }
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
            toString(sb, a, first, last);
        } else {
            toString(sb, a, first, a.length);
            toString(sb, a, 0, last);
        }
        sb.setCharAt(sb.length() - 2, ']');
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    private static final void writeObject(final Object[] a, final ObjectOutputStream s,
            final int fromIndex, final int toIndex) throws IOException {
        for (int i = fromIndex; i < toIndex; i++) {
            s.writeObject(a[i]);
        }
    }
    
    private final void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size());
        final Object[] a = elements;
        final int first = this.first;
        final int last = this.last;
        if (first < last) {
            writeObject(a, s, first, last);
        } else if (first > last) {
            writeObject(a, s, first, a.length);
            writeObject(a, s, 0, last);
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
    
    public void sort() {
        sort(null);
    }
    
    @SuppressWarnings("unchecked")
    public void sort(final Comparator<? super E> comparator) {
        if (first < last) {
            Arrays.sort((E[]) elements, first, last, comparator);
        } else {
            final Object[] a = toArray();
            Arrays.sort((E[]) a, comparator);
            System.arraycopy(a, 0, elements, 0, a.length);
            first = 0;
            last = a.length;
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
        final RingBuffer<Integer> ring = new RingBuffer<>(50);
        ring.add(5);
        ring.add(10);
        ring.addFirst(0);
        ring.addFirst(Integer.MAX_VALUE);
        
        final Random random = new Random(12345678);
        for (int i = 0; i < 10; i++) {
            if (random.nextBoolean()) {
                ring.addLast(i);
                //ring.addLast(random.nextInt(100));
            } else {
                ring.addFirst(i);
                //ring.addFirst(random.nextInt(100));
            }
        }
        
        System.out.println(ring.first);
        System.out.println(ring.contains(null));
        System.out.println(ring.contains(10));
        System.out.println(ring);
        
        for (final int i : ring) {
            System.out.print(i + ", ");
        }
        System.out.println();
        
        ring.remove(3);
        System.out.println("remove(3): " + ring);
        
        ring.removeRange(3, 6);
        System.out.println("removeRange(3, 6): " + ring);
        
        ring.addAll(new Integer[] {5, 6, 7, 8, 9, 10});
        System.out.println("addAll: " + ring);
        
        ring.addAll(5, new Integer[] {null, 1, 2, 3, 4, 5, null});
        System.out.println("addAll(5, ): " + ring);
        System.out.println("size: " + ring.size());
    }
    
}