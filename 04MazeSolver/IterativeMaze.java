import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class IterativeMaze extends AbstractMaze {
    
    public IterativeMaze(final char[][] maze) {
        super(maze);
    }
    
    public IterativeMaze(final Path path) throws IOException {
        super(path);
    }
    
    public static IterativeMaze openMaze(final int m, final int n) {
        return new IterativeMaze(openMazeCharMatrix(m, n));
    }
    
    @Override
    protected boolean findAnyPath() {
        // allow Hotspot to remove animate checks b/c final var
        final boolean localAnimate = animate;
        
        // copy to stack for faster access
        final int[] iStack = iMoves;
        final int[] jStack = jMoves;
        final int[] kStack = new int[maxLength];
        
        int i = startI;
        int j = startJ;
        int k = 0;
        int moveNum = 0;
        boolean wasSolved = false;
        
        for (;;) {
            
            // move
            if (k == 0) {
                maze[i][j] = VISITED;
                iStack[moveNum] = i;
                jStack[moveNum] = j;
            }
            
            // either backtrack or fail
            if (k == NUM_MOVES) {
                moveNum--;
                // no solution exists
                if (moveNum == 0) {
                    break;
                }
                // backtrack
                i = iStack[moveNum];
                j = jStack[moveNum];
                k = kStack[moveNum];
                continue;
            }
            
            if (localAnimate) {
                animate();
            }
            
            // check position ahead
            i += I_MOVES[k];
            j += J_MOVES[k];
            final char next = maze[i][j];
            
            // check if finished
            if (next == END) {
                moveNum++;
                wasSolved = true;
                break;
            }
            
            // continue iterating through moves
            if (next != EMPTY) {
                i -= I_MOVES[k];
                j -= J_MOVES[k];
                k++;
                continue;
            }
            
            // "recurse"
            k++;
            kStack[moveNum] = k;
            moveNum++;
            k = 0;
        }
        
        // fill in final path taken
        for (int c = 0; c < moveNum; c++) {
            maze[iStack[c]][jStack[c]] = PATH;
        }
        printMemoryStats();
        return wasSolved;
    }
    
    @Override
    protected boolean aStarPath() {
        return false;
    }
    
    private static void printMemoryStats() {
        final Runtime runtime = Runtime.getRuntime();
        System.out.println("free mem : " + runtime.freeMemory());
        System.out.println("total mem: " + runtime.totalMemory());
        System.out.println("max mem  : " + runtime.maxMemory());
    }
    
    public static void main(final String[] args) throws IOException {
        AbstractMaze maze = null;
        try {
            maze = IterativeMaze.openMaze(1_000_000, 100);
        } catch (final OutOfMemoryError e) {
            printMemoryStats();
            System.in.read();
            throw e;
        }
        maze.save(Paths.get("04MazeSolver/generatedMaze.dat"));
        final long start = System.nanoTime();
        System.out.println(maze.anyPath());
        final double seconds = (System.nanoTime() - start) / 1e9;
        maze.save(Paths.get("04MazeSolver/solvedGeneratedMaze.dat"));
        System.out.println(seconds + " sec");
    }
    
}
