import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class AStarMazeSolver extends GreedyMazeSolver {
    
    public AStarMazeSolver(final char[][] maze) {
        super(maze);
    }
    
    public AStarMazeSolver(final Path path) throws IOException {
        super(path);
    }
    
    @Override
    protected MazeHeuristic getMazeHeuristic() {
        return new AStarManhattanHeuristic();
    }
    
    public static void main(final String[] args) throws IOException {
        final AStarMazeSolver maze = new AStarMazeSolver(
                Paths.get("04MazeSolver/tests/unsolveableMaze.dat"));
        maze.solve();
        maze.print();
    }
    
}
