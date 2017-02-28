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
    
    @Override
    protected boolean findAnyPath() {
        // allow JIT to remove animate checks b/c final var
        final boolean localAnimate = animate;
        final int[] iStack = iMoves;
        final int[] jStack = jMoves;
        final int[] kStack = new int[maxLength];
        int i = startI;
        int j = startJ;
        int k = 0;
        int moveNum = 0;
        boolean wasSolved = false;
        for (;;) {
            if (k == 0) {
                maze[i][j] = VISITED;
                iStack[moveNum] = i;
                jStack[moveNum] = j;
            }
            if (k == NUM_MOVES) {
                moveNum--;
                if (moveNum == 0) {
                    break;
                }
                i = iStack[moveNum];
                j = jStack[moveNum];
                k = kStack[moveNum];
                continue;
            }
            
            if (localAnimate) {
                animate();
            }
            
            i += I_MOVES[k];
            j += J_MOVES[k];
            final char next = maze[i][j];
            if (next == END) {
                moveNum++;
                wasSolved = true;
                break;
            }
            if (next != EMPTY) {
                i -= I_MOVES[k];
                j -= J_MOVES[k];
                k++;
                continue;
            }
            k++;
            kStack[moveNum] = k;
            moveNum++;
            k = 0;
        }
        for (int c = 0; c < moveNum; c++) {
            maze[iStack[c]][jStack[c]] = PATH;
        }
        return wasSolved;
    }
    
    @Override
    protected boolean aStarPath() {
        return false;
    }
    
    public static void main(final String[] args) throws IOException {
        final Path path = Paths.get("04MazeSolver/maze1.dat");
        final AbstractMaze maze = new IterativeMaze(path);
        //        final int m = 500;
        //        final int n = 500;
        //        final char[] edge = new char[n];
        //        Arrays.fill(edge, '#');
        //        final char[] middle = new char[n];
        //        Arrays.fill(middle, ' ');
        //        System.out.println(new String(middle));
        //        middle[0] = '#';
        //        middle[n - 1] = '#';
        //        final char[][] grid = new char[m][n];
        //        Arrays.fill(grid, middle);
        //        grid[0] = edge;
        //        grid[m - 1] = edge;
        //        grid[1] = middle.clone();
        //        grid[m - 2] = middle.clone();
        //        grid[1][1] = 'S';
        //        grid[m - 2][n - 2] = 'E';
        //        for (final char[] row : grid) {
        //            System.out.println(new String(row));
        //        }
        //        final Maze maze = new IterativeMaze(grid);
        maze.setAnimate(true);
        maze.anyPath();
        System.out.println(maze);
    }
    
}