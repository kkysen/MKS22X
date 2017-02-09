
/**
 * 
 * 
 * @author Khyber Sen
 */
public class LongChessBoard extends ChessBoard {
    
    private static long[] COL_MASKS = new long[64];
    static {
        for (int i = 0; i < 64; i++) {
            COL_MASKS[i] = -1L >>> 64 - i;
        }
    }
    
    private long cols;
    private final long[] rows;
    private final long fullMask;
    
    public LongChessBoard(final int n) {
        super(n);
        cols = 0L;
        rows = new long[n];
        fullMask = COL_MASKS[n];
    }
    
    private LongChessBoard(final LongChessBoard other) {
        super(other.n);
        cols = other.cols;
        rows = new long[n];
        fullMask = other.fullMask;
        System.arraycopy(other.rows, 0, rows, 0, n);
        numClones++;
    }
    
    @Override
    public LongChessBoard clone() {
        return new LongChessBoard(this);
    }
    
    @Override
    public void set(final int i, final int j) {
        numSets++;
        rows[i] |= 1L << j;
    }
    
    @Override
    public boolean isRowFull(final int i) {
        return rows[i] == fullMask;
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