import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MazeTests {
    
    private MazeTests() {}
    
    public static <M extends AbstractMazeSolver> void test(final Class<M> type, final Path dir,
            final Path file) throws IOException {
        Constructor<M> constructor;
        try {
            constructor = type.getDeclaredConstructor(Path.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException();
        }
        AbstractMazeSolver maze;
        try {
            maze = constructor.newInstance(dir.resolve(file));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException();
        }
        //maze.animate(1000);
        final long start = System.nanoTime();
        maze.solve();
        final long time = System.nanoTime() - start;
        final Path solvedFile = Paths
                .get("solved " + file.toString() + " - " + type.getSimpleName());
        //maze.save(dir.resolve(solvedFile));
        //maze.print();
        System.out.println(
                String.format("%1$31s" + ": " + time / 1e9 + " sec", type.getSimpleName()));
    }
    
    public static void test(final Class<? extends AbstractMazeSolver> type) throws IOException {
        test(type, Paths.get("04MazeSolver", "tests"), Paths.get("biggerMaze.dat"));
        System.out.println("waiting...");
        //System.in.read();
    }
    
    public static void testAll() throws IOException {
        //test(IterativeBacktrackingMazeSolver.class);
        test(DepthFirstMazeSolver.class);
        test(BreadthFirstMazeSolver.class);
        test(BestFirstMazeSolver.class);
        test(AStarMazeSolver.class);
    }
    
    public static void main(final String[] args) throws IOException {
        //testAll();
        //test(AStarMazeSolver.class);
        final MazeSolver solver = new MazeSolver("../04MazeSolver/tests/maze.txt", true);
        solver.setPrint();
        for (int i = 0; i < 4; i++) {
            System.out.println(i);
            solver.solve(i);
        }
    }
    
}
