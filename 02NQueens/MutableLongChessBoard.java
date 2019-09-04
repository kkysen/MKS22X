
/**
 * 
 * 
 * @author Khyber Sen
 */
public class MutableLongChessBoard extends ChessBoard {
    
    /**
     * 5 possible states for each position
     * 1st state: empty
     * max 4 queens can threaten each position
     * 6th state where queen occupies the position can be ignored,
     * because that is taken care of by the columns (cols)
     */
    private static final int mask = 0b11111;
    
    private final long cols = 0;
    private final long[] rows;
    
    public MutableLongChessBoard(final int n) {
        super(n);
        rows = new long[n * mask];
    }
    
    @Override
    public ChessBoard clone() {
        // can be cloned, but no need to, can remove queen instead
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void set(final int i, final int j) {
        
    }
    
    @Override
    public boolean get(final int i, final int j) {
        return false;
    }
    
    @Override
    protected void addQueenColumn(final int j) {
        // can be done, but already done internally in addQueen
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ChessBoard addQueen(final Queen queen) {
        
        return this;
    }
    
    @Override
    public void removeQueen(final Queen queen) {
        
    }
    
    /**
     * @see ChessBoard#isRowFull(int)
     */
    @Override
    public boolean isRowFull(final int i) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see ChessBoard#flip(int, int)
     */
    @Override
    public void flip(final int i, final int j) {
        // TODO Auto-generated method stub
        
    }
    
}
