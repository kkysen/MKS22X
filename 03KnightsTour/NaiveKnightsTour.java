import java.io.FileNotFoundException;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class NaiveKnightsTour extends KnightsTour {
    
    public NaiveKnightsTour(final int m, final int n) {
        super(m, n);
    }
    
    public NaiveKnightsTour(final int n) {
        this(n, n);
    }
    
    private boolean isValidMove(final int i, final int j) {
        return moves[i + 2][j + 2] == 0;
    }
    
    private void move(final int i, final int j) {
        iMoves[moveNum] = i;
        jMoves[moveNum] = j;
        moveNum++;
        moves[i + 2][j + 2] = moveNum;
    }
    
    private void undo(final int i, final int j) {
        moves[i + 2][j + 2] = 0;
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
    
    @Override
    public boolean findTour() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                move(i, j);
                if (findTour(i, j)) {
                    return true;
                }
                undo(i, j);
            }
        }
        return false;
    }
    
    public static void main(final String[] args) throws FileNotFoundException {
        KnightsTour.test(new NaiveKnightsTour(5));
    }
    
}
