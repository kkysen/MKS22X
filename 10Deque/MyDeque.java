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
    
    public static void main(final String[] args) {
        final MyDeque d = new MyDeque();
        d.add("hello");
        d.add("world");
        System.out.println(d);
        d.addFirst("khyber");
        System.out.println(d);
        d.removeLast();
        System.out.println(d);
    }
    
}
