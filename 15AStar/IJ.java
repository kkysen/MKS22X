/**
 * 
 * 
 * @author Khyber Sen
 */
public class IJ {
    
    public final int i;
    public final int j;
    public final IJ previous;
    public final int distance;
    
    public IJ(final int i, final int j, final IJ previous, final int distance) {
        this.i = i;
        this.j = j;
        this.previous = previous;
        this.distance = distance;
    }
    
    public IJ next(final int i, final int j) {
        return new IJ(i, j, this, distance + 1);
    }
    
    @Override
    public String toString() {
        return "(" + i + ", " + j + ")";
    }
    
}
