
/**
 * 
 * 
 * @author Khyber Sen
 */
public class LongChessBitBoard extends ChessBitBoard {
    
    private long cols;
    private final long[] rows;
    
    public LongChessBitBoard(final int n) {
        super(n);
        cols = 0L;
        rows = new long[n];
    }
    
    private LongChessBitBoard(final LongChessBitBoard other) {
        super(other.n);
        cols = other.cols;
        rows = new long[n];
        System.arraycopy(other.rows, 0, rows, 0, n);
        numClones++;
    }
    
    @Override
    public LongChessBitBoard clone() {
        return new LongChessBitBoard(this);
    }
    
    @Override
    public void set(final int i, final int j) {
        numSets++;
        rows[i] |= 1L << j;
    }
    
    @Override
    protected void addQueenColumn(final int j) {
        cols |= 1L << j;
    }
    
    @Override
    public boolean get(final int i, final int j) {
        numGets++;
        final long col = 1L << j;
        return (cols & col) == col || (rows[i] & col) == col;
    }
    
}
