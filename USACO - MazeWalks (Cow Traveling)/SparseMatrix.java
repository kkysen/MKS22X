/**
 * represents a sparse matrix, compressing it to save memory using a HashMap
 * 
 * @author Khyber Sen
 */
public class SparseMatrix {
    
    static double seconds = 0;
    
    private final int m;
    private final int n;
    
    private long[][] matrix;
    
    private final int estimatedNumNonZeros;
    private CompressedMatrix compressedMatrix;
    
    public SparseMatrix(final long[][] matrix, final int estimatedNumNonZeros) {
        this.matrix = matrix;
        m = matrix.length;
        n = m == 0 ? 0 : matrix[0].length;
        this.estimatedNumNonZeros = estimatedNumNonZeros;
    }
    
    public SparseMatrix(final long[][] matrix) {
        this(matrix, -1);
    }
    
    public int getHeight() {
        return m;
    }
    
    public int getWidth() {
        return n;
    }
    
    public boolean isCompressed() {
        return matrix == null;
    }
    
    public void compress() {
        if (isCompressed()) {
            return;
        }
        final long start = System.currentTimeMillis();
        compressedMatrix = new CompressedMatrix(matrix, estimatedNumNonZeros);
        final long elapsed = System.currentTimeMillis() - start;
        System.out.println(elapsed);
        seconds += elapsed / 1000.0;
        matrix = null; // free up memory
    }
    
    public long get(final int i, final int j) {
        if (!isCompressed()) {
            return matrix[i][j];
        }
        return compressedMatrix.get(i, j);
    }
    
    public long[][] toArrayMatrix() {
        return matrix;
    }
    
    public long approximateMemory() {
        return m * n * 8; // 8 bits per long
    }
    
}
