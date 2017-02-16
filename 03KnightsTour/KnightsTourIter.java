import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class KnightsTourIter {
    
    private static final int VISITED = Integer.MAX_VALUE;
    private static final int DEADEND = VISITED - 1;
    
    private static final int NUM_MOVES = 8;
    private static final int[] I_MOVES = {2, 1, -1, -2, -2, -1, 1, 2};
    private static final int[] J_MOVES = {1, 2, 2, 1, -1, -2, -2, -1};
    
    private final int m;
    private final int n;
    
    private final int mn;
    
    private final int realM;
    private final int realN;
    
    private final int[][] moves;
    private final int[][] board;
    
    private int moveNum = 0;
    private final int[] iMoves;
    private final int[] jMoves;
    private boolean solved;
    
    public KnightsTourIter(final int m, final int n) {
        this.m = m;
        this.n = n;
        mn = m * n;
        realM = m + 4;
        realN = n + 4;
        moves = new int[realM][realN];
        initVisited();
        board = new int[realM][realN];
        initBoard();
        iMoves = new int[mn];
        jMoves = new int[mn];
    }
    
    public KnightsTourIter(final int n) {
        this(n, n);
    }
    
    private void initVisited() {
        final int[] edgeRow = new int[realN];
        final int[] middleRow = new int[realN];
        
        Arrays.fill(edgeRow, 1);
        middleRow[0] = middleRow[1] = middleRow[realN - 2] = middleRow[realN - 1] = 1;
        
        // ok if array not cloned b/c these rows aren't changing
        moves[0] = moves[1] = moves[realM - 2] = moves[realM - 1] = edgeRow;
        
        for (int i = 0; i < m; i++) {
            moves[i + 2] = middleRow.clone();
        }
    }
    
    private void initBoard() {
        final int[][] rows = new int[3][realN];
        final int[] firstRow = rows[0];
        final int[] secondRow = rows[1];
        final int[] middleRow = rows[2];
        
        Arrays.fill(firstRow, 4);
        Arrays.fill(secondRow, 6);
        Arrays.fill(middleRow, 8);
        
        firstRow[2] = firstRow[n + 1] = 2;
        firstRow[3] = firstRow[n] = 3;
        
        secondRow[2] = secondRow[n + 1] = 3;
        secondRow[3] = secondRow[n] = 4;
        
        middleRow[2] = middleRow[n + 1] = 4;
        middleRow[3] = middleRow[n] = 6;
        
        for (final int[] row : rows) {
            for (final int i : new int[] {0, 1, realN - 2, realN - 1}) {
                row[i] = 0;
            }
        }
        
        for (int i = 2; i < realM - 2; i++) {
            board[i] = middleRow.clone();
        }
        
        board[2] = firstRow;
        board[3] = secondRow;
        board[m] = secondRow.clone();
        board[m + 1] = firstRow.clone();
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
        System.err.println("undo");
        if (1 == 1) {
            //throw new RuntimeException();
        }
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
    
    private boolean findTourIter(final int i, final int j) {
        final int[][] weightsStack = new int[mn][];
        final int[] kStack = new int[mn];
        final boolean backtracking = false;
        int[] weights;
        int k;
        move(i, j);
        for (;;) {
            if (moveNum == mn) {
                return true;
            }
            weights = getWeights(i, j);
            k = 0;
            final int move = bestMove(weights, k);
        }
    }
    
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
    
    private static String padLeft(final String s, final int length) {
        final char[] pad = new char[length - s.length()];
        Arrays.fill(pad, ' ');
        return s + new String(pad);
    }
    
    private static String padLeft(final int i, final int length) {
        return padLeft(String.valueOf(i), length);
    }
    
    @Override
    public String toString() {
        if (!solved) {
            return "";
        }
        final int padLength = String.valueOf(mn).length();
        final StringBuilder sb = new StringBuilder((padLength + 1) * mn);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(padLeft(moves[i + 2][j + 2], padLength));
                sb.append(' ');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    public static void main(final String[] args) {
        final long start = System.nanoTime();
        final KnightsTourIter kt = new KnightsTourIter(83);
        kt.findTour();
        final double seconds = (System.nanoTime() - start) / 1e9;
        System.out.println(kt);
        System.out.println(seconds + " sec");
    }
    
}
