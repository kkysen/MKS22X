package stacks;

import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class DoubleStack extends PrimitiveStack {
    
    private double[] a;
    
    public DoubleStack() {
        this(10);
    }
    
    public DoubleStack(final int size) {
        super(size);
        a = new double[size];
    }
    
    public void push(final double d) {
        if (a.length == size) {
            a = Arrays.copyOf(a, size << 1);
        }
        a[size++] = d;
    }
    
    public double pop() {
        return a[--size];
    }
    
    public double peek() {
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
