import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class GraphMaze implements Graph {
    
    protected static final int NUM_MOVES = 4;
    protected static final int[] I_MOVES = {1, -1, 0, 0};
    protected static final int[] J_MOVES = {0, 0, 1, -1};
    // down, up, right, left in that order
    
    protected static final char EMPTY = '.';
    protected static final char WALL = '*';
    
    protected final int m;
    protected final int n;
    
    protected final char[][] maze;
    
    protected final AdjacencyMatrix adjacencyMatrix;
    
    public interface Input {
        
        public int getHeight();
        
        public int getWidth();
        
        public List<String> getLines();
        
        public int getOffset();
        
    }
    
    private GraphMaze(final int m, final int n, final List<String> lines, final int offset) {
        this.m = m;
        this.n = n;
        maze = new char[m + 2][n + 2];
        fillMaze(lines, offset);
        adjacencyMatrix = new AdjacencyMatrix(this);
    }
    
    @Override
    public int size() {
        return m * n;
    }
    
    public GraphMaze(final Input input) {
        this(input.getHeight(), input.getWidth(), input.getLines(), input.getOffset());
    }
    
    private static Input pathToInput(final Path path) throws IOException {
        final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        final int m = lines.size();
        final int n = lines.get(0).length();
        
        return new Input() {
            
            @Override
            public int getHeight() {
                return m;
            }
            
            @Override
            public int getWidth() {
                return n;
            }
            
            @Override
            public List<String> getLines() {
                return lines;
            }
            
            @Override
            public int getOffset() {
                return 0;
            }
            
        };
    }
    
    public GraphMaze(final Path path) throws IOException {
        this(pathToInput(path));
    }
    
    private void fillMaze(final List<String> lines, final int offset) {
        for (int i = 0; i < m; i++) {
            final char[] src = lines.get(i + offset).toCharArray();
            final char[] row = maze[i + 1];
            System.arraycopy(src, 0, row, 1, src.length);
            row[0] = row[n + 1] = WALL;
        }
        Arrays.fill(maze[0], WALL);
        Arrays.fill(maze[m + 1], WALL);
    }
    
    protected static void printMaze(final char[][] maze) {
        for (final char[] row : maze) {
            System.out.println(new String(row));
        }
    }
    
    private int matrixIndex(final int i, final int j) {
        return i * n + j;
    }
    
    private void fillVertex(final long[][] adjacencyMatrix, final int r, final int c) {
        // r, c in maze; i, j in matrix
        for (int k = 0; k < NUM_MOVES; k++) {
            final int adjR = r + I_MOVES[k];
            final int adjC = c + J_MOVES[k];
            final char adjacent = maze[adjR + 1][adjC + 1];
            if (adjacent != WALL) {
                final int i = matrixIndex(r, c);
                final int j = matrixIndex(adjR, adjC);
                adjacencyMatrix[i][j] = 1;
            }
        }
    }
    
    @Override
    public void fillAdjacencyMatrix(final long[][] adjacencyMatrix) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                fillVertex(adjacencyMatrix, i, j);
            }
        }
        //reflectMatrixOverDiagonal(adjacencyMatrix);
    }
    
    // from top right fill in bottom left
    private static void reflectMatrixOverDiagonal(final int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix[i].length; j++) {
                matrix[j][i] = matrix[i][j];
            }
        }
    }
    
    public long numWalks(final int length, final int startI, final int startJ, final int endI,
            final int endJ) {
        final int startVertex = matrixIndex(startI, startJ);
        final int endVertex = matrixIndex(endI, endJ);
        return adjacencyMatrix.numWalks(length, startVertex, endVertex);
    }
    
}
