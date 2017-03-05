import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream.Filter;
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
public class CowTravel {
    
    /**
     * If optimized, it will only consider the part of the maze that may be part
     * of a walk of the given length between the two points.
     * 
     * Therefore, all the other positions/vertices in the maze won't have their
     * number of walks calculated in the adjacency matrix power. Walks of
     * shorter length will still be cached.
     * 
     * If not optimized, the entire maze will going into the adjacency matrix
     * and the entire, huge matrix will be exponentiated. This is expensive in
     * both time and memory, the latter of which will require the cached
     * matrices to be compressed.
     * 
     * However, this will cache everything: all the
     * number of walks between any two positions in the maze of any length < T.
     */
    private static final boolean OPTIMIZE_BOUNDARY = true;
    
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
    
    public CowTravel(final Path path) throws IOException {
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
        
        if (OPTIMIZE_BOUNDARY) {
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
    
    public long numWalksOfShorterLength(final int length) {
        if (length > T) {
            throw new IllegalArgumentException("not computed yet");
        }
        return maze.numWalks(length, startI - iLowBound, startJ - jLowBound, endI - iLowBound,
                endJ - jLowBound);
    }
    
    public long numWalks() {
        return numWalksOfShorterLength(T);
    }
    
    public static boolean test(final Path inPath, final Path outPath) throws IOException {
        final CowTravel problem = new CowTravel(inPath);
        final long start = System.currentTimeMillis();
        System.out.println("# walks: " + problem.numWalks());
        System.out.println(SparseMatrix.seconds);
        //System.in.read();
        System.out.println((System.currentTimeMillis() - start) / 1000.0 + " sec");
        //System.out.println(problem.numWalksOfShorterLength(11));
        
        final int answer = Integer.parseInt(new String(Files.readAllBytes(outPath)).trim());
        System.out.println(problem.numWalks() + " should be " + answer);
        return problem.numWalks() == answer;
    }
    
    public static void main(final String[] args) throws IOException {
        final Path dir = Paths.get("USACO - MazeWalks (Cow Traveling)", "tests");
        int testNum = 0;
        for (final Path inPath : Files.newDirectoryStream(dir, new Filter<Path>() {
            
            @Override
            public boolean accept(final Path path) {
                return path.toString().endsWith(".in");
            }
            
        })) {
            final String inFileName = inPath.getFileName().toString();
            final String outFileName = inFileName.substring(0, inFileName.lastIndexOf('.') + 1)
                    + "out";
            final Path outPath = dir.resolve(outFileName);
            System.out.println("test # " + ++testNum);
            if (!test(inPath, outPath)) {
                throw new AssertionError(inPath.toString());
            }
            System.out.println();
        }
    }
    
}
