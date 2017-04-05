import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class Stack<E> {
    
    private Object[] elements;
    private int size = 0;
    
    public Stack() {
        this(10);
    }
    
    public Stack(final int size) {
        elements = new Object[size];
    }
    
    public Stack(final Collection<? extends E> c) {
        elements = c.toArray();
        size = elements.length;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void push(final E e) {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, size << 1);
        }
        elements[size++] = e;
    }
    
    private final void checkEmpty() {
        if (size == 0) {
            throw new EmptyStackException();
        }
    }
    
    @SuppressWarnings("unchecked")
    public E pop() {
        checkEmpty();
        return (E) elements[--size];
    }
    
    @SuppressWarnings("unchecked")
    public E peek() {
        checkEmpty();
        return (E) elements[size - 1];
    }
    
    public static void main(final String[] args) {
        final Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(10);
        System.out.println(stack.peek());
        System.out.println(stack.size());
        System.out.println(stack.pop());
        System.out.println(stack.size());
        System.out.println(stack.pop());
    }
    
}
