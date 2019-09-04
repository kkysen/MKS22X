import java.util.Arrays;

/**
 * DoubleStack
 * 
 * @author Khyber Sen
 */
public class DoubleStack {
    
    private double[] a;
    private int size = 0;
    
    public DoubleStack() {
        this(10);
    }
    
    public DoubleStack(final int size) {
        a = new double[size];
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void push(final double i) {
        if (a.length == size) {
            a = Arrays.copyOf(a, size << 1);
        }
        a[size++] = i;
    }
    
    public double pop() {
        return a[--size];
    }
    
    public double peek() {
        return a[size - 1];
    }
    
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(a[i]);
            sb.append(',');
            sb.append(' ');
        }
        sb.setCharAt(sb.length() - 2, ']');
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
}
