import java.io.FileNotFoundException;
import java.util.Arrays;

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
    
    @Override
    protected void initMoves() {
        Arrays.fill(moves[0], -1);
        Arrays.fill(moves[1], -1);
        Arrays.fill(moves[realM - 2], -1);
        Arrays.fill(moves[realM - 1], -1);
        for (int i = 2; i < realM - 2; i++) {
            final int[] row = moves[i];
            row[0] = row[1] = row[realN - 2] = row[realN - 1] = -1;
        }
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
