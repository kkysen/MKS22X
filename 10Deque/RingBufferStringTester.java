import java.util.Collection;
import java.util.Random;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class RingBufferStringTester extends RingBufferTester<String> {
    
    private static final Random random = RingBufferTester.random;
    
    @Override
    protected final String randomElement() {
        final byte[] bytes = new byte[random.nextInt(10)];
        random.nextBytes(bytes);
        return new String(bytes);
    }
    
    public RingBufferStringTester() {}
    
    public RingBufferStringTester(final int size) {
        super(size);
    }
    
    public RingBufferStringTester(final String... a) {
        super(a);
    }
    
    public RingBufferStringTester(final Collection<? extends String> c) {
        super(c);
    }
    
    private RingBufferStringTester(final RingBufferStringTester clone) {
        super(clone);
    }
    
    @Override
    protected RingBufferStringTester clone() {
        return new RingBufferStringTester(this);
    }
    
    public static void main(final String[] args) {
        final RingBufferStringTester tester = new RingBufferStringTester();
        tester.test(100);
    }
    
}
