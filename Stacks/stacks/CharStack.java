package stacks;

import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class CharStack extends PrimitiveStack {
    
    private char[] a;
    
    public CharStack() {
        this(10);
    }
    
    public CharStack(final int size) {
        super(size);
        a = new char[size];
    }
    
    public void push(final char c) {
        if (a.length == size) {
            a = Arrays.copyOf(a, size << 1);
        }
        a[size++] = c;
    }
    
    public char pop() {
        return a[--size];
    }
    
    public char peek() {
        return a[size - 1];
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
