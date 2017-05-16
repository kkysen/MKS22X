import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MazeSolver {
    
    private final Path file;
    private final boolean animate;
    private boolean print;
    
    public MazeSolver(final String file, final boolean animate) {
        this.file = Paths.get(file);
        this.animate = animate;
    }
    
    public MazeSolver(final String file) {
        this(file, false);
    }
    
    private AbstractMazeSolver getSolver(final int style) throws IOException {
        switch (style) {
            case 0:
                return new DepthFirstMazeSolver(file);
            case 1:
                return new BreadthFirstMazeSolver(file);
            case 2:
                return new BestFirstMazeSolver(file);
            case 3:
                return new AStarMazeSolver(file);
            default:
                throw new IllegalArgumentException();
        }
    }
    
    /**
     * @param style algorithm to use:
     *            0 - DFS
     *            1 - BFS
     *            2 - Best First
     *            3 - A*
     */
    public void solve(final int style) {
        AbstractMazeSolver maze;
        try {
            maze = getSolver(style);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        maze.setAnimate(animate);
        maze.solve();
        if (print) {
            maze.print();
        }
    }
    
    public void solve() {
        solve(1);
    }
    
    public void setPrint(final boolean print) {
        this.print = print;
    }
    
    public void setPrint() {
        print = true;
    }
    
}
