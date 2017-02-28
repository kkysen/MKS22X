
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
    
    private final NQueens delegate;
    
    private int solutionCount = -1;
    private ChessBoard solution;
    
    public QueenBoard(final int size) {
        delegate = new NQueens(size);
    }
    
    public void solve() {
        solution = delegate.firstSolution();
    }
    
    public void countSolutions() {
        solutionCount = (int) delegate.numSolutions();
    }
    
    public int getSolutionCount() {
        return solutionCount;
    }
    
    public int getCount() {
        return getSolutionCount();
    }
    
    @Override
    public String toString() {
        if (solution == null) {
            return "";
        }
        return solution.toString('Q', '_');
    }
    
    public static void main(final String[] args) {
        final QueenBoard board = new QueenBoard(35);
        board.solve();
        System.out.println(board);
        //System.out.println(board.getCount());
    }
    
}
