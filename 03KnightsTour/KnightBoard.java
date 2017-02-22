
/**
 * 
 * 
 * @author Khyber Sen
 */
public class KnightBoard {
    
    public static String name() {
        return "Sen,Khyber";
    }
    
    private final KnightsTour delegate;
    
    public KnightBoard(final int m, final int n) {
        delegate = new IterativeWarnsdorffKnightsTour(m, n);
    }
    
    public void solve() {
        delegate.solve();
    }
    
    @Override
    public String toString() {
        return delegate.toString();
    }
    
}
