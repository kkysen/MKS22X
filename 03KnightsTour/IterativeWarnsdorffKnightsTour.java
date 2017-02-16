import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class IterativeWarnsdorffKnightsTour extends WarnsdorffKnightsTour {
    
    private final int[][] weightsStack;
    private final int[] kStack;
    
    private int i;
    private int j;
    private int k;
    private int[] weights;
    
    public IterativeWarnsdorffKnightsTour(final int m, final int n) {
        super(m, n);
        weightsStack = new int[mn][];
        kStack = new int[mn];
    }
    
    public IterativeWarnsdorffKnightsTour(final int n) {
        this(n, n);
    }
    
    private void move() {
        //System.out.println(moveNum + 1 + ": " + i + ", " + j);
        // update Warnsdorff weights
        for (int k = 0; k < NUM_MOVES; k++) {
            board[i + I_MOVES[k] + 2][j + J_MOVES[k] + 2]--;
        }
        iMoves[moveNum] = i;
        jMoves[moveNum] = j;
        kStack[moveNum] = k;
        weightsStack[moveNum] = weights;
        moveNum++;
        moves[i + 2][j + 2] = moveNum;
        k = 0;
        weights = getWeights();
    }
    
    private void move(final int move) {
        i += I_MOVES[move];
        j += J_MOVES[move];
        move();
    }
    
    private void undo() {
        //System.err.println("undo");
        moves[i + 2][j + 2] = 0;
        // update Warnsdorff weights
        for (int k = 0; k < NUM_MOVES; k++) {
            board[i + 2 + I_MOVES[k]][j + 2 + J_MOVES[k]]++;
        }
        //weightsStack[moveNum] = null; // not sure if memory release needed
        moveNum--;
        i = iMoves[moveNum];
        j = jMoves[moveNum];
        k = kStack[moveNum];
        weights = weightsStack[moveNum];
    }
    
    private int[] getWeights() {
        final int[] weights = new int[NUM_MOVES];
        for (int k = 0; k < NUM_MOVES; k++) {
            final int nextI = i + 2 + I_MOVES[k];
            final int nextJ = j + 2 + J_MOVES[k];
            int weight;
            if (moves[nextI][nextJ] != 0) {
                weight = VISITED;
            } else {
                weight = board[nextI][nextJ];
            }
            weights[k] = weight == 0 ? DEADEND : weight;
        }
        return weights;
    }
    
    // one run of a lazy selection sort
    private int bestMove() {
        int min = weights[k];
        int minIndex = k;
        for (int i = k + 1; i < weights.length; i++) {
            final int cur = weights[i];
            if (cur < min) {
                min = cur;
                minIndex = i;
            }
        }
        weights[minIndex] = weights[k];
        weights[k] = min;
        k++;
        return minIndex;
    }
    
    private boolean findTourIter() {
        move();
        final int m = this.m;
        final int n = this.n;
        final int[][] board = this.board;
        final int[][] moves = this.moves;
        final KnightsTour kt = this;
        for (;;) {
            if (moveNum == 68) {
                System.out.println(toStringIgnoreSolve());
                final int x = 1;
            }
            if (moveNum == mn) {
                return true;
            }
            if (k == NUM_MOVES) {
                undo();
                if (moveNum == -1) {
                    return false;
                }
            }
            final int move = bestMove();
            if (weights[0] == VISITED) {
                continue;
            }
            move(move);
        }
    }
    
    @Override
    public boolean findTour() {
        if (solved) {
            return true;
        }
        for (; i < m; i++) {
            for (; j < n; j++) {
                if (findTourIter()) {
                    solved = true;
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void test(final int n) throws IOException {
        KnightsTour.test(new IterativeWarnsdorffKnightsTour(n));
    }
    
    public static void testUpTo(final int n) throws FileNotFoundException {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                try {
                    KnightsTour.test(new IterativeWarnsdorffKnightsTour(i, j), false);
                } catch (final RuntimeException e) {
                    System.out.println(i + "x" + j);
                }
            }
        }
    }
    
    public static void main(final String[] args) throws IOException {
        //        test(2);
        //        test(3);
        //        test(4);
        //        for (int n = 5; n < 64; n++) {
        //            test(n);
        //        }
        //test(63);
        //test(66);
        //testUpTo(25);
        //test(5000);
        KnightsTour.test(new IterativeWarnsdorffKnightsTour(5, 14));
    }
    
}
