import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class IterativeBacktrackingMazeSolver extends Maze {
    
    public IterativeBacktrackingMazeSolver(final char[][] maze) {
        super(maze);
    }
    
    public IterativeBacktrackingMazeSolver(final Path path) throws IOException {
        super(path);
    }
    
    public static IterativeBacktrackingMazeSolver openMaze(final int m, final int n) {
        return new IterativeBacktrackingMazeSolver(openMazeCharMatrix(m, n));
    }
    
    @Override
    public IJ solve() {
        final boolean localAnimate = animate;
        
        final int[] iStack = new int[maxLength];
        final int[] jStack = new int[maxLength];
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
                printAndPause();
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
        
        if (!wasSolved) {
            return null;
        }
        
        // fill in final path taken
        IJ ij = new IJ(startI, startJ, null, 0);
        for (int c = 0; c < moveNum; c++) {
            final int y = iStack[c];
            final int x = jStack[c];
            maze[y][x] = PATH;
            ij = ij.next(y, x);
        }
        
        maze[startI][startJ] = START;
        return ij;
    }
    
    public static void main(final String[] args) throws IOException {
        final IterativeBacktrackingMazeSolver maze = new IterativeBacktrackingMazeSolver(
                Paths.get("04MazeSolver/tests/maze.txt"));
        maze.animate();
        maze.solve();
        maze.print();
    }
    
}
