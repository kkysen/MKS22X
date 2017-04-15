import java.util.Collection;

/**
 * String wrapper for {@link RingBuffer}.
 * 
 * @author Khyber Sen
 */
public class MyDeque extends RingBuffer<String> {
    
    private static final long serialVersionUID = 6918361035727497198L;
    
    public MyDeque() {}
    
    public MyDeque(final int size) {
        super(size);
    }
    
    public MyDeque(final String... strings) {
        super(strings);
    }
    
    public MyDeque(final Collection<? extends String> strings) {
        super(strings);
    }
    
}
