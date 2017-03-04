import java.util.HashMap;
import java.util.Map;

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
    
    //private final LongTrie map = new LongTrie();
    private final Map<Long, Long> map = new HashMap<>();
    
    public SparseMatrix(final long[][] matrix) {
        this.matrix = matrix;
        m = matrix.length;
        n = m == 0 ? 0 : matrix[0].length;
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
        if (1 == 1) {
            //return;
        }
        final long start = System.currentTimeMillis();
        for (int i = 0; i < m; i++) {
            final long[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                final long val = matrix[i][j];
                if (val != 0) {
                    put(i, j, val);
                }
            }
        }
        final long elapsed = System.currentTimeMillis() - start;
        System.out.println(elapsed);
        seconds += elapsed / 1000.0;
        matrix = null; // free up memory
    }
    
    private long convertIndex(final int i, final int j) {
        return (long) i * m + j;
    }
    
    private void put(final int i, final int j, final long val) {
        map.put(convertIndex(i, j), val);
    }
    
    public long get(final int i, final int j) {
        if (!isCompressed()) {
            return matrix[i][j];
        }
        final Long val = map.get(convertIndex(i, j));
        return val == null ? 0 : val;
    }
    
    public long[][] toArrayMatrix() {
        return matrix;
    }
    
    public long approximateMemory() {
        return m * n * 8; // 8 bits per long
    }
    
}
