import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class CowTraveling {
    
    private final int N; // rows
    private final int M; // cols
    
    private final int T; // time (numMoves/length)
    
    private final int startI;
    private final int startJ;
    
    private final int endI;
    private final int endJ;
    
    private final GraphMaze maze;
    
    public CowTraveling(final Path path) throws IOException {
        final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        final int offset = 1; // num lines before maze starts
        
        Scanner scanner = new Scanner(lines.get(0));
        N = scanner.nextInt();
        M = scanner.nextInt();
        T = scanner.nextInt();
        scanner.close();
        
        scanner = new Scanner(lines.get(N + offset));
        startI = scanner.nextInt() - 1;
        startJ = scanner.nextInt() - 1;
        endI = scanner.nextInt() - 1;
        endJ = scanner.nextInt() - 1;
        scanner.close();
        
        maze = new GraphMaze(new GraphMaze.Input() {
            
            private final int height = lines.size() - 2;
            private final int width = lines.get(1).length();
            
            @Override
            public int getHeight() {
                return height;
            }
            
            @Override
            public int getWidth() {
                return width;
            }
            
            @Override
            public List<String> getLines() {
                return lines;
            }
            
            @Override
            public int getOffset() {
                return offset;
            }
            
        });
    }
    
    public long numWalks() {
        return maze.numWalks(T, startI, startJ, endI, endJ);
    }
    
    public static void main(final String[] args) throws IOException {
        final Path path = Paths.get("USACO", "cowTraveling2.txt");
        final CowTraveling problem = new CowTraveling(path);
        try {
            System.out.println("# walks: " + problem.numWalks());
        } catch (final OutOfMemoryError e) {
            System.in.read();
            throw e;
        }
        System.out.println(SparseMatrix.seconds);
        System.in.read();
    }
    
}
