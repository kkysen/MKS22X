import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class KnightsTour {
    
    private static final int NUM_MOVES = 8;
    private static final int[] I_MOVES = {2, 1, -1, -2, -2, -1, 1, 2};
    private static final int[] J_MOVES = {1, 2, 2, 1, -1, -2, -2, -1};
    
    private final int m;
    private final int n;
    private final int mn;
    
    private final int realM;
    private final int realN;
    
    private final boolean[][] board;
    
    //private final long[] rows;
    
    //private final long sideMask;
    
    private int moveNum = 0;
    private final int[] iMoves;
    private final int[] jMoves;
    
    private void initBoard() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i + 2][j + 2] = true;
            }
        }
    }
    
    private void checkForNoSolution(final int m, final int n) {
        if ((m & 1) == 1 && (n & 1) == 1
                || m == 1 || m == 2 || m == 4
                || m == 3 && (n == 4 || n == 6 || n == 8)) {
            System.err.println("no solution exists");
        }
    }
    
    public KnightsTour(final int m, final int n) {
        checkForNoSolution(m, n);
        checkForNoSolution(n, m);
        this.m = m;
        this.n = n;
        mn = m * n;
        realM = m + 4;
        realN = n + 4;
        board = new boolean[realM][realN]; // border avoids bounds checking
        initBoard();
        iMoves = new int[mn];
        jMoves = new int[mn];
    }
    
    public KnightsTour(final int n) {
        this(n, n);
    }
    
    private boolean isValidMove(final int i, final int j) {
        return board[i + 2][j + 2];
    }
    
    private void move(final int i, final int j) {
        board[i + 2][j + 2] = false;
        iMoves[moveNum] = i;
        jMoves[moveNum] = j;
        moveNum++;
    }
    
    private void undo(final int i, final int j) {
        board[i + 2][j + 2] = true;
        moveNum--;
    }
    
    public boolean findTour(final int i, final int j) {
        if (moveNum == mn) {
            return true;
        }
        if (moveNum == 0) {
            //move(i, j);
        }
        for (int k = 0; k < NUM_MOVES; k++) {
            final int nextI = i + I_MOVES[k];
            final int nextJ = j + J_MOVES[k];
            if (isValidMove(nextI, nextJ)) {
                move(nextI, nextJ);
                if (findTour(nextI, nextJ)) {
                    return true;
                } else {
                    undo(nextI, nextJ);
                }
            }
        }
        return false;
    }
    
    public boolean findTour() {
        return findTour(0, 0);
    }
    
    private static String padLeft(final String s, final int length) {
        final char[] pad = new char[length - s.length()];
        Arrays.fill(pad, ' ');
        return s + new String(pad);
    }
    
    private static String padLeft(final int i, final int length) {
        return padLeft(String.valueOf(i), length);
    }
    
    private static StringBuilder join(final String delimiter, final String[] strings) {
        final StringBuilder sb = new StringBuilder(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            sb.append(delimiter);
            sb.append(strings[i]);
        }
        return sb;
    }
    
    private static StringBuilder join(final String rowDelimiter, final String columnDelimiter,
            final String[][] strings) {
        final StringBuilder sb = new StringBuilder(join(columnDelimiter, strings[0]));
        for (final String[] string : strings) {
            sb.append(rowDelimiter);
            sb.append(join(columnDelimiter, string));
        }
        return sb;
    }
    
    @Override
    public String toString() {
        System.out.println(Arrays.toString(iMoves));
        System.out.println(Arrays.toString(jMoves));
        final String[][] solution = new String[m][n];
        final int padLength = String.valueOf(mn).length();
        for (int k = 0; k < mn; k++) {
            solution[iMoves[k]][jMoves[k]] = padLeft(k + 1, padLength);
        }
        return join(" ", "\n", solution).toString();
    }
    
    public static void main(final String[] args) {
        final KnightsTour kt = new KnightsTour(5, 6);
        kt.findTour();
        System.out.println(kt);
    }
    
}
