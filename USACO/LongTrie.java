import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class LongTrie {
    
    private static final int BITS = 4;
    private static final int MASK = (1 << BITS) - 1;
    private static final int NUM_BUCKETS = MASK + 1;
    private final int levels;
    
    public LongTrie() {
        levels = Long.SIZE / BITS;
    }
    
    public LongTrie(final int maxBits) {
        if (maxBits > Long.SIZE) {
            throw new IllegalArgumentException();
        }
        levels = (int) Math.ceil((double) maxBits / BITS);
    }
    
    private static class Entry {
        
        private long value;
        private final Entry[] buckets = new Entry[NUM_BUCKETS];
        
    }
    
    private final Entry root = new Entry();
    
    // 0L keys don't work
    public void put(final long key, final long value) {
        Entry entry = root;
        for (int i = 0; i < levels; i++) {
            final int hash = (int) (key >>> BITS * i) & MASK;
            try {
                if (entry.buckets[hash] == null) {
                    entry.buckets[hash] = new Entry();
                }
            } catch (final ArrayIndexOutOfBoundsException e) {
                System.out.println(Arrays.toString(entry.buckets));
                System.out.println(entry.buckets.length);
                throw e;
            }
            entry = entry.buckets[hash];
        }
        entry.value = value;
    }
    
    private Entry getEntry(final long key) {
        Entry entry = root;
        for (int i = 0; i < levels; i++) {
            final int hash = (int) (key >>> BITS * i) & MASK;
            entry = entry.buckets[hash];
            if (entry == null) {
                entry = root; // always has value = 0
            }
        }
        return entry;
    }
    
    public long get(final long key) {
        return getEntry(key).value;
    }
    
    public long remove(final long key) {
        final Entry entry = getEntry(key);
        final long value = entry.value;
        entry.value = 0;
        return value;
    }
    
    public static void main(final String[] args) {
        final LongTrie trie = new LongTrie(16);
        trie.put(1234, 4321);
        trie.put(2345, 5432);
        trie.put(1235, 9876);
        trie.remove(2345);
        System.out.println(trie.get(1234));
        System.out.println(trie.get(2345));
        System.out.println(trie.get(1235));
    }
    
}
