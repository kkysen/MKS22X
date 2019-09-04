import java.io.IOException;
import java.nio.file.Path;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class FrontierMazeSolver extends Maze {
    
    private final Frontier frontier;
    
    protected FrontierMazeSolver(final char[][] maze) {
        super(maze);
        frontier = getFrontier();
    }
    
    protected FrontierMazeSolver(final Path path) throws IOException {
        super(path);
        frontier = getFrontier();
    }
    
    protected abstract Frontier getFrontier();
    
    private IJ explore(final Frontier frontier, final IJ ij) {
        for (int move = 0; move < NUM_MOVES; move++) {
            final int i = ij.i;
            final int j = ij.j;
            maze[i][j] = VISITED;
            final int nextI = i + I_MOVES[move];
            final int nextJ = j + J_MOVES[move];
            final char next = maze[nextI][nextJ];
            final IJ nextIJ = ij.next(nextI, nextJ);
            if (next == EMPTY) {
                frontier.add(nextIJ);
                maze[nextI][nextJ] = FRONTIER;
            } else if (next == END) {
                return nextIJ;
            }
        }
        return null;
    }
    
    private IJ solve(final Frontier frontier) {
        final boolean localAnimate = animate;
        frontier.clear();
        frontier.add(new IJ(startI, startJ, null, 0));
        while (frontier.size() > 0) {
            if (localAnimate) {
                System.out.println(toString(pause));
                System.out.println(frontier);
            }
            final IJ end = explore(frontier, frontier.remove());
            if (end != null) {
                return end;
            }
        }
        return null;
    }
    
    @Override
    public IJ solve() {
        if (unsolveable) {
            return null;
        }
        final IJ end = solve(frontier);
        for (IJ ij = end.previous; ij != null; ij = ij.previous) {
            maze[ij.i][ij.j] = PATH;
        }
        maze[startI][startJ] = START;
        return end;
    }
    
}
