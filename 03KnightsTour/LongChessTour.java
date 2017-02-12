
/**
 * 
 * 
 * @author Khyber Sen
 */
public class LongChessTour extends LongChessBoard {
    
    private int iMove;
    private int jMove;
    
    private int prevI;
    private int prevJ;
    
    public LongChessTour(final int n) {
        super(n);
    }
    
    @Override
    public void set(final int i, final int j) {
        super.set(i, j);
        prevI = iMove;
        prevJ = jMove;
        iMove = i;
        jMove = j;
    }
    
    /**
     * @see LongChessBoard#flip(int, int)
     */
    @Override
    public void flip(final int i, final int j) {
        super.flip(i, j);
        iMove = prevI;
        jMove = prevJ;
    }
    
    @Override
    public String toString() {
        return toString(new CharChooser() {
            
            @Override
            public char choose(final int i, final int j, final boolean full) {
                return i == iMove && j == jMove ? '\u25C9' : full ? '\u25A0' : '\u25A1';
            }
            
        });
    }
    
}
