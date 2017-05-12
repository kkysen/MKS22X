/**
 * 
 * 
 * @author Khyber Sen
 */
public interface Frontier {
    
    public int size();
    
    public boolean isEmpty();
    
    public void clear();
    
    public void add(IJ ij, int distance);
    
    public IJ remove();
    
    @Override
    public String toString();
    
}
