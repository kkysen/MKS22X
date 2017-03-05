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
    
    private final int iLowBound;
    private final int iHighBound;
    
    private final int jLowBound;
    private final int jHighBound;
    
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
        
        final int dy = Math.abs(startI - endI);
        final int dx = Math.abs(startJ - endJ);
        final int dist = dy + dx;
        
        final int extraDist = (T - dist) / 2 + 2;
        // don't know why I need the 2 here, but it doesn't work without out
        
        final int minI = Math.min(startI, endI) - extraDist;
        final int maxI = Math.max(startI, endI) + extraDist;
        final int minJ = Math.min(startJ, endJ) - extraDist;
        final int maxJ = Math.max(startJ, endJ) + extraDist;
        
        if (1 == 1) {
            iLowBound = minI < 0 ? 0 : minI;
            iHighBound = maxI > N ? N : maxI;
            jLowBound = minJ < 0 ? 0 : minJ;
            jHighBound = maxJ > M ? M : maxJ;
        } else {
            iLowBound = 0;
            iHighBound = N;
            jLowBound = 0;
            jHighBound = M;
        }
        
        maze = new GraphMaze(new GraphMaze.Input() {
            
            @Override
            public int getHeight() {
                return iHighBound - iLowBound;
            }
            
            @Override
            public int getWidth() {
                return jHighBound - jLowBound;
            }
            
            @Override
            public int getVerticalOffset() {
                return offset + iLowBound;
            }
            
            @Override
            public int getHorizontalOffset() {
                return jLowBound;
            }
            
            @Override
            public List<String> getLines() {
                return lines;
            }
            
        });
    }
    
    public long numWalks() {
        return maze.numWalks(T, startI - iLowBound, startJ - jLowBound, endI - iLowBound,
                endJ - jLowBound);
    }
    
    public static void main(final String[] args) throws IOException {
        final Path path = Paths.get("USACO - MazeWalks (Cow Traveling)", "cowTraveling4.txt");
        final CowTraveling problem = new CowTraveling(path);
        final long start = System.currentTimeMillis();
        System.out.println("# walks: " + problem.numWalks());
        System.out.println(SparseMatrix.seconds);
        //System.in.read();
        System.out.println((System.currentTimeMillis() - start) / 1000.0 + " sec");
    }
    
}
