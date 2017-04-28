package stacks;

import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ByteStack extends PrimitiveStack {
    
    private byte[] a;
    
    public ByteStack() {
        this(10);
    }
    
    public ByteStack(final int size) {
        super(size);
        a = new byte[size];
    }
    
    public void push(final byte b) {
        if (a.length == size) {
            a = Arrays.copyOf(a, size << 1);
        }
        a[size++] = b;
    }
    
    public byte pop() {
        return a[--size];
    }
    
    public byte peek() {
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
