import java.io.IOException;
import java.nio.file.Path;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class GreedyMazeSolver extends FrontierMazeSolver {
    
    public GreedyMazeSolver(final char[][] maze) {
        super(maze);
    }
    
    public GreedyMazeSolver(final Path path) throws IOException {
        super(path);
    }
    
    protected abstract MazeHeuristic getMazeHeuristic();
    
    @Override
    protected Frontier getFrontier() {
        return new IJPriorityQueue(getMazeHeuristic(), minLength, endI, endJ);
    }
    
}
