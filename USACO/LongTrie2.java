/**
 * 
 * 
 * @author Khyber Sen
 */
public class LongTrie2 {
    
    private static final int BITS = 4;
    private static final int MASK = (1 << BITS) - 1;
    private static final int NUM_BUCKETS = MASK + 1;
    private final int levels;
    
    public LongTrie2() {
        levels = Long.SIZE / BITS;
    }
    
    public LongTrie2(final int maxBits) {
        if (maxBits > Long.SIZE) {
            throw new IllegalArgumentException();
        }
        levels = (int) Math.ceil((double) maxBits / BITS);
    }
    
    private static class Entry {
        
        private long value;
        private Entry next;
        
        public Entry get(final int index) {
            
        }
        
    }
    
    private final Entry root = new Entry();
    
}
