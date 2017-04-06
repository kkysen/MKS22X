import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class MyArrayDeque<E> extends AbstractCollection<E> implements Deque<E>, List<E>, Cloneable {
    
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
            capacity = size;
            capacity |= capacity >>> 1;
            capacity |= capacity >>> 2;
            capacity |= capacity >>> 4;
            capacity |= capacity >>> 8;
            capacity |= capacity >>> 16;
            capacity++;
            
            if (capacity < 0) {
                capacity >>>= 1;// Good luck allocating 2 ^ 30 elements
            }
        }
        elements = new Object[capacity];
        mask = capacity - 1;
    }
    
    private final void doubleCapacity() {
        final int oldCapacity = elements.length;
        final int rightLength = oldCapacity - first; // size of right half of array
        final int newCapacity = oldCapacity << 1;
        if (newCapacity < 0) {
            throw new OutOfMemoryError("array size too big");
        }
        final Object[] a = new Object[newCapacity];
        System.arraycopy(elements, first, a, 0, rightLength);
        System.arraycopy(elements, 0, a, rightLength, first);
        elements = a;
        first = 0;
        last = oldCapacity;
        mask = newCapacity - 1;
    }
    
    private final void tryDoubleCapacity() {
        if (first == last) {
            doubleCapacity();
        }
    }
    
    public MyArrayDeque() {
        elements = new Object[DEFAULT_CAPACITY];
        mask = DEFAULT_CAPACITY - 1;
    }
    
    public MyArrayDeque(final int size) {
        allocateElements(size);
    }
    
    public MyArrayDeque(final Collection<? extends E> c) {
        this(c.size());
        addAll(c);
    }
    
    /**
     * @see java.util.Deque#size()
     */
    @Override
    public int size() {
        return last - first & mask;
    }
    
    /**
     * @see java.util.Collection#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return first == last;
    }
    
    private final <T> T[] copyElements(final T[] a) {
        if (first < last) {
            System.arraycopy(elements, first, a, 0, size());
        } else if (first > last) {
            final int rightLength = elements.length - first;
            System.arraycopy(elements, first, a, 0, rightLength);
            System.arraycopy(elements, 0, a, rightLength, last);
        }
        return a;
    }
    
    /**
     * @see java.util.Collection#toArray()
     */
    @Override
    public Object[] toArray() {
        return copyElements(new Object[size()]);
    }
    
    /**
     * @see java.util.Collection#toArray(java.lang.Object[])
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
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(final Collection<?> c) {
        for (final Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }
    
    private final boolean addAllUnchecked(final int index, final Object[] a) {
        
        return false;
    }
    
    public <T extends E> boolean addAll(final int index, final T[] a) {
        return addAllUnchecked(index, a);
    }
    
    public <T extends E> boolean addAll(final T[] a) {
        return addAll(size(), a);
    }
    
    /**
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        return addAll(size(), c);
    }
    
    /**
     * @see java.util.Collection#removeAll(java.util.Collection)
     */
    @Override
    public boolean removeAll(final Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    @Override
    public boolean retainAll(final Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.Collection#clear()
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
    
    private final void checkEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }
    
    /**
     * @see java.util.Deque#removeFirst()
     */
    @Override
    public E removeFirst() {
        checkEmpty();
        @SuppressWarnings("unchecked")
        final E removed = (E) elements[first];
        elements[first] = null;
        first = first + 1 & mask;
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
     * @see java.util.Deque#add(java.lang.Object)
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
     * @see java.util.Deque#remove()
     */
    @Override
    public E remove() {
        return removeFirst();
    }
    
    /**
     * @see java.util.Deque#poll()
     */
    @Override
    public E poll() {
        return pollFirst();
    }
    
    /**
     * @see java.util.Deque#element()
     */
    @Override
    public E element() {
        return getFirst();
    }
    
    /**
     * @see java.util.Deque#peek()
     */
    @Override
    public E peek() {
        return peekFirst();
    }
    
    /**
     * @see java.util.Deque#push(java.lang.Object)
     */
    @Override
    public void push(final E e) {
        addFirst(e);
    }
    
    /**
     * @see java.util.Deque#pop()
     */
    @Override
    public E pop() {
        return removeFirst();
    }
    
    /**
     * @see java.util.Deque#remove(java.lang.Object)
     */
    @Override
    public boolean remove(final Object o) {
        return removeFirstOccurrence(o);
    }
    
    /**
     * @see java.util.Deque#contains(java.lang.Object)
     */
    @Override
    public boolean contains(final Object o) {
        return indexOf(o) != -1;
    }
    
    /**
     * @see java.util.Deque#iterator()
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            
            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public E next() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public void remove() {
                // TODO Auto-generated method stub
                
            }
            
        };
    }
    
    /**
     * @see java.util.Deque#descendingIterator()
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            
            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public E next() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public void remove() {
                // TODO Auto-generated method stub
                
            }
            
        };
    }
    
    private static final void checkIndexForSize(final int index, final int size) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
        }
    }
    
    private final void checkIndex(final int index) {
        checkIndexForSize(index, size());
    }
    
    private final void checkIndexForAdd(final int index) {
        checkIndexForSize(index, size() + 1);
    }
    
    /**
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        checkIndexForAdd(index);
        return addAllUnchecked(index, c.toArray());
    }
    
    /**
     * @see java.util.List#get(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public E get(final int index) {
        checkIndex(index);
        return (E) elements[first + index & mask];
    }
    
    /**
     * @see java.util.List#set(int, java.lang.Object)
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
     * @see java.util.List#add(int, java.lang.Object)
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
        final int i = first + index & mask;
        
        if (first < last) {
            System.arraycopy(elements, i, elements, i + 1, last - i);
            elements[i] = element;
            last = last + 1 & mask;
        } else {
            if (i >= first) {
                // TODO
            } else {
                final Object lastElem = elements[mask];
                System.arraycopy(elements, first, elements, first + 1, mask - first);
                System.arraycopy(elements, i, elements, i + 2, mask - i);
                elements[i + 1] = elements;
                System.arraycopy(elements, 0, elements, 1, i);
                elements[0] = lastElem;
                last++;
            }
        }
        tryDoubleCapacity();
    }
    
    public void removeRange(final int fromIndex, final int toIndex) {
        
    }
    
    /**
     * @see java.util.List#remove(int)
     */
    @Override
    public E remove(final int index) {
        checkIndex(index);
        if (index == 0) {
            return removeFirst();
        } else if (index == size()) {
            return removeLast();
        }
        
        final int i = first + index & mask;
        
        if (first < last) {
            @SuppressWarnings("unchecked")
            final E removed = (E) elements[i];
            System.arraycopy(elements, i, elements, i - 1, last - i);
            last--;
            elements[last] = null;
            return removed;
        }
        
        if (i <= first) {
            // TODO
        } else {
            // TODO
        }
        
        System.arraycopy(elements, i + 1, elements, i, mask - i);
        elements[0] = elements[mask];
        
        final Object lastElem = elements[mask];
        System.arraycopy(elements, first, elements, first + 1, elements.length - first);
        System.arraycopy(elements, i, elements, i + 2, last - i);
        elements[i + 1] = elements;
        System.arraycopy(elements, 0, elements, 1, i);
        elements[0] = lastElem;
        last++;
    }
    
    /**
     * @see java.util.List#indexOf(java.lang.Object)
     */
    @Override
    public int indexOf(final Object o) {
        final int last = this.last;
        final Object[] elements = this.elements;
        if (first < last) {
            if (o == null) {
                for (int i = first; i < last; i++) {
                    if (elements[i] == null) {
                        return i - first;
                    }
                }
            } else {
                for (int i = first; i < last; i++) {
                    if (o.equals(elements[i])) {
                        return i - first;
                    }
                }
            }
        } else {
            if (o == null) {
                for (int i = first; i < elements.length; i++) {
                    if (elements[i] == null) {
                        return i - first;
                    }
                }
                for (int i = 0; i < last; i++) {
                    if (elements[i] == null) {
                        return elements.length - first + i;
                    }
                }
            } else {
                for (int i = first; i < elements.length; i++) {
                    if (o.equals(elements[i])) {
                        return i - first;
                    }
                }
                for (int i = 0; i < last; i++) {
                    if (o.equals(elements[i])) {
                        return elements.length - first + i;
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    @Override
    public int lastIndexOf(final Object o) {
        final int first = this.first;
        final Object[] elements = this.elements;
        if (first < last) {
            if (o == null) {
                for (int i = last; i >= first; i--) {
                    if (elements[i] == null) {
                        return i - first;
                    }
                }
            } else {
                for (int i = last; i >= first; i--) {
                    if (o.equals(elements[i])) {
                        return i - first;
                    }
                }
            }
        } else {
            if (o == null) {
                for (int i = last; i >= 0; i--) {
                    if (elements[i] == null) {
                        return elements.length - first + i;
                    }
                }
                for (int i = elements.length - 1; i >= first; i--) {
                    if (elements[i] == null) {
                        return i - first;
                    }
                }
            } else {
                for (int i = last; i >= 0; i--) {
                    if (o.equals(elements[i])) {
                        return elements.length - first + i;
                    }
                }
                for (int i = elements.length - 1; i >= first; i--) {
                    if (o.equals(elements[i])) {
                        return i - first;
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * @see java.util.List#listIterator()
     */
    @Override
    public ListIterator<E> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#listIterator(int)
     */
    @Override
    public ListIterator<E> listIterator(final int index) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#subList(int, int)
     */
    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
