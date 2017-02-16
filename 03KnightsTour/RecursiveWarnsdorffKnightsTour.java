import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class RecursiveWarnsdorffKnightsTour extends WarnsdorffKnightsTour {
    
    public RecursiveWarnsdorffKnightsTour(final int m, final int n) {
        super(m, n);
    }
    
    public RecursiveWarnsdorffKnightsTour(final int n) {
        this(n, n);
    }
    
    private void move(final int i, final int j) {
        //System.out.println(moveNum + 1 + ": " + i + ", " + j);
        // update Warnsdorff weights
        for (int k = 0; k < NUM_MOVES; k++) {
            board[i + I_MOVES[k] + 2][j + J_MOVES[k] + 2]--;
        }
        iMoves[moveNum] = i;
        jMoves[moveNum] = j;
        moveNum++;
        moves[i + 2][j + 2] = moveNum;
    }
    
    private void undo(int i, int j) {
        i += 2;
        j += 2;
        moves[i][j] = 0;
        // update Warnsdorff weights
        for (int k = 0; k < NUM_MOVES; k++) {
            board[i + I_MOVES[k]][j + J_MOVES[k]]++;
        }
        moveNum--;
    }
    
    private int[] getWeights(final int i, final int j) {
        final int[] weights = new int[NUM_MOVES];
        for (int k = 0; k < NUM_MOVES; k++) {
            final int nextI = i + I_MOVES[k] + 2;
            final int nextJ = j + J_MOVES[k] + 2;
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
    private int bestMove(final int[] weights, final int startIndex) {
        int min = weights[startIndex];
        int minIndex = startIndex;
        for (int i = startIndex + 1; i < weights.length; i++) {
            final int cur = weights[i];
            if (cur < min) {
                min = cur;
                minIndex = i;
            }
        }
        weights[minIndex] = weights[startIndex];
        weights[startIndex] = min;
        return minIndex;
    }
    
    private boolean findTour(final int i, final int j) {
        if (moveNum == mn) {
            return true;
        }
        final int[] weights = getWeights(i, j);
        for (int k = 0; k < weights.length; k++) {
            final int move = bestMove(weights, k);
            if (weights[0] == VISITED) {
                continue;
            }
            final int nextI = i + I_MOVES[move];
            final int nextJ = j + J_MOVES[move];
            // already checked for validity
            move(nextI, nextJ);
            if (findTour(nextI, nextJ)) {
                return true;
            }
            undo(nextI, nextJ);
        }
        return false;
    }
    
    @Override
    public boolean findTour() {
        if (solved) {
            return true;
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                move(i, j);
                if (findTour(i, j)) {
                    solved = true;
                    return true;
                }
                undo(i, j);
            }
        }
        return false;
    }
    
    public static void test(final int n) throws IOException {
        KnightsTour.test(new RecursiveWarnsdorffKnightsTour(n));
    }
    
    public static void test(final int m, final int n) throws FileNotFoundException {
        KnightsTour.test(new RecursiveWarnsdorffKnightsTour(m, n));
    }
    
    public static void main(final String[] args) throws IOException {
        //        test(2);
        //        test(3);
        //        test(4);
        //        for (int n = 5; n < 64; n++) {
        //            test(n);
        //        }
        //test(63);
        test(5, 14);
    }
    
}
