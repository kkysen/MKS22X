import java.io.IOException;
import java.nio.file.Paths;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Maze {
    
    private final AbstractMaze delegate;
    
    public Maze(final String fileName) {
        // necessary to get around weird compiler errors
        // about not initializing a final field after System.exit(0)
        AbstractMaze temp = null;
        try {
            temp = new IterativeMaze(Paths.get(fileName));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println(e.toString());
            System.exit(0);
        }
        delegate = temp;
    }
    
    public boolean solve() {
        return delegate.findAnyPath();
    }
    
    public void setAnimate(final boolean animate) {
        delegate.setAnimate(animate);
    }
    
    public void clearTerminal() {
        delegate.clearTerminal();
    }
    
    @Override
    public String toString() {
        return delegate.toString();
    }
    
    public static void main(final String[] args) {
        final Maze f = new Maze("04MazeSolver/maze3.dat");//true animates the maze.
        
        //f.setAnimate(true);
        f.solve();
        
        System.out.println(f);
    }
    
}
