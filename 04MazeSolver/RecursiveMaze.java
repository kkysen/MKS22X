import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.PriorityQueue;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class RecursiveMaze extends Maze {
    
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
        maze[i][j] = WALL;
    }
    
    private boolean findOnePath(final int i, final int j) {
        final char c = maze[i][j];
        if (c == END) {
            return true;
        }
        if (c == WALL || c == PATH) {
            return false;
        }
        move(i, j);
        for (int k = 0; k < NUM_MOVES; k++) {
            if (findOnePath(i + I_MOVES[k], j + J_MOVES[k])) {
                return true;
            }
        }
        undo(i, j);
        return false;
    }
    
    @Override
    protected boolean findAnyPath() {
        final boolean solved = findOnePath(startI, startJ);
        maze[startI][startJ] = START;
        return solved;
    }
    
    @Override
    protected boolean findShortestPath() {
        final PriorityQueue<Object> openSet = new PriorityQueue<>();
        return false; // FIXME
    }
    
    public static void main(final String[] args) throws IOException {
        final Path path = Paths.get("04MazeSolver/maze.txt");
        final Maze maze = new RecursiveMaze(path);
        maze.anyPath();
        System.out.println(maze);
    }
    
}
