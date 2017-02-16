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
    
    public abstract boolean findTour();
    
    public boolean verify() {
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
        kt.findTour();
        final double seconds = (System.nanoTime() - start) / 1e9;
        if (print && m < 100 && n < 100) {
            System.out.println(kt);
        }
        kt.verify();
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
