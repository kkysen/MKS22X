
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
    
    private final int height;
    private final int width;
    private final int length;
    
    private final int startI;
    private final int startJ;
    
    private final int endI;
    private final int endJ;
    
    private final int[][][] cache;
    
    public CowTravelDynamically(final char[][] maze, final int height, final int width,
            final int length,
            final int startI, final int startJ, final int endI, final int endJ) {
        this.maze = maze;
        this.height = height;
        this.width = width;
        this.length = length;
        this.startI = startI;
        this.startJ = startJ;
        this.endI = endI;
        this.endJ = endJ;
        cache = new int[height][width][length + 1];
    }
    
    private boolean isValidMove(final int i, final int j) {
        return i >= 0 && j >= 0 && i < height && j < width && maze[i][j] != WALL;
    }
    
    private void search() {
        cache[startI][startJ][0] = 1;
        for (int t = 1; t <= length; t++) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    cache[i][j][t] = 0;
                    if (maze[i][j] == WALL) {
                        continue;
                    }
                    for (int k = 0; k < NUM_MOVES; k++) {
                        final int prevI = i + I_MOVES[k];
                        final int prevJ = j + J_MOVES[k];
                        if (isValidMove(prevI, prevJ)) {
                            cache[i][j][t] += cache[prevI][prevJ][t - 1];
                        }
                    }
                }
            }
        }
    }
    
    public long numWalks() {
        search();
        return cache[endI][endJ][length];
    }
    
}
