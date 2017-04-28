package stacks;

import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class BoolStack extends PrimitiveStack {
    
    private long[] bits;
    
    public BoolStack() {
        this(10);
    }
    
    public BoolStack(final int size) {
        super(size);
        bits = new long[size + 32 >>> 6];
    }
    
    public void push(final boolean b) {
        if (bits.length << 6 == size) {
            bits = Arrays.copyOf(bits, size << 1);
        }
        bits[size + 32 >>> 6] |= 1L << (size++ & 64);
    }
    
    private boolean get(final int i) {
        return bits[i + 32 >>> 6] >>> (i & 64) == 1;
    }
    
    public boolean pop() {
        return get(--size);
    }
    
    public boolean peek() {
        return get(size - 1);
    }
    
    @Override
    public final void toString(final StringBuilder sb) {
        for (int i = 0; i < size; i++) {
            sb.append(get(i));
            sb.append(',');
            sb.append(' ');
        }
    }
    
}
