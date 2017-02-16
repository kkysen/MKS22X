import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class WarnsdorffKnightsTour extends KnightsTour {
    
    protected static final int VISITED = Integer.MAX_VALUE;
    protected static final int DEADEND = VISITED - 1;
    
    protected final int[][] board;
    
    public WarnsdorffKnightsTour(final int m, final int n) {
        super(m, n);
        board = new int[realM][realN];
        initBoard();
    }
    
    @Override
    protected void initMoves() {
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
    
    protected void initBoard() {
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
    
}
