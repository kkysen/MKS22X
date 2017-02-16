
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
        delegate = new KnightsTour(m, n);
    }
    
    public void solve() {
        delegate.findTour();
    }
    
    @Override
    public String toString() {
        return delegate.toString();
    }
    
}
