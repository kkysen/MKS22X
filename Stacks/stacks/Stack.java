package stacks;

import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Stack<E> extends PrimitiveStack {
    
    private Object[] a;
    
    public Stack() {
        this(10);
    }
    
    public Stack(final int size) {
        super(size);
        a = new Object[size];
    }
    
    public void push(final E e) {
        if (a.length == size) {
            a = Arrays.copyOf(a, size << 1);
        }
        a[size++] = e;
    }
    
    @SuppressWarnings("unchecked")
    public E pop() {
        return (E) a[--size];
    }
    
    @SuppressWarnings("unchecked")
    public E peek() {
        return (E) a[size - 1];
    }
    
    @Override
    public final void toString(final StringBuilder sb) {
        for (int i = 0; i < size; i++) {
            sb.append(a[i]);
            sb.append(',');
            sb.append(' ');
        }
    }
    
}
