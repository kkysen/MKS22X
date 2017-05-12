import java.io.IOException;
import java.nio.file.Path;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class BestFirstMazeSolver extends GreedyMazeSolver {
    
    public BestFirstMazeSolver(final char[][] maze) {
        super(maze);
    }
    
    public BestFirstMazeSolver(final Path path) throws IOException {
        super(path);
    }
    
    @Override
    protected MazeHeuristic getMazeHeuristic() {
        return new BestFirstManhattanHeuristic();
    }
    
}
