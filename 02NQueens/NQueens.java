import java.io.IOException;
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
    private long solutionCount = -1;
    private List<List<Queen>> solutions;
    private List<Queen> solution;
    
    private int numCalls = 0;
    
    public NQueens(final int n) {
        this.n = n;
        root = Queen.root(n);
    }
    
    private void checkSize() {
        if (n > 64) {
            throw new IllegalStateException(
                    "Use of 64-bit longs does not allow boards larger than 64 to be solved. "
                            + "Boards of that size will take millions of years anyways.");
        }
    }
    
    @Deprecated
    private boolean countSolutions(final int i, final long cols, final Queen parent) {
        if (i == n) {
            solutionCount++;
            return true;
        }
        boolean deadend = true;
        for (int j = 0; j < n; j++) {
            final long col = 1 << j;
            if ((cols & col) != col && parent.isValidMove(i, j)) {
                if (countSolutions(i + 1, cols | col, parent.newChild(i, j))) {
                    deadend = true;
                }
            }
        }
        return !deadend;
    }
    
    private boolean countSolutions(final int i, final ChessBoard board) {
        if (i == n) {
            solutionCount++;
            return true;
        }
        if (board.isRowFull(i)) {
            return false;
        }
        boolean deadend = true;
        for (int j = 0; j < n; j++) {
            if (board.isValidMove(i, j)) {
                if (countSolutions(i + 1, board.addQueen(new Queen(n, i, j)))) {
                    deadend = true;
                }
            }
        }
        return !deadend;
    }
    
    //    private long countSolutionsIter2(final int start, ChessBoard board) {
    //        long solutionCount = 0;
    //        final int[] jMoves = new int[n];
    //        final ChessBoard[] boards = new ChessBoard[n];
    //        int i = start;
    //        int j = 0;
    //        for (;;) {
    //            // exit
    //            if (i == start - 1) {
    //                return solutionCount;
    //            }
    //            if (i == n) {
    //                solutionCount++;
    //            }
    //            if (board.isRowFull(i) || j == n) {
    //                // backtrack
    //                i--;
    //                j = jMoves[i];
    //                board = boards[i];
    //            }
    //            if (!board.isValidMove(i, j)) {
    //                continue;
    //            }
    //            // add to stack
    //            jMoves[i] = j;
    //            boards[i] = board;
    //            board = board.addQueen(i, j);
    //            i++;
    //            j++;
    //            continue;
    //        }
    //    }
    //
    //    }
    
    @Deprecated
    private long countSolutionsIter(final int start, ChessBoard board) {
        int i = start;
        long solutionCount = 0;
        final ChessBoard[] boards = new ChessBoard[n];
        boards[i] = board;
        Queen parent = root;
        outer: for (;;) {
            // a solution
            if (i == n) {
                solutionCount++;
            } else if (!board.isRowFull(i)) {
                for (int j = 0; j < n; j++) {
                    if (board.isValidMove(i, j)) {
                        //System.out.println(i + ", " + j);
                        //System.out.println(board);
                        parent = parent.newChild(i, j);
                        System.out.println(parent);
                        try {
                            System.in.read();
                        } catch (final IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        boards[i] = board = board.addQueen(parent);
                        i++;
                        continue outer;
                    }
                }
                // backtrack
                final Queen child = parent;
                parent = child.parent;
                child.delete();
                boards[i] = null;
                i--;
                board = boards[i];
            }
            // completely finished
            if (i == start) {
                return solutionCount;
            }
        }
    }
    
    private void countSolutions() {
        // m is the symmetric version of n
        int m = n >>> 1;
        final ChessBoard board = new LongChessBoard(n);
        int i = 0;
        for (int j = 0; j < m; j++) {
            countSolutions(i + 1, board.addQueen(new Queen(n, i, j)));
            //countSolutions(i + 1, 1L << j, root.newChild(i, j));
        }
        // if n is odd you need to account for the middle column
        if ((n & 1) == 1) {
            final ChessBoard middleBoard = board.addQueen(new Queen(n, i, m));
            i++;
            m--; // exclude position right below and to the left of the top middle
            for (int j = 0; j < m; j++) {
                countSolutions(i + 1, middleBoard.addQueen(new Queen(n, i, j)));
            }
            //countSolutions(i + 1, board.addQueen(new Queen(n, i, m)));
            //countSolutions(i + 1, 1L << m + 1, root.newChild(i, m));
        }
        // double because of symmetry
        solutionCount *= 2;
    }
    
    public long numSolutions() {
        if (solutionCount != -1) {
            return solutionCount;
        }
        checkSize();
        solutionCount++;
        countSolutions();
        //countSolutions(0, new LongChessBoard(n));
        //root.clear();
        return solutionCount;
    }
    
    private boolean solveForTree(final int i, final long cols, final Queen parent) {
        if (i == n) {
            solutionCount++;
            return true;
        }
        boolean deadend = true;
        for (int j = 0; j < n; j++) {
            final long col = 1L << j;
            if ((cols & col) != col && parent.isValidMove(i, j)) {
                final Queen newQueen = parent.newChild(i, j);
                final boolean newDeadend = !solveForTree(i + 1, cols | col, newQueen);
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
    @Deprecated
    private boolean solveForTree(final int i, final ChessBoard board,
            final Queen parent) {
        numCalls++;
        if (i == n) {
            solutionCount++;
            return true;
        }
        if (board.isRowFull(i)) {
            return false;
        }
        boolean deadend = true;
        for (int j = 0; j < n; j++) {
            if (board.isValidMove(i, j)) {
                final Queen newQueen = parent.newChild(i, j);
                final boolean newDeadend = //
                        !solveForTree(i + 1, board.addQueen(newQueen), newQueen);
                board.removeQueen(newQueen);
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
        checkSize();
        solutionCount++;
        //solveForTree(0, new LongChessBoard(n), root);
        solveForTree(0, 0L, root);
        solved = true;
        return root;
    }
    
    private List<List<Queen>> solutions(final boolean solve) {
        if (solutions != null) {
            return solutions;
        }
        if (solve) {
            solutionTree();
        }
        solutions = new ArrayList<>();
        root.solutions(solutions, new ArrayList<Queen>());
        if (solutions.size() == 0) {
            solution = null;
        } else {
            solution = solutions.get(0);
        }
        return solutions;
    }
    
    public List<List<Queen>> solutions() {
        return solutions(true);
    }
    
    @Deprecated
    private boolean solveForFirstSolution(final int i, final long cols, final Queen parent) {
        if (i == n) {
            return true;
        }
        for (int j = 0; j < n; j++) {
            final long col = 1L << j;
            if ((cols & col) != col && parent.isValidMove(i, j)) {
                final Queen newQueen = parent.newChild(i, j);
                if (solveForFirstSolution(i + 1, cols | col, newQueen)) {
                    return true;
                } else {
                    newQueen.delete();
                }
            }
        }
        return false;
    }
    
    private boolean solveForFirstSolution(final int i, final ChessBoard board,
            final Queen parent) {
        numCalls++;
        if (i == n) {
            return true;
        }
        if (board.isRowFull(i)) {
            return false;
        }
        for (int j = 0; j < n; j++) {
            if (board.isValidMove(i, j)) {
                final Queen newQueen = parent.newChild(i, j);
                if (solveForFirstSolution(i + 1, board.addQueen(newQueen), newQueen)) {
                    return true;
                } else {
                    newQueen.delete();
                }
            }
        }
        return false;
    }
    
    public List<Queen> solution() {
        if (solved) {
            solutions(false);
            return solution;
        }
        final ChessBoard board = n <= 64 ? new LongChessBoard(n) : new BigChessBoard(n);
        solveForFirstSolution(0, board, root);
        //solveForFirstSolution(0, 0L, root);
        solutions(false);
        solutions = null;
        root.clear();
        return solution;
    }
    
    public static void numSolutionsTest(final int n) {
        final long start = System.nanoTime();
        final NQueens nQueens = new NQueens(n);
        System.out.println(nQueens.numSolutions());
        final double seconds = (System.nanoTime() - start) / 1e9;
        System.out.println(seconds + " sec");
    }
    
    public static void allSolutionsTest(final int n, final boolean print) {
        final long start = System.nanoTime();
        final NQueens nQueens = new NQueens(n);
        System.out.println(nQueens.solutionTree().numSolutions());
        final double seconds = (System.nanoTime() - start) / 1e9;
        if (print) {
            for (final List<Queen> solution : nQueens.solutions()) {
                System.out.println(solution.get(n));
            }
        }
        System.out.println(nQueens.solutionTree().numSolutions());
        System.out.println(seconds + " sec");
        System.out.println("numSolves = " + nQueens.numCalls);
        System.out.println(ChessBoard.stats());
    }
    
    public static void oneSolutionTest(final int n) {
        final long start = System.nanoTime();
        final NQueens nQueens = new NQueens(n);
        final List<Queen> solution = nQueens.solution();
        System.out.println(solution.get(n));
        final double seconds = (System.nanoTime() - start) / 1e9;
        System.out.println(seconds + " sec");
        System.out.println("numSolves = " + nQueens.numCalls);
        System.out.println(ChessBoard.stats());
    }
    
    public static void main(final String[] args) {
        //oneSolutionTest(30);
        //allSolutionsTest(15, false);
        allSolutionsTest(8, true);
        //numSolutionsTest(16);
        //allSolutionsTest(9, true);
        final int n = 2;
        final NQueens nq = new NQueens(n);
        //System.out.println(nq.countSolutionsIter2(0, new LongChessBoard(n)));
    }
    
}
