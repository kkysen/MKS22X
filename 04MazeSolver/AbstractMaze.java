import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class AbstractMaze {
    
    protected static final char WALL = '#';
    protected static final char EMPTY = ' ';
    protected static final char START = 'S';
    protected static final char END = 'E';
    protected static final char PATH = '@';
    protected static final char VISITED = '.';
    
    protected static final int NUM_MOVES = 4;
    protected static final int[] I_MOVES = {1, -1, 0, 0};
    protected static final int[] J_MOVES = {0, 0, 1, -1};
    // down, up, right, left in that order
    
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
    
    protected boolean animate;
    
    private void checkNoMaze(final int dimension) {
        if (dimension == 0) {
            throw new IllegalArgumentException("no maze");
        }
    }
    
    protected AbstractMaze(final char[][] maze) {
        m = maze.length;
        checkNoMaze(m);
        n = maze[0].length;
        checkNoMaze(n);
        this.maze = maze;
        
        int emptyCount = 0;
        int tempStartI = -1;
        int tempStartJ = -1;
        int numStarts = 0;
        int numEnds = 0;
        
        for (int i = 0; i < m; i++) {
            if (maze[i].length != n) {
                throw new IllegalArgumentException("jagged maze: row " + i);
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
                    numStarts++;
                    tempStartI = i;
                    tempStartJ = j;
                } else if (c == END) {
                    numEnds++;
                } else if (c != WALL) {
                    emptyCount++;
                }
            }
        }
        
        if (numStarts == 0) {
            throw new IllegalArgumentException("missing start");
        }
        
        if (numStarts > 1) {
            throw new IllegalArgumentException("more than one start");
        }
        
        if (numEnds == 0) {
            throw new IllegalArgumentException("missing end");
        }
        
        if (numEnds > 1) {
            throw new IllegalArgumentException("more than one end");
        }
        
        if (emptyCount == 0) {
            unsolveable = true;
        }
        
        maxLength = emptyCount;
        startI = tempStartI;
        startJ = tempStartJ;
        
        System.out.println(Size.of(maze));
        
        iMoves = new int[maxLength];
        jMoves = new int[maxLength];
    }
    
    protected boolean isVeryBig() {
        return false;
    }
    
    private static char[][] readAsCharMatrix(final Path path) throws IOException {
        final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        final char[][] charMatrix = new char[lines.size()][];
        for (int i = 0; i < charMatrix.length; i++) {
            charMatrix[i] = lines.get(i).toCharArray();
        }
        return charMatrix;
    }
    
    protected AbstractMaze(final Path path) throws IOException {
        this(readAsCharMatrix(path));
    }
    
    protected static char[][] openMazeCharMatrix(int m, int n) {
        // don't count border as part of size
        m += 2;
        n += 2;
        final char[][] maze = new char[m][n];
        final char[] row = new char[n];
        Arrays.fill(row, ' ');
        row[0] = row[n - 1] = '#';
        for (int i = 1; i < m - 1; i++) {
            maze[i] = row.clone();
        }
        Arrays.fill(row, '#');
        maze[0] = row.clone();
        maze[m - 1] = row.clone();
        maze[1][1] = 'S';
        maze[m - 2][n - 2] = 'E';
        return maze;
    }
    
    public void clearTerminal() {
        //erase terminal, go to top left of screen.
        System.out.println("\033[2J\033[1;1H");
    }
    
    public void setAnimate(final boolean animate) {
        this.animate = animate;
    }
    
    public void setAnimate() {
        animate = true;
    }
    
    protected void animate() {
        System.out.println(this);
        clearTerminal();
        try {
            Thread.sleep(20);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    protected abstract boolean findAnyPath();
    
    public final boolean anyPath() {
        if (solved) {
            return true;
        }
        if (unsolveable) {
            return false;
        }
        if (findAnyPath()) {
            //maze[startI][startJ] = START;
            solved = true;
            return anyPath();
        }
        unsolveable = true;
        return false;
    }
    
    @Deprecated
    protected abstract boolean aStarPath();
    
    @Deprecated
    protected boolean findShortestPath() {
        return aStarPath();
    }
    
    @Deprecated
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
    
    public void save(final Path path) throws IOException {
        Files.write(path, toString().getBytes());
    }
    
}
