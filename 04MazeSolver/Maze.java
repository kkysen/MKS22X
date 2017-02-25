import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class Maze {
    
    protected static final char WALL = '#';
    protected static final char EMPTY = ' ';
    protected static final char START = 'S';
    protected static final char END = 'E';
    protected static final char PATH = '\u25A0';
    
    protected static final int NUM_MOVES = 4;
    protected static final int[] I_MOVES = {1, -1, 0, 0};
    protected static final int[] J_MOVES = {0, 0, 1, -1};
    
    protected final int m;
    protected final int n;
    protected final int maxLength;
    
    protected final char[][] maze;
    
    protected final int startI;
    protected final int startJ;
    
    protected final int[] iMoves;
    protected final int[] jMoves;
    
    private boolean solved;
    private boolean unsolveable;
    
    private void checkNoMaze(final int dimension) {
        if (dimension == 0) {
            throw new IllegalArgumentException("no maze");
        }
    }
    
    protected Maze(final char[][] maze) {
        m = maze.length;
        checkNoMaze(m);
        n = maze[0].length;
        checkNoMaze(n);
        this.maze = maze;
        
        int emptyCount = 0;
        int tempStartI = -1;
        int tempStartJ = -1;
        boolean hasEnd = false;
        
        for (int i = 0; i < m; i++) {
            if (maze[i].length != n) {
                throw new IllegalArgumentException("jagged maze");
            }
            if (maze[i][0] != WALL || maze[i][n - 1] != WALL) {
                throw new IllegalArgumentException("open maze");
            }
            if (i == 0 || i == m - 1) {
                for (int j = 0; j < n; j++) {
                    if (maze[i][j] != WALL) {
                        throw new IllegalArgumentException("open maze");
                    }
                }
            }
            for (int j = 0; j < n; j++) {
                final char c = maze[i][j];
                if (c == START) {
                    tempStartI = i;
                    tempStartJ = j;
                } else if (c == END) {
                    hasEnd = true;
                } else if (c != WALL) {
                    emptyCount++;
                }
            }
        }
        
        if (tempStartI == -1) {
            throw new IllegalArgumentException("missing start");
        }
        
        if (!hasEnd) {
            throw new IllegalArgumentException("missing end");
        }
        
        if (emptyCount == 0) {
            unsolveable = true;
        }
        
        maxLength = emptyCount;
        startI = tempStartI;
        startJ = tempStartJ;
        
        iMoves = new int[maxLength];
        jMoves = new int[maxLength];
    }
    
    private static char[][] readAsCharMatrix(final Path path) throws IOException {
        final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        final char[][] charMatrix = new char[lines.size()][];
        for (int i = 0; i < charMatrix.length; i++) {
            charMatrix[i] = lines.get(i).toCharArray();
        }
        return charMatrix;
    }
    
    protected Maze(final Path path) throws IOException {
        this(readAsCharMatrix(path));
    }
    
    protected abstract boolean findAnyPath();
    
    public final int[][] anyPath() {
        if (solved) {
            return new int[][] {iMoves, jMoves};
        }
        if (findAnyPath()) {
            solved = true;
            return anyPath();
        }
        unsolveable = true;
        return null;
    }
    
    protected abstract boolean aStarPath();
    
    protected boolean findShortestPath() {
        return aStarPath();
    }
    
    public final int[][] shortestPath() {
        
        unsolveable = true;
        return null;
    }
    
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder(m * n);
        for (final char[] row : maze) {
            sb.append(row);
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
}
