import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class NQueens {
    
    private final int n;
    
    private final Queen root;
    
    private boolean solved = false;
    
    private int numCalls = 0;
    
    public NQueens(final int n) {
        this.n = n;
        root = Queen.root(n);
    }
    
    /**
     * @param i
     * @param cols
     * @param board
     * @param parent
     * 
     * @implSpec
     *           cols is a long bit set. 64 bits is sufficiently large for the
     *           N-Queens problem and is pretty much unsolvable at that point.
     *           Even a 32-bit int should be large enough.
     */
    private boolean solveForTree(final int i, final long cols, final ChessBitBoard board,
            final Queen parent) {
        numCalls++;
        if (i == n) {
            return true;
        }
        boolean deadend = true;
        for (int j = 0; j < n; j++) {
            final long col = 1L << j;
            if ((col & cols) != col && board.isValidMove(i, j)) {
                final Queen newQueen = parent.newChild(i, j);
                final boolean newDeadend = //
                        !solveForTree(i + 1, cols | col, board.addQueen(newQueen), newQueen);
                if (newDeadend) {
                    newQueen.delete();
                }
                if (deadend) {
                    deadend = newDeadend;
                }
            }
        }
        return !deadend;
    }
    
    public Queen solveForTree() {
        if (solved) {
            return root;
        }
        solveForTree(0, 0L, new ChessBitBoard(n), root);
        solved = true;
        return root;
    }
    
    public List<List<Queen>> solve() {
        solveForTree();
        return root.solutions(new ArrayList<List<Queen>>(), new ArrayList<Queen>());
    }
    
    public static void main(final String[] args) {
        final long start = System.nanoTime();
        final NQueens nQueens = new NQueens(8);
        System.out.println(nQueens.solveForTree().numSolutions());
        final double seconds = (System.nanoTime() - start) / 1e9;
        for (final List<Queen> solution : nQueens.solve()) {
            System.out.println(solution.get(solution.size() - 1));
        }
        System.out.println(seconds + " sec");
        System.out.println("numSolves = " + nQueens.numCalls);
        System.out.println(ChessBitBoard.stats());
    }
    
}
