import java.io.IOException;
import java.nio.file.Path;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class DepthFirstMazeSolver extends FrontierMazeSolver {
    
    public DepthFirstMazeSolver(final char[][] maze) {
        super(maze);
    }
    
    public DepthFirstMazeSolver(final Path path) throws IOException {
        super(path);
    }
    
    @Override
    protected Frontier getFrontier() {
        return new IJStack(minLength);
    }
    
}
