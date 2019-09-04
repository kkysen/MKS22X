package stacks;

import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class IntStack extends PrimitiveStack {
    
    private int[] a;
    
    public IntStack() {
        this(10);
    }
    
    public IntStack(final int size) {
        super(size);
        a = new int[size];
    }
    
    public void push(final int i) {
        if (a.length == size) {
            a = Arrays.copyOf(a, size << 1);
        }
        a[size++] = i;
    }
    
    public int pop() {
        return a[--size];
    }
    
    public int peek() {
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
