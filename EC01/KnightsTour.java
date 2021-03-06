import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class KnightsTour {
    
    protected static final int NUM_MOVES = 8;
    protected static final int[] I_MOVES = {2, 1, -1, -2, -2, -1, 1, 2};
    protected static final int[] J_MOVES = {1, 2, 2, 1, -1, -2, -2, -1};
    
    protected final int m;
    protected final int n;
    protected final int mn;
    
    protected final int realM;
    protected final int realN;
    
    protected final int[][] moves;
    
    protected int moveNum = 0;
    protected final int[] iMoves;
    protected final int[] jMoves;
    
    protected boolean solved;
    protected boolean unsolveable;
    
    protected KnightsTour(final int m, final int n) {
        this.m = m;
        this.n = n;
        mn = m * n;
        realM = m + 4;
        realN = n + 4;
        moves = new int[realM][realN]; // border avoids bounds checking
        initMoves();
        iMoves = new int[mn];
        jMoves = new int[mn];
    }
    
    protected void initMoves() {}
    
    public int tourLength() {
        return mn;
    }
    
    public int getHeight() {
        return m;
    }
    
    public int getWidth() {
        return n;
    }
    
    protected abstract boolean findTour();
    
    protected void clearInternalState() {
        Arrays.fill(iMoves, 0);
        Arrays.fill(jMoves, 0);
    }
    
    public boolean solve() {
        if (solved) {
            return true;
        }
        if (unsolveable) {
            return false;
        }
        if (findTour()) {
            solved = true;
            return true;
        }
        clearInternalState();
        unsolveable = true;
        return false;
    }
    
    // quick verify w/ no final debug info
    public boolean quickVerify() {
        if (unsolveable) {
            System.out.println("no solution");
            return true;
        }
        int sum = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sum += moves[i + 2][j + 2];
            }
        }
        return sum == mn * (mn + 1) / 2; // triangular number
    }
    
    public boolean verify() {
        if (unsolveable) {
            System.out.println("no solution");
            return true;
        }
        final boolean[][] board = new boolean[m][n];
        for (int moveNum = 0; moveNum < mn; moveNum++) {
            final int i = iMoves[moveNum];
            final int j = jMoves[moveNum];
            if (board[i][j] == true) {
                if (m < 100 && n < 100) {
                    System.err.println(this);
                }
                System.err.println("invalid move #" + moveNum + ": " + i + ", " + j);
                //throw new IllegalStateException("invalid solution");
                return false;
            }
            board[i][j] = true;
        }
        System.out.println("solution verified");
        return true;
    }
    
    private static String padLeft(final String s, final int length) {
        final char[] pad = new char[length - s.length()];
        Arrays.fill(pad, ' ');
        return s + new String(pad);
    }
    
    private static String padLeft(final int i, final int length) {
        return padLeft(String.valueOf(i), length);
    }
    
    protected String toStringIgnoreSolve() {
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
    
    @Override
    public String toString() {
        if (!solved) {
            return "";
        }
        return toStringIgnoreSolve();
    }
    
    public static void test(final KnightsTour kt, final boolean print)
            throws FileNotFoundException {
        final int m = kt.getHeight();
        final int n = kt.getWidth();
        final long start = System.nanoTime();
        kt.solve();
        final double seconds = (System.nanoTime() - start) / 1e9;
        final int maxPrintSize = 150;
        if (print && m < maxPrintSize && n < maxPrintSize) {
            System.out.println(kt);
        }
        kt.verify();
        if (!kt.quickVerify()) {
            System.out.println("not verified");
        }
        final String time = seconds + " sec";
        final PrintStream out = new PrintStream("C:/Users/kkyse/Downloads/KnightsTourOut.txt");
        out.println("Knight's Tour");
        out.println(m + "x" + n + " board");
        out.println();
        out.println(time);
        out.println(kt);
        out.close();
        if (print) {
            System.out.println("Knight's Tour");
            System.out.println(m + "x" + n + " board");
            System.out.println(time);
        }
    }
    
    public static void test(final KnightsTour kt) throws FileNotFoundException {
        test(kt, true);
    }
    
}
