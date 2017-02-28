import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.PriorityQueue;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class RecursiveMaze extends AbstractMaze {
    
    public RecursiveMaze(final char[][] maze) {
        super(maze);
    }
    
    public RecursiveMaze(final Path path) throws IOException {
        super(path);
    }
    
    private void move(final int i, final int j) {
        maze[i][j] = PATH;
    }
    
    private void undo(final int i, final int j) {
        maze[i][j] = VISITED;
    }
    
    private boolean findAnyPath(final int i, final int j) {
        if (animate) {
            animate();
        }
        
        final char c = maze[i][j];
        if (c == END) {
            return true;
        }
        if (c != EMPTY) {
            return false;
        }
        move(i, j);
        for (int k = 0; k < NUM_MOVES; k++) {
            if (findAnyPath(i + I_MOVES[k], j + J_MOVES[k])) {
                return true;
            }
        }
        undo(i, j);
        return false;
    }
    
    @Override
    protected boolean findAnyPath() {
        maze[startI][startJ] = EMPTY;
        final boolean solved = findAnyPath(startI, startJ);
        return solved;
    }
    
    @Override
    protected boolean aStarPath() {
        final PriorityQueue<Object> openSet = new PriorityQueue<>();
        return false; // FIXME
    }
    
    public static void main(final String[] args) throws IOException {
        final Path path = Paths.get("04MazeSolver/maze.txt");
        final AbstractMaze maze = new RecursiveMaze(path);
        maze.anyPath();
        System.out.println(maze);
    }
    
}
