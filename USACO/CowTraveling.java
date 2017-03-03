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
        startI = scanner.nextInt();
        startJ = scanner.nextInt();
        endI = scanner.nextInt();
        endJ = scanner.nextInt();
        scanner.close();
        
        maze = new GraphMaze(N, M, new GraphMaze.Input() {
            
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
    
    public int numWalks() {
        return maze.numWalks(T, startI, startJ, endI, endJ);
    }
    
    public static void main(final String[] args) throws IOException {
        final Path path = Paths.get("USACO", "cowTraveling2.txt");
        final CowTraveling problem = new CowTraveling(path);
        System.out.println("\n# walks: " + problem.numWalks());
    }
    
}
