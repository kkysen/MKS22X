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
    
    private Maze maze;
    
    public MazeSolver(final String file, final boolean animate) {
        this.file = Paths.get(file);
        this.animate = animate;
    }
    
    public MazeSolver(final String file) {
        this(file, false);
    }
    
    private Maze getSolver(final int style) throws IOException {
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
    
    private void setDefaultMaze() {
        if (maze == null) {
            try {
                maze = getSolver(3);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    @Override
    public String toString() {
        setDefaultMaze();
        return maze.toString();
    }
    
    public String toString(final int delay) {
        setDefaultMaze();
        return maze.toString(delay);
    }
    
    public static void main(final String[] args) {
        final MazeSolver solver = new MazeSolver("04MazeSolver/tests/maze4.dat");
        System.out.println(solver);
        solver.solve(1);
        final String answer = solver.toString();
        System.out.println(answer);
    }
    
}
