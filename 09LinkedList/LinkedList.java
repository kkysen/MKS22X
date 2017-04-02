import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * my own implementation of a doubly-linked list
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class LinkedList<E> implements List<E>, Deque<E>, Cloneable {
    
    private static class Node<E> {
        
        private E value;
        private Node<E> prev;
        private Node<E> next;
        
        private Node(final E value, final Node<E> prev, final Node<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
        
        /**
         * @see Object#toString()
         */
        @Override
        public String toString() {
            return value.toString();
        }
        
    }
    
    private int size;
    
    private Node<E> first;
    private Node<E> last;
    
    /**
     * @param first starting linked node
     * @param last ending linked node
     * @param size size
     */
    @SuppressWarnings("unused")
    private LinkedList(final Node<E> first, final Node<E> last, final int size) {
        this.size = size;
        this.first = first;
        this.last = last;
    }
    
    /**
     * Creates an empty list
     */
    public LinkedList() {}
    
    /**
     * Creates a list with the elements of c
     * 
     * @param c the collection whose elements are to be placed in this list
     */
    public LinkedList(final Collection<? extends E> c) {
        addAll(c);
    }
    
    /**
     * @see List#size()
     */
    @Override
    public int size() {
        return size;
    }
    
    /**
     * @see List#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    private static void checkIndexForSize(final int index, final int size) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index: " + index + "; size: " + size);
        }
    }
    
    private void checkIndex(final int index) {
        checkIndexForSize(index, size);
    }
    
    private void checkIndexForAdd(final int index) {
        checkIndexForSize(index, size + 1);
    }
    
    /**
     * sets the fields of node to null
     * 
     * @param node node to clear
     */
    private static final void clearNode(final Node<?> node) {
        node.value = null;
        node.prev = null;
        node.next = null;
    }
    
    /**
     * returns the node index after this node
     * 
     * @param node the node to start searching from
     * @param index the index after node
     * @return the node index after this node
     *         null if not found
     */
    private static final <E> Node<E> getNodeAfter(Node<E> node, int index) {
        for (; node != null; node = node.next, index--) {
            if (index == 0) {
                return node;
            }
        }
        return null;
    }
    
    /**
     * returns the node index before this node
     * 
     * @param node the node to start searching from
     * @param index the index before node
     * @return the node index before this node
     *         null if not found
     */
    private static final <E> Node<E> getNodeBefore(Node<E> node, int index) {
        for (; node != null; node = node.prev, index--) {
            if (index == 0) {
                return node;
            }
        }
        return null;
    }
    
    /**
     * returns the first node after this node whose value is o
     * 
     * @param node the node to start searching from
     * @param o the object to search for
     * @return the first node after this node whose value is o
     *         null if not found
     */
    private static final <E> Node<E> getNodeAfter(Node<E> node, final Object o, final int size) {
        if (o == null) {
            for (int i = 0; i < size; i++, node = node.next) {
                if (node.value == null) {
                    return node;
                }
            }
        } else {
            for (int i = 0; i < size; i++, node = node.next) {
                if (o.equals(node.value)) {
                    return node;
                }
            }
        }
        return null;
    }
    
    /**
     * returns the first node before this node whose value is o
     * 
     * @param node the node to start searching from
     * @param o the object to search for
     * @return the first node before this node whose value is o
     *         null if not found
     */
    private static final <E> Node<E> getNodeBefore(Node<E> node, final Object o, final int size) {
        if (o == null) {
            for (int i = 0; i < size; i++, node = node.prev) {
                if (node.value == null) {
                    return node;
                }
            }
        } else {
            for (int i = 0; i < size; i++, node = node.prev) {
                if (o.equals(node.value)) {
                    return node;
                }
            }
        }
        return null;
    }
    
    /**
     * return the index of the first node after this node whoe value is o
     * 
     * @param node the node to start searching from
     * @param o the object to search for
     * @return the index of the first node after this node whoe value is o
     *         -1 if not found
     */
    private static final int getNodeIndexAfter(Node<?> node, final Object o, final int size) {
        if (o == null) {
            for (int i = 0; i < size; i++, node = node.next) {
                if (node.value == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++, node = node.next) {
                if (o.equals(node.value)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * returns the previous node
     * 
     * @param node the node to remove (between the nodes around it)
     * @return the previous node
     */
    private static final <E> Node<E> removeNode(final Node<E> node) {
        final Node<E> prev = node.prev;
        prev.next = node.next;
        clearNode(node);
        return prev;
    }
    
    /**
     * @see List#clear()
     */
    @Override
    public void clear() {
        Node<E> node = first;
        for (int i = 0; i < size; i++) {
            final Node<E> next = node.next;
            clearNode(node);
            node = next;
        }
        first = last = null;
        size = 0;
    }
    
    private Node<E> getNode(final int index) {
        final int mid = size >>> 1;
        Node<E> node;
        if (index < mid) {
            node = getNodeAfter(first, index);
        } else {
            node = getNodeBefore(last, size - index - 1);
        }
        return node;
    }
    
    /**
     * @see Deque#peekFirst()
     */
    @Override
    public E peekFirst() {
        if (first == null) {
            return null;
        }
        return first.value;
    }
    
    /**
     * @see Deque#peekLast()
     */
    @Override
    public E peekLast() {
        if (last == null) {
            return null;
        }
        return last.value;
    }
    
    /**
     * @see Deque#peek()
     */
    @Override
    public E peek() {
        return peekFirst();
    }
    
    /**
     * @see Deque#getFirst()
     */
    @Override
    public E getFirst() {
        final E e = peekFirst();
        if (e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }
    
    /**
     * @see Deque#getLast()
     */
    @Override
    public E getLast() {
        final E e = peekLast();
        if (e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }
    
    /**
     * @see Deque#element()
     */
    @Override
    public E element() {
        return getFirst();
    }
    
    /**
     * @see List#get(int)
     */
    @Override
    public E get(final int index) {
        checkIndex(index);
        return getNode(index).value;
    }
    
    /**
     * @see List#set(int, java.lang.Object)
     */
    @Override
    public E set(final int index, final E element) {
        checkIndex(index);
        final Node<E> node = getNode(index);
        final E removed = node.value;
        node.value = element;
        return removed;
    }
    
    /**
     * @see Deque#offerFirst(java.lang.Object)
     */
    @Override
    public boolean offerFirst(final E e) {
        addFirst(e);
        return true;
    }
    
    /**
     * @see Deque#offerLast(java.lang.Object)
     */
    @Override
    public boolean offerLast(final E e) {
        addLast(e);
        return true;
    }
    
    /**
     * @see Deque#offer(java.lang.Object)
     */
    @Override
    public boolean offer(final E e) {
        return offerLast(e);
    }
    
    private void addMiddle(final Node<E> node, final E element) {
        final Node<E> newNode = new Node<>(element, node.prev, node);
        node.prev.next = newNode;
        node.prev = newNode;
        size++;
    }
    
    /**
     * @see Deque#addFirst(java.lang.Object)
     */
    @Override
    public void addFirst(final E e) {
        if (first == null) {
            first = last = new Node<>(e, null, null);
            return;
        }
        if (first != null && first.prev != null) {
            addMiddle(first, e);
            return;
        }
        final Node<E> newFirst = new Node<>(e, null, first);
        if (first == null) {
            first = last = newFirst;
        }
        first.prev = newFirst;
        first = newFirst;
        size++;
    }
    
    /**
     * @see Deque#addLast(java.lang.Object)
     */
    @Override
    public void addLast(final E e) {
        if (last == null) {
            first = last = new Node<>(e, null, null);
            return;
        }
        if (last.next != null) {
            addMiddle(last, e);
            return;
        }
        final Node<E> newLast = new Node<>(e, last, null);
        last.next = newLast;
        last = newLast;
        size++;
    }
    
    /**
     * @see Deque#push(java.lang.Object)
     */
    @Override
    public void push(final E e) {
        addFirst(e);
    }
    
    /**
     * @see List#add(java.lang.Object)
     */
    @Override
    public boolean add(final E e) {
        addLast(e);
        return true;
    }
    
    /**
     * @see List#add(int, java.lang.Object)
     */
    @Override
    public void add(final int index, final E element) {
        checkIndexForAdd(index);
        if (index == 0) {
            addFirst(element);
        } else if (index == size) {
            addLast(element);
        } else {
            addMiddle(getNode(index), element);
        }
    }
    
    private void removeMiddle(final Node<E> toRemove) {
        removeNode(toRemove);
        size--;
    }
    
    /**
     * @see Deque#pollFirst()
     */
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        final E removed = first.value;
        if (first.prev != null) {
            removeMiddle(first);
        } else {
            final Node<E> oldFirst = first;
            first = first.next;
            first.prev = null;
            clearNode(oldFirst);
            size--;
        }
        return removed;
    }
    
    /**
     * @see Deque#pollLast()
     */
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        final E removed = last.value;
        if (last.next != null) {
            removeMiddle(last);
        } else {
            final Node<E> oldLast = last;
            last = last.prev;
            last.next = null;
            clearNode(oldLast);
            size--;
        }
        return removed;
    }
    
    /**
     * @see Deque#poll()
     */
    @Override
    public E poll() {
        return pollFirst();
    }
    
    /**
     * @see Deque#removeFirst()
     */
    @Override
    public E removeFirst() {
        final E removed = pollFirst();
        if (removed == null) {
            throw new NoSuchElementException();
        }
        return removed;
    }
    
    /**
     * @see Deque#removeLast()
     */
    @Override
    public E removeLast() {
        final E removed = pollLast();
        if (removed == null) {
            throw new NoSuchElementException();
        }
        return removed;
    }
    
    /**
     * @see Deque#remove()
     */
    @Override
    public E remove() {
        return removeFirst();
    }
    
    /**
     * @see Deque#pop()
     */
    @Override
    public E pop() {
        return removeFirst();
    }
    
    /**
     * @see List#remove(int)
     */
    @Override
    public E remove(final int index) {
        checkIndex(index);
        if (index == 0) {
            removeFirst();
        }
        if (index == size - 1) {
            removeFirst();
        }
        final Node<E> remove = getNode(index);
        final E removed = remove.value;
        removeMiddle(remove);
        return removed;
    }
    
    /**
     * @see Deque#removeFirstOccurrence(java.lang.Object)
     */
    @Override
    public boolean removeFirstOccurrence(final Object o) {
        final Node<E> remove = getNodeAfter(first, o, size);
        if (remove == null) {
            return false;
        }
        if (remove == first) {
            removeFirst();
        } else if (remove == last) {
            removeLast();
        } else {
            removeMiddle(remove);
        }
        return true;
    }
    
    /**
     * @see Deque#removeLastOccurrence(java.lang.Object)
     */
    @Override
    public boolean removeLastOccurrence(final Object o) {
        final Node<E> remove = getNodeBefore(last, o, size);
        if (remove == null) {
            return false;
        }
        if (remove == first) {
            removeFirst();
        } else if (remove == last) {
            removeLast();
        } else {
            removeMiddle(remove);
        }
        return true;
    }
    
    /**
     * @see List#remove(java.lang.Object)
     */
    @Override
    public boolean remove(final Object o) {
        return removeFirstOccurrence(o);
    }
    
    /**
     * @see List#indexOf(java.lang.Object)
     */
    @Override
    public int indexOf(final Object o) {
        return getNodeIndexAfter(first, o, size);
    }
    
    /**
     * @see List#lastIndexOf(java.lang.Object)
     */
    @Override
    public int lastIndexOf(final Object o) {
        int index = size - 1;
        Node<E> node = last;
        if (o == null) {
            for (; index >= 0; node = node.prev, index--) {
                if (node.value == null) {
                    return index;
                }
            }
        } else {
            for (; index >= 0; node = node.prev, index--) {
                if (o.equals(node.value)) {
                    return index;
                }
            }
        }
        return -1;
    }
    
    /**
     * @see List#contains(java.lang.Object)
     */
    @Override
    public boolean contains(final Object o) {
        return indexOf(o) != -1;
    }
    
    /**
     * @see List#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(final Collection<?> c) {
        Objects.requireNonNull(c);
        for (final Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * @see List#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        return addAll(size, c);
    }
    
    /**
     * @see List#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        Objects.requireNonNull(c);
        final Object[] a = c.toArray();
        final int numNew = a.length;
        if (numNew == 0) {
            return false;
        }
        
        // FIXME for subList views
        Node<E> prev;
        Node<E> next;
        if (index == size) {
            prev = last;
            if (prev == null) {
                next = null;
            } else {
                next = prev.next;
            }
        } else {
            next = getNode(index);
            prev = next.prev;
        }
        
        @SuppressWarnings("unchecked")
        final E e0 = (E) a[0];
        final Node<E> newFirstNode = new Node<>(e0, prev, null);
        if (prev == null) {
            first = newFirstNode;
        } else {
            prev.next = newFirstNode;
        }
        prev = newFirstNode;
        
        for (int i = 1; i < a.length; i++) {
            @SuppressWarnings("unchecked")
            final E e = (E) a[i];
            final Node<E> newNode = new Node<>(e, prev, null);
            prev.next = newNode;
            prev = newNode;
        }
        
        if (next == null) {
            last = prev;
        } else {
            prev.next = next;
            next.prev = prev;
        }
        
        size += numNew;
        return true;
    }
    
    /**
     * @see List#removeAll(java.util.Collection)
     */
    @Override
    public boolean removeAll(final Collection<?> c) {
        Objects.requireNonNull(c);
        boolean removed = false;
        for (final Object o : c) {
            removed = removed || remove(o);
        }
        return removed;
    }
    
    /**
     * @see List#retainAll(java.util.Collection)
     */
    @Override
    public boolean retainAll(final Collection<?> c) {
        Objects.requireNonNull(c);
        throw new UnsupportedOperationException();
    }
    
    /**
     * @see List#toArray()
     */
    @Override
    public Object[] toArray() {
        final Object[] a = new Object[size];
        Node<E> node = first;
        for (int i = 0; i < a.length; i++) {
            a[i] = node.value;
            node = node.next;
        }
        return a;
    }
    
    /**
     * @see List#toArray(java.lang.Object[])
     */
    @Override
    public <T> T[] toArray(T[] a) {
        Objects.requireNonNull(a);
        if (a.length < size) {
            a = Arrays.copyOf(a, size);
        }
        final Object[] result = a;
        Node<E> node = first;
        for (int i = 0; i < size; i++) {
            result[i] = node.value;
            node = node.next;
        }
        return a;
    }
    
    /**
     * @see Object#clone()
     */
    @Override
    public LinkedList<E> clone() {
        return new LinkedList<>(this);
    }
    
    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        final StringBuilder sb = new StringBuilder("[");
        Node<E> node = first;
        for (int i = 0; i < size; i++, node = node.next) {
            sb.append(node.value.toString());
            sb.append(", ");
        }
        sb.setCharAt(sb.length() - 2, ']');
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    /**
     * @see List#iterator()
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            
            private int size = LinkedList.this.size;
            private Node<E> node = first;
            
            @Override
            public boolean hasNext() {
                return size > 0;
            }
            
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new IllegalStateException();
                }
                final E e = node.value;
                node = node.next;
                size--;
                return e;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
            
        };
    }
    
    /**
     * @see Deque#descendingIterator()
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            
            private int size = LinkedList.this.size;
            private Node<E> node = last;
            
            @Override
            public boolean hasNext() {
                return size > 0;
            }
            
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new IllegalStateException();
                }
                final E e = node.value;
                node = node.prev;
                size--;
                return e;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
            
        };
    }
    
    /**
     * @see List#listIterator()
     */
    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * @see List#listIterator(int)
     */
    @Override
    public ListIterator<E> listIterator(final int index) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * @see List#subList(int, int)
     */
    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        //        checkIndex(fromIndex);
        //        checkIndexForAdd(toIndex);
        //        if (fromIndex > toIndex) {
        //            throw new IllegalArgumentException("fromIndex cannot be greater than toIndex");
        //        }
        //        final int subSize = toIndex - fromIndex;
        //        final Node<E> subFirst = getNode(fromIndex);
        //        final Node<E> subLast = getNodeAfter(subFirst, subSize);
        //        return new LinkedList<>(subFirst, subLast, subSize);
        throw new UnsupportedOperationException();
    }
    
}
