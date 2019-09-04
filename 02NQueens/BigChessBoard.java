
/**
 * unfinished, but supposed to support ChessBoards of size n > 64 where longs
 * cannot be used to a single row
 * 
 * @author Khyber Sen
 */
@Deprecated
public class BigChessBoard extends ChessBoard {
    
    public BigChessBoard(final int n) {
        super(n);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public ChessBoard clone() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void set(final int i, final int j) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean get(final int i, final int j) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    protected void addQueenColumn(final int j) {
        // TODO Auto-generated method stub
        
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
