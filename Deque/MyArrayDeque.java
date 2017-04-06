import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MyArrayDeque<E> implements Deque<E>, List<E>, Cloneable {
    
    private Object[] elements;
    
    private int size;
    
    private int first;
    private int last;
    
    /**
     * @see java.util.Deque#size()
     */
    @Override
    public int size() {
        return size;
    }
    
    /**
     * @see java.util.Collection#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * @see java.util.Collection#toArray()
     */
    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.Collection#toArray(java.lang.Object[])
     */
    @Override
    public <T> T[] toArray(final T[] a) {
        // TODO Auto-generated method stub
        return null;
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
    
    /**
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        // TODO Auto-generated method stub
        return false;
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
        // TODO Auto-generated method stub
        
    }
    
    /**
     * @see java.util.Deque#addFirst(java.lang.Object)
     */
    @Override
    public void addFirst(final E e) {
        offerFirst(e);
    }
    
    /**
     * @see java.util.Deque#addLast(java.lang.Object)
     */
    @Override
    public void addLast(final E e) {
        offerLast(e);
    }
    
    /**
     * @see java.util.Deque#offerFirst(java.lang.Object)
     */
    @Override
    public boolean offerFirst(final E e) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.Deque#offerLast(java.lang.Object)
     */
    @Override
    public boolean offerLast(final E e) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.Deque#removeFirst()
     */
    @Override
    public E removeFirst() {
        return pollFirst();
    }
    
    /**
     * @see java.util.Deque#removeLast()
     */
    @Override
    public E removeLast() {
        return pollLast();
    }
    
    /**
     * @see java.util.Deque#pollFirst()
     */
    @Override
    public E pollFirst() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.Deque#pollLast()
     */
    @Override
    public E pollLast() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.Deque#getFirst()
     */
    @Override
    public E getFirst() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.Deque#getLast()
     */
    @Override
    public E getLast() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.Deque#peekFirst()
     */
    @Override
    public E peekFirst() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.Deque#peekLast()
     */
    @Override
    public E peekLast() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.Deque#removeFirstOccurrence(java.lang.Object)
     */
    @Override
    public boolean removeFirstOccurrence(final Object o) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.Deque#removeLastOccurrence(java.lang.Object)
     */
    @Override
    public boolean removeLastOccurrence(final Object o) {
        // TODO Auto-generated method stub
        return false;
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
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.Deque#contains(java.lang.Object)
     */
    @Override
    public boolean contains(final Object o) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.Deque#iterator()
     */
    @Override
    public Iterator<E> iterator() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.Deque#descendingIterator()
     */
    @Override
    public Iterator<E> descendingIterator() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.List#get(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public E get(final int index) {
        // TODO Auto-generated method stub
        return (E) elements[(first + index) % elements.length];
    }
    
    /**
     * @see java.util.List#set(int, java.lang.Object)
     */
    @Override
    public E set(final int index, final E element) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#add(int, java.lang.Object)
     */
    @Override
    public void add(final int index, final E element) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * @see java.util.List#remove(int)
     */
    @Override
    public E remove(final int index) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#indexOf(java.lang.Object)
     */
    @Override
    public int indexOf(final Object o) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /**
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    @Override
    public int lastIndexOf(final Object o) {
        // TODO Auto-generated method stub
        return 0;
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
