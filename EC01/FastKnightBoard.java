
/**
 * 
 * 
 * @author Khyber Sen
 */
public class FastKnightBoard {
    
    public static String name() {
        return "Sen,Khyber";
    }
    
    private final KnightsTour delegate;
    
    public FastKnightBoard(final int m, final int n) {
        delegate = new IterativeWarnsdorffKnightsTour(m, n);
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
    
}
