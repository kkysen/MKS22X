import java.io.FileNotFoundException;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class KnightBoard {
    
    private static final boolean optimized = true;
    
    public static String name() {
        return "Sen,Khyber";
    }
    
    private final KnightsTour delegate;
    
    public KnightBoard(final int m, final int n) {
        delegate = optimized
                ? new IterativeWarnsdorffKnightsTour(m, n)
                : new NaiveKnightsTour(m, n);
    }
    
    public KnightBoard(final int n) {
        this(n, n);
    }
    
    public KnightsTour getDelegate() {
        return delegate;
    }
    
    public void solve() {
        delegate.solve();
    }
    
    public void solveFast() {
        delegate.solve();
    }
    
    @Override
    public String toString() {
        return delegate.toString();
    }
    
    public static void main(final String[] args) throws FileNotFoundException {
        final int maxSize = optimized ? 63 : 8;
        for (int size = 0; size <= maxSize; size++) {
            final KnightBoard kb = new KnightBoard(size);
            KnightsTour.test(kb.getDelegate(), true);
        }
    }
    
}
