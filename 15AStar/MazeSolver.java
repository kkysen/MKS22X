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
public abstract class MazeSolver {
    
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
    protected final int minLength;
    
    protected final char[][] maze;
    
    protected final int startI;
    protected final int startJ;
    
    protected final int endI;
    protected final int endJ;
    
    protected boolean unsolveable;
    
    protected boolean animate;
    private long pause = 20; // pause length in milliseconds
    
    private void checkNoMaze(final int dimension) {
        if (dimension == 0) {
            throw new IllegalArgumentException("no maze");
        }
    }
    
    protected MazeSolver(final char[][] maze) {
        m = maze.length;
        checkNoMaze(m);
        n = maze[0].length;
        checkNoMaze(n);
        this.maze = maze;
        
        int emptyCount = 0;
        
        int numStarts = 0;
        int numEnds = 0;
        
        int tempStartI = -1;
        int tempStartJ = -1;
        
        int tempEndI = -1;
        int tempEndJ = -1;
        
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
                    tempEndI = i;
                    tempEndJ = j;
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
        
        startI = tempStartI;
        startJ = tempStartJ;
        
        endI = tempEndI;
        endJ = tempEndJ;
        
        maxLength = emptyCount + 2; // 1 for uncounted S and E (not sure if E is needed, but just being safe)
        minLength = Math.max(10, Utils.manhattanDistance(startI, startJ, endI, endJ));
    }
    
    private static char[][] readAsCharMatrix(final Path path) throws IOException {
        final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        final char[][] charMatrix = new char[lines.size()][];
        for (int i = 0; i < charMatrix.length; i++) {
            charMatrix[i] = lines.get(i).toCharArray();
        }
        return charMatrix;
    }
    
    protected MazeSolver(final Path path) throws IOException {
        this(readAsCharMatrix(path));
    }
    
    protected static char[][] openMazeCharMatrix(int m, int n) {
        // don't count border as part of size
        m += 2;
        n += 2;
        final char[][] maze = new char[m][n];
        final char[] row = new char[n];
        Arrays.fill(row, ' ');
        row[0] = row[n - 1] = WALL;
        for (int i = 1; i < m - 1; i++) {
            maze[i] = row.clone();
        }
        Arrays.fill(row, WALL);
        maze[0] = row.clone();
        maze[m - 1] = row.clone();
        maze[1][1] = START;
        maze[m - 2][n - 2] = END;
        return maze;
    }
    
    public void clearTerminal() {
        //erase terminal, go to top left of screen.
        //System.out.println("\033[2J\033[1;1H");
        // FIXME
        //System.out.println("\n\n\n");
    }
    
    public void setAnimate(final boolean animate) {
        this.animate = animate;
    }
    
    public void animate() {
        animate = true;
    }
    
    public void animate(final long pause) {
        animate();
        this.pause = pause;
    }
    
    public void print() {
        System.out.println(toString());
    }
    
    private void pause() {
        clearTerminal();
        //        try {
        //            System.in.read();
        //        } catch (final IOException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        }
        //        try {
        //            Thread.sleep(pause);
        //        } catch (final InterruptedException e) {
        //            e.printStackTrace();
        //        }
    }
    
    protected void printAndPause() {
        print();
        pause();
    }
    
    public abstract IJ solve();
    
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