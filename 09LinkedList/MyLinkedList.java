import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class MyLinkedList<E> implements List<E> {
    
    private static class Node<E> {
        
        private E value;
        private Node<E> prev;
        private Node<E> next;
        
        private Node(final E value, final Node<E> prev, final Node<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
        
    }
    
    private int size;
    
    private Node<E> first;
    private Node<E> last;
    
    private MyLinkedList(final Node<E> first, final Node<E> last, final int size) {
        this.size = size;
        this.first = first;
        this.last = last;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    private static final void clearNode(final Node<?> node) {
        node.value = null;
        node.prev = null;
        node.next = null;
    }
    
    private static final <E> Node<E> getNodeAfter(Node<E> node, int index) {
        for (; node != null; node = node.next, index--) {
            if (index == 0) {
                return node;
            }
        }
        return null;
    }
    
    private static final <E> Node<E> getNodeBefore(Node<E> node, int index) {
        for (; node != null; node = node.prev, index--) {
            if (index == 0) {
                return node;
            }
        }
        return null;
    }
    
    private static final <E> Node<E> getNode(Node<E> node, final Object o) {
        if (o == null) {
            for (; node != null; node = node.next) {
                if (node.value == null) {
                    return node;
                }
            }
        } else {
            for (; node != null; node = node.next) {
                if (o.equals(node.value)) {
                    return node;
                }
            }
        }
        return null;
    }
    
    private static final int getNodeIndex(Node<?> node, final Object o) {
        int i = 0;
        if (o == null) {
            for (; node != null; node = node.next, i++) {
                if (node.value == null) {
                    return i;
                }
            }
        } else {
            for (; node != null; node = node.next, i++) {
                if (o.equals(node.value)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private static final <E> Node<E> removeNode(final Node<E> node) {
        final Node<E> prev = node.prev;
        prev.next = node.next;
        clearNode(node);
        return prev;
    }
    
    @Override
    public boolean contains(final Object o) {
        return indexOf(o) != -1;
    }
    
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            
            private Node<E> node = first;
            
            @Override
            public boolean hasNext() {
                return node == null;
            }
            
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new IllegalStateException();
                }
                node = node.next;
                return node.value;
            }
            
            @Override
            public void remove() {
                if (node.prev == null) {
                    if (node.next == null) {
                        node = null;
                        return;
                    }
                    node = node.next;
                    node.prev = null;
                    return;
                }
                node = removeNode(node);
            }
            
        };
    }
    
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
    
    @Override
    public <T> T[] toArray(T[] a) {
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
    
    public void addFirst(final E e) {
        final Node<E> newFirst = new Node<>(e, null, first);
        first.prev = newFirst;
        first = newFirst;
    }
    
    public void addLast(final E e) {
        final Node<E> newLast = new Node<>(e, last, null);
        last.next = newLast;
        last = newLast;
    }
    
    @Override
    public boolean add(final E e) {
        addLast(e);
        return true;
    }
    
    @Override
    public boolean remove(final Object o) {
        final Node<E> remove = getNode(first, o);
        if (remove == first) {
            removeFirst();
        } else if (remove == last) {
            removeLast();
        } else {
            removeNode(remove);
        }
        return true;
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        for (final Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void clear() {
        for (Node<E> node = first; node != null;) {
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
            node = getNodeBefore(last, size - index);
        }
        return node;
    }
    
    @Override
    public E get(final int index) {
        return getNode(index).value;
    }
    
    @Override
    public E set(final int index, final E element) {
        final Node<E> node = getNode(index);
        final E removed = node.value;
        node.value = element;
        return removed;
    }
    
    @Override
    public void add(final int index, final E element) {
        if (index == 0) {
            addFirst(element);
        } else if (index == size) {
            addLast(element);
        } else {
            final Node<E> node = getNode(index);
            final Node<E> newNode = new Node<>(element, node.prev, node);
            node.prev = newNode;
            node.prev.next = newNode;
        }
    }
    
    public E removeFirst() {
        final Node<E> oldFirst = first;
        first = first.next;
        first.prev = null;
        final E removed = oldFirst.value;
        clearNode(oldFirst);
        return removed;
    }
    
    public E removeLast() {
        final Node<E> oldLast = last;
        last = last.prev;
        last.next = null;
        final E removed = oldLast.value;
        clearNode(oldLast);
        return removed;
    }
    
    @Override
    public E remove(final int index) {
        if (index == 0) {
            removeFirst();
        }
        if (index == size - 1) {
            removeFirst();
        }
        final Node<E> remove = getNode(index);
        final E removed = remove.value;
        removeNode(remove);
        return removed;
    }
    
    @Override
    public int indexOf(final Object o) {
        return getNodeIndex(first, o);
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        int index = size - 1;
        if (o == null) {
            for (Node<E> node = last; node != null; node = node.prev, index--) {
                if (node.value == null) {
                    return index;
                }
            }
        } else {
            for (Node<E> node = last; node != null; node = node.prev, index--) {
                if (o.equals(node.value)) {
                    return index;
                }
            }
        }
        return -1;
    }
    
    @Override
    public ListIterator<E> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ListIterator<E> listIterator(final int index) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        final Node<E> subFirst = getNode(fromIndex);
        final Node<E> subLast = getNodeAfter(subFirst, toIndex - 1);
        return new MyLinkedList<>(subFirst, subLast, toIndex - fromIndex);
    }
    
}
