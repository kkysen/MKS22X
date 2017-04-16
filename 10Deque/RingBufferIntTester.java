import java.util.Collection;
import java.util.Random;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class RingBufferIntTester extends RingBufferTester<Integer> {
    
    private static final Random random = RingBufferTester.random;
    
    @Override
    protected final Integer randomElement() {
        return random.nextInt(100);
    }
    
    public RingBufferIntTester() {}
    
    public RingBufferIntTester(final int size) {
        super(size);
    }
    
    public RingBufferIntTester(final Integer... a) {
        super(a);
    }
    
    public RingBufferIntTester(final Collection<? extends Integer> c) {
        super(c);
    }
    
    private RingBufferIntTester(final RingBufferIntTester clone) {
        super(clone);
    }
    
    @Override
    protected RingBufferIntTester clone() {
        return new RingBufferIntTester(this);
    }
    
    public static void main(final String[] args) {
        final RingBufferIntTester tester = new RingBufferIntTester();
        tester.test(100);
    }
    
}