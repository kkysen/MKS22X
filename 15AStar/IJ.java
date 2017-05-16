/**
 * 
 * 
 * @author Khyber Sen
 */
public class IJ {
    
    public final int i;
    public final int j;
    public final IJ previous;
    
    public IJ(final int i, final int j, final IJ prev) {
        this.i = i;
        this.j = j;
        this.previous = prev;
    }
    
    public IJ next(final int i, final int j) {
        return new IJ(i, j, this);
    }
    
    @Override
    public String toString() {
        return "(" + i + ", " + j + ")";
    }
    
}
