
/**
 * 
 * 
 * @author Khyber Sen
 */
public class KnightsTour {
    
    private static final int NUM_MOVES = 8;
    private static final int[] I_MOVES = {2, 1, -1, -2, -2, -1, 1, 2};
    private static final int[] J_MOVES = {1, 2, 2, 1, -1, -2, -2, -1};
    
    private final int n;
    private final int nn;
    private final int m; // size of actual board w/ border
    
    private final long[] rows;
    
    private final long sideMask;
    
    private int moveNum = 0;
    private final int[] iMoves;
    private final int[] jMoves;
    private final ChessBoard board;
    
    public KnightsTour(final int n) {
        this.n = n;
        nn = n * n;
        m = n + 4;
        rows = new long[m]; // border avoids bounds checking
        sideMask = 0b11L | 0b11L << m - 2;
        initBorder();
        iMoves = new int[nn];
        jMoves = new int[nn];
        board = new LongChessTour(n);
    }
    
    private void initBorder() {
        // fill top and bottom
        rows[0] = -1L;
        rows[1] = -1L;
        rows[m - 1] = -1L;
        rows[m - 2] = -1L;
        // fill sides
        for (int i = 2; i < m - 2; i++) {
            rows[i] = sideMask;
        }
    }
    
    private boolean isValidMove(final int i, final int j) {
        return (rows[i + 2] >>> j + 2 & 1) == 0;
    }
    
    private void move(final int i, final int j) {
        rows[i + 2] |= 1L << j + 2;
        iMoves[moveNum] = i;
        jMoves[moveNum] = j;
        board.set(i, j);
        moveNum++;
    }
    
    private void clear(final int i, final int j) {
        rows[i + 2] ^= 1L << j + 2;
        board.flip(i, j);
        moveNum--;
        // no need to clear prev moves, just overwrite
    }
    
    public boolean findTour(final int i, final int j) {
        if (moveNum == nn) {
            return true;
        }
        for (int k = 0; k < NUM_MOVES; k++) {
            final int nextI = i + I_MOVES[k];
            final int nextJ = j + J_MOVES[k];
            if (isValidMove(nextI, nextJ)) {
                move(nextI, nextJ);
                if (findTour(nextI, nextJ)) {
                    return true;
                } else {
                    clear(nextI, nextJ);
                }
            }
        }
        return false;
    }
    
    public boolean solve(int i, int j) {
        //        int[] moves = new int[nn];
        //        int k = 0;
        //        move(i, j);
        //        for (;;) {
        //            if (moveNum == nn) {
        //                return true;
        //            }
        //            if (k == NUM_MOVES) {
        //                if (moveNum == 0) {
        //                    return false;
        //                }
        //                clear(i, j);
        //                i = iMoves[moveNum];
        //                j = jMoves[moveNum];
        //            }
        //
        //        }
        //
        //
        //
        
        // FIXME
        // must have k stack
        // clear not clearing bits
        
        final ChessBoard aBoard = board;
        final KnightsTour tour = this;
        final int[] iiMoves = iMoves;
        final int[] jjMoves = jMoves;
        move(i, j);
        outer: for (;;) {
            // exit when successful
            if (moveNum == nn) {
                return true;
            }
            if (i == 1 && j == 0) {
                i++;
                i--;
            }
            for (int k = 0; k < NUM_MOVES; k++) {
                final int iMove = I_MOVES[k];
                final int jMove = J_MOVES[k];
                i += iMove;
                j += jMove;
                if (isValidMove(i, j)) {
                    // add to stack
                    move(i, j);
                    continue outer;
                }
                // set i, j to original vals
                i -= iMove;
                j -= jMove;
            }
            // exit when failed
            if (moveNum == 0) {
                return false;
            }
            // backtrack
            clear(i, j);
            moveNum--;
            i = iMoves[moveNum];
            j = jMoves[moveNum];
            moveNum++;
        }
    }
    
    public void printTour() {
        final ChessBoard board = new LongChessTour(n);
        for (int k = 0; k < nn; k++) {
            final int iMove = iMoves[k];
            final int jMove = jMoves[k];
            //System.out.println(i + ", " + j);
            board.set(iMove, jMove);
            System.out.println(k + "\n" + board + "\n");
        }
    }
    
    public static void main(final String[] args) {
        final KnightsTour tour = new KnightsTour(5);
        tour.solve(0, 0);
        tour.printTour();
    }
    
}
