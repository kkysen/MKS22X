package stacks;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class PrimitiveStack {
    
    protected int size;
    
    protected PrimitiveStack(final int size) {
        this.size = size;
    }
    
    public final int size() {
        return size;
    }
    
    public final boolean isEmpty() {
        return size == 0;
    }
    
    public abstract void toString(final StringBuilder sb);
    
    @Override
    public final String toString() {
        if (size == 0) {
            return "[]";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        toString(sb);
        sb.setCharAt(sb.length() - 2, ']');
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
}
