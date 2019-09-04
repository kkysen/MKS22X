package stacks;

import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class LongStack extends PrimitiveStack {
    
    private long[] a;
    
    public LongStack() {
        this(10);
    }
    
    public LongStack(final int size) {
        super(size);
        a = new long[size];
    }
    
    public void push(final long L) {
        if (a.length == size) {
            a = Arrays.copyOf(a, size << 1);
        }
        a[size++] = L;
    }
    
    public long pop() {
        return a[--size];
    }
    
    public long peek() {
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
