
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
    
    private Queen solution;
    
    public QueenBoard(final int size) {
        this.size = size;
        delegate = new NQueens(size);
    }
    
    public boolean solve() {
        solution = delegate.solution().get(size);
        return solution != null;
    }
    
    public boolean countSolutions() {
        return delegate.numSolutions() != 0;
    }
    
    public int getCount() {
        return (int) delegate.numSolutions();
    }
    
    @Override
    public String toString() {
        return solution.board().toString('Q', '_');
    }
    
    public static void main(final String[] args) {
        final QueenBoard board = new QueenBoard(15);
        board.solve();
        System.out.println(board);
        System.out.println(board.getCount());
    }
    
}
