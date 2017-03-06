
/**
 * dynamic programming solution for Cow Traveling
 * 
 * @author Khyber Sen
 */
public class CowTravelDynamically {
    
    private static final char WALL = '*';
    
    private static final int NUM_MOVES = 4;
    private static final int[] I_MOVES = {1, -1, 0, 0};
    private static final int[] J_MOVES = {0, 0, 1, -1};
    // down, up, right, left in that order
    
    private final char[][] maze;
    
    private final int m;
    private final int n;
    private final int length;
    
    private final int r1;
    private final int c1;
    
    private final int r2;
    private final int c2;
    
    private final int[][][] cache;
    
    public CowTravelDynamically(final char[][] maze, final int m, final int n, final int length,
            final int r1, final int c1, final int r2, final int c2) {
        this.maze = maze;
        this.m = m;
        this.n = n;
        this.length = length;
        this.r1 = r1;
        this.c1 = c1;
        this.r2 = r2;
        this.c2 = c2;
        cache = new int[m][n][length + 1];
    }
    
    private boolean isValidMove(final int i, final int j) {
        return i >= 0 && j >= 0 && i < m && j < n && maze[i][j] != WALL;
    }
    
    private void search() {
        cache[r1][c1][0] = 1;
        for (int t = 1; t <= length; t++) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    cache[i][j][t] = 0;
                    if (maze[i][j] == WALL) {
                        continue;
                    }
                    for (int k = 0; k < NUM_MOVES; k++) {
                        final int nextI = i + I_MOVES[k];
                        final int nextJ = j + J_MOVES[k];
                        if (isValidMove(nextI, nextJ)) {
                            cache[i][j][t] += cache[nextI][nextJ][t - 1];
                        }
                    }
                }
            }
        }
    }
    
    public long numWalks() {
        search();
        return cache[r2][c2][length];
    }
    
}
