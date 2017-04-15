import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Random;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class RingBufferTester<E> extends AbstractList<E> implements Deque<E> {
    
    private final RingBuffer<E> ring;
    private final ArrayList<E> list;
    
    public void assertEqual() {
        assert ring.equals(list);
    }
    
    private static final void assertEqual(final Object a, final Object b) {
        assert a == null ? a == b : a.equals(b);
    }
    
    private static final void assertArraysEqual(final Object[] a1, final Object[] a2) {
        assert Arrays.equals(a1, a2);
    }
    
    private static final <E> void assertIteratorsEqual(final Iterator<? extends E> iter1,
            final Iterator<? extends E> iter2) {
        while (true) {
            final boolean b1 = iter1.hasNext();
            final boolean b2 = iter2.hasNext();
            assert b1 == b2;
            if (!b1 && !b2) {
                break;
            }
            assertEqual(iter1.next(), iter2.next());
        }
    }
    
    private static final <E> void assertRemovingIteratorsEqual(final Iterator<? extends E> iter1,
            final Iterator<? extends E> iter2) {
        final Random random = new Random();
        boolean first = true;
        while (true) {
            if (random.nextBoolean() && !first) {
                iter1.remove();
                iter2.remove();
            }
            final boolean b1 = iter1.hasNext();
            final boolean b2 = iter2.hasNext();
            first = false;
            assert b1 == b2;
            if (!b1 && !b2) {
                break;
            }
            assertEqual(iter1.next(), iter2.next());
        }
    }
    
    private static final <E> void assertListIteratorNextEqual(
            final ListIterator<? extends E> iter1,
            final ListIterator<? extends E> iter2) {
        final boolean b1 = iter1.hasNext();
        final boolean b2 = iter2.hasNext();
        assert b1 == b2;
        if (!b1 && !b2) {
            return;
        }
        assertEqual(iter1.next(), iter2.next());
        assert iter1.nextIndex() == iter2.nextIndex();
        assert iter1.previousIndex() == iter2.previousIndex();
    }
    
    private static final <E> void assertListIteratorPreviousEqual(
            final ListIterator<? extends E> iter1,
            final ListIterator<? extends E> iter2) {
        final boolean b1 = iter1.hasPrevious();
        final boolean b2 = iter2.hasPrevious();
        assert b1 == b2;
        if (!b1 && !b2) {
            return;
        }
        assertEqual(iter1.previous(), iter2.previous());
        assert iter1.nextIndex() == iter2.nextIndex();
        assert iter1.previousIndex() == iter2.previousIndex();
    }
    
    private static final <E> void assertListIteratorPositionEqual(
            final ListIterator<? extends E> iter1,
            final ListIterator<? extends E> iter2) {
        assertListIteratorNextEqual(iter1, iter2);
        assertListIteratorPreviousEqual(iter1, iter2);
    }
    
    private static final <E> int assertListIteratorsEqual(final ListIterator<E> iter1,
            final ListIterator<E> iter2) {
        final Random random = new Random();
        final Queue<E> queue = new LinkedList<>();
        while (true) {
            final boolean b1 = iter1.hasNext();
            final boolean b2 = iter2.hasNext();
            assert b1 == b2;
            if (!b1 && !b2) {
                break;
            }
            assertListIteratorPositionEqual(iter1, iter2);
            final E e1 = iter1.next();
            iter2.next();
            if (random.nextBoolean()) {
                iter1.remove();
                iter2.remove();
                queue.add(e1);
            }
            if (random.nextBoolean() && !queue.isEmpty()) {
                final E e = queue.remove();
                iter1.add(e);
                iter1.add(e);
            }
            if (random.nextBoolean() && !queue.isEmpty()) {
                final E e = queue.peek();
                iter1.set(e);
                iter2.set(e);
            }
        }
        final int size1 = iter1.nextIndex();
        final int size2 = iter2.nextIndex();
        assert size1 == size2;
        while (true) {
            final boolean b1 = iter1.hasPrevious();
            final boolean b2 = iter2.hasPrevious();
            assert b1 == b2;
            if (!b1 && !b2) {
                break;
            }
            assertListIteratorPositionEqual(iter1, iter2);
            final E e1 = iter1.previous();
            iter2.previous();
            if (random.nextBoolean()) {
                iter1.remove();
                iter2.remove();
                queue.add(e1);
            }
            if (random.nextBoolean() && !queue.isEmpty()) {
                final E e = queue.remove();
                iter1.add(e);
                iter1.add(e);
            }
            if (random.nextBoolean() && !queue.isEmpty()) {
                final E e = queue.peek();
                iter1.set(e);
                iter2.set(e);
            }
        }
        assert iter1.previousIndex() == -1;
        assert iter2.previousIndex() == -1;
        for (final E e : queue) {
            iter1.add(e);
            iter2.add(e);
        }
        return size1;
    }
    
    public RingBufferTester() {
        ring = new RingBuffer<>();
        list = new ArrayList<>();
        assertEqual();
    }
    
    public RingBufferTester(final int size) {
        ring = new RingBuffer<>(size);
        list = new ArrayList<>(size);
        assertEqual();
    }
    
    @SafeVarargs
    public RingBufferTester(final E... a) {
        ring = new RingBuffer<>(a);
        list = new ArrayList<>(Arrays.asList(a));
        assertEqual();
    }
    
    public RingBufferTester(final Collection<? extends E> c) {
        ring = new RingBuffer<>(c);
        list = new ArrayList<>(c);
        assertEqual();
    }
    
    public void trimToSize() {
        ring.trimToSize();
        list.trimToSize();
        assertEqual();
    }
    
    @Override
    public int size() {
        assert ring.size() == list.size();
        return ring.size();
    }
    
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    
    @Override
    public E getFirst() {
        final E e1 = ring.getFirst();
        final E e2 = list.get(0);
        assertEqual(e1, e2);
        return e1;
    }
    
    @Override
    public E getLast() {
        final E e1 = ring.getLast();
        final E e2 = list.get(list.size() - 1);
        assertEqual(e1, e2);
        return e1;
    }
    
    @Override
    public E element() {
        return getFirst();
    }
    
    @Override
    public E get(final int index) {
        final E e1 = ring.get(index);
        final E e2 = list.get(index);
        assertEqual(e1, e2);
        return e1;
    }
    
    @Override
    public E set(final int index, final E element) {
        final E e1 = ring.set(index, element);
        final E e2 = list.set(index, element);
        assertEqual(e1, e2);
        assertEqual();
        return e1;
    }
    
    @Override
    public void addFirst(final E e) {
        ring.addFirst(e);
        list.add(0, e);
        assertEqual();
    }
    
    @Override
    public void addLast(final E e) {
        ring.addLast(e);
        list.add(e);
        assertEqual();
    }
    
    @Override
    public boolean offerFirst(final E e) {
        addFirst(e);
        return true;
    }
    
    @Override
    public boolean offerLast(final E e) {
        addLast(e);
        return true;
    }
    
    @Override
    public boolean add(final E e) {
        addLast(e);
        return true;
    }
    
    public void enqueue(final E e) {
        add(e);
    }
    
    @Override
    public boolean offer(final E e) {
        return offerLast(e);
    }
    
    @Override
    public void push(final E e) {
        addFirst(e);
    }
    
    @Override
    public void add(final int index, final E element) {
        ring.add(index, element);
        list.add(index, element);
        assertEqual();
    }
    
    @Override
    public E removeFirst() {
        final E e1 = ring.removeFirst();
        final E e2 = list.remove(0);
        assertEqual(e1, e2);
        assertEqual();
        return e1;
    }
    
    @Override
    public E removeLast() {
        final E e1 = ring.removeLast();
        final E e2 = list.remove(list.size() - 1);
        assertEqual(e1, e2);
        assertEqual();
        return e1;
    }
    
    @Override
    public E remove() {
        return removeFirst();
    }
    
    public E dequeue() {
        return remove();
    }
    
    @Override
    public E pop() {
        return removeFirst();
    }
    
    @Override
    public E remove(final int index) {
        final E e1 = ring.remove(index);
        final E e2 = list.remove(index);
        assertEqual(e1, e2);
        assertEqual();
        return e1;
    }
    
    @Override
    public void removeRange(final int fromIndex, final int toIndex) {
        ring.removeRange(fromIndex, toIndex);
        list.subList(fromIndex, toIndex).clear();
        assertEqual();
    }
    
    @Override
    public boolean removeFirstOccurrence(final Object o) {
        final boolean b1 = ring.removeFirstOccurrence(o);
        final boolean b2 = list.remove(o);
        assert b1 == b2;
        assertEqual();
        return b1;
    }
    
    @Override
    public boolean removeLastOccurrence(final Object o) {
        final boolean b1 = ring.removeFirstOccurrence(o);
        final int i = list.lastIndexOf(o);
        final boolean b2 = i != -1;
        if (b2) {
            list.remove(i);
        }
        assert b1 == b2;
        assertEqual();
        return b1;
    }
    
    @Override
    public boolean remove(final Object o) {
        return removeFirstOccurrence(o);
    }
    
    @Override
    public int indexOf(final Object o) {
        final int i1 = ring.indexOf(o);
        final int i2 = list.indexOf(o);
        assert i1 == i2;
        return i2;
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        final int i1 = ring.lastIndexOf(o);
        final int i2 = list.lastIndexOf(o);
        assert i1 == i2;
        return i2;
    }
    
    @Override
    public boolean contains(final Object o) {
        return indexOf(o) != -1;
    }
    
    @SafeVarargs
    public final boolean addAll(final int index, final E... a) {
        final boolean b1 = ring.addAll(index, a);
        final boolean b2 = list.addAll(index, Arrays.asList(a));
        assert b1 == b2;
        assertEqual();
        return b1;
    }
    
    @SafeVarargs
    public final boolean addAll(final E... a) {
        return addAll(size(), a);
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        return addAll(size(), c);
    }
    
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        final boolean b1 = ring.addAll(index, c);
        final boolean b2 = list.addAll(index, c);
        assert b1 == b2;
        assertEqual();
        return b1;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        final boolean b1 = ring.removeAll(c);
        final boolean b2 = list.removeAll(c);
        assert b1 == b2;
        assertEqual();
        return b1;
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        final boolean b1 = ring.retainAll(c);
        final boolean b2 = list.retainAll(c);
        assert b1 == b2;
        assertEqual();
        return b1;
    }
    
    @Override
    public void clear() {
        ring.clear();
        list.clear();
        assertEqual();
    }
    
    @SuppressWarnings("unchecked")
    private RingBufferTester(final RingBufferTester<E> clone) {
        ring = clone.ring.clone();
        list = (ArrayList<E>) clone.list.clone();
        assertEqual();
    }
    
    @Override
    public RingBufferTester<E> clone() {
        return new RingBufferTester<>(this);
    }
    
    @Override
    public Object[] toArray() {
        final Object[] a1 = ring.toArray();
        final Object[] a2 = list.toArray();
        assertArraysEqual(a1, a2);
        return a1;
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        final T[] a1 = ring.toArray(a);
        final T[] a2 = list.toArray(a);
        assertArraysEqual(a1, a2);
        return a1;
    }
    
    private final void removingIterator() {
        assertRemovingIteratorsEqual(ring.iterator(), list.iterator());
        assertEqual();
    }
    
    @Override
    public Iterator<E> iterator() {
        assertIteratorsEqual(ring.iterator(), list.iterator());
        removingIterator();
        return ring.iterator();
    }
    
    private static final <E> Iterator<E> listDescendingIterator(final ArrayList<E> list) {
        @SuppressWarnings("unchecked")
        final ArrayList<E> listCopy = (ArrayList<E>) list.clone();
        Collections.reverse(listCopy);
        return listCopy.iterator();
    }
    
    private final void removingDescendingIterator() {
        assertRemovingIteratorsEqual(ring.descendingIterator(), listDescendingIterator(list));
        assertEqual();
    }
    
    @Override
    public Iterator<E> descendingIterator() {
        assertIteratorsEqual(ring.descendingIterator(), listDescendingIterator(list));
        removingDescendingIterator();
        return ring.descendingIterator();
    }
    
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }
    
    @Override
    public ListIterator<E> listIterator(final int index) {
        final int size = assertListIteratorsEqual(ring.listIterator(index),
                list.listIterator(index));
        assert size == size();
        assertEqual();
        return ring.listIterator(index);
    }
    
    @Override
    public int hashCode() {
        final int h1 = ring.hashCode();
        final int h2 = list.hashCode();
        assert h1 == h2;
        return h1;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RingBufferTester)) {
            return false;
        }
        
        final boolean b1 = ring.equals(obj);
        final boolean b2 = list.equals(obj);
        assert b1 == b2;
        return b1;
    }
    
    @Override
    public String toString() {
        final String s1 = ring.toString();
        final String s2 = list.toString();
        assertEqual(s1, s2);
        return s1;
    }
    
    public void sort() {
        sort(null);
    }
    
    public void sort(final Comparator<? super E> comparator) {
        ring.sort(comparator);
        Collections.sort(list, comparator);
        assertEqual();
    }
    
    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public E peekFirst() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public E peekLast() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public E poll() {
        return pollFirst();
    }
    
    @Override
    public E peek() {
        return peekFirst();
    }
    
    public static void main(final String[] args) {
        final RingBufferTester<Integer> intTester = new RingBufferTester<>();
        // TODO
    }
    
}
