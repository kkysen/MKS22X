import java.io.IOException;
import java.nio.file.Path;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class BreadthFirstMazeSolver extends FrontierMazeSolver {
    
    public BreadthFirstMazeSolver(final char[][] maze) {
        super(maze);
    }
    
    public BreadthFirstMazeSolver(final Path path) throws IOException {
        super(path);
    }
    
    @Override
    protected Frontier getFrontier() {
        return new IJDeque(minLength);
    }
    
}
