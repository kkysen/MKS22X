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
    private List<List<Queen>> solutions;
    
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
     * @implNote
     *           cols is a long bit set. 64 bits is sufficiently large for the
     *           N-Queens problem and is pretty much unsolvable at that point.
     *           Even a 32-bit int should be large enough.
     */
    private boolean solveForTree(final int i, final ChessBitBoard board,
            final Queen parent) {
        numCalls++;
        if (i == n) {
            return true;
        }
        boolean deadend = true;
        for (int j = 0; j < n; j++) {
            if (board.isValidMove(i, j)) {
                final Queen newQueen = parent.newChild(i, j);
                final boolean newDeadend = //
                        !solveForTree(i + 1, board.addQueen(newQueen), newQueen);
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
    
    public Queen solutionTree() {
        if (solved) {
            return root;
        }
        solveForTree(0, new LongChessBitBoard(n), root);
        solved = true;
        return root;
    }
    
    public List<List<Queen>> solutions() {
        if (solutions != null) {
            return solutions;
        }
        solutionTree();
        solutions = new ArrayList<>();
        return root.solutions(solutions, new ArrayList<Queen>());
    }
    
    private boolean solveForFirstSolution(final int i, final ChessBitBoard board,
            final Queen parent) {
        return true;
    }
    
    public List<Queen> solution() {
        if (solved) {
            solutions();
            if (solutions.size() == 0) {
                return null;
            }
            return solutions.get(0);
        }
        return null;
    }
    
    public static void main(final String[] args) {
        final long start = System.nanoTime();
        final NQueens nQueens = new NQueens(8);
        System.out.println(nQueens.solutionTree().numSolutions());
        final double seconds = (System.nanoTime() - start) / 1e9;
        for (final List<Queen> solution : nQueens.solutions()) {
            System.out.println(solution.get(solution.size() - 1));
        }
        System.out.println(nQueens.solutionTree().numSolutions());
        System.out.println(seconds + " sec");
        System.out.println("numSolves = " + nQueens.numCalls);
        System.out.println(ChessBitBoard.stats());
    }
    
}
