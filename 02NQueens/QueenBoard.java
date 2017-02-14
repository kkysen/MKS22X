
/**
 * Wrapper class for NQueens
 * 
 * I already wrote NQueens to my specifications, so I just created this wrapper
 * class to have the correct functionality using an NQueens instance as a
 * delegate.
 * 
 * @author Khyber Sen
 */
public class QueenBoard {
    
    public static String name() {
        return "Sen,Khyber";
    }
    
    private final int size;
    
    private final NQueens delegate;
    
    private ChessBoard solution;
    
    public QueenBoard(final int size) {
        this.size = size;
        delegate = new NQueens(size);
    }
    
    public void solve() {
        solution = delegate.firstSolution();
    }
    
    public boolean countSolutions() {
        return delegate.numSolutions() != 0;
    }
    
    public int getCount() {
        return (int) delegate.numSolutions();
    }
    
    public int getSolutionCount() {
        return getCount();
    }
    
    @Override
    public String toString() {
        if (solution == null) {
            return "";
        }
        return solution.toString('Q', '_');
    }
    
    public static void main(final String[] args) {
        final QueenBoard board = new QueenBoard(30);
        board.solve();
        System.out.println(board);
        //System.out.println(board.getCount());
    }
    
}
