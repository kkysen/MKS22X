
/**
 * read only but supports get
 * 
 * @author Khyber Sen
 */
public class CompressedMatrix {
    
    private final int numNonZeros;
    
    private final long[] values;
    
    private final int[] colIndices;
    private final int[] rowIndices; // start of new row
    
    private static int numNonZeros(final long[][] matrix) {
        int numNonZeros = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0) {
                    numNonZeros++;
                }
            }
        }
        System.out.println("numNonZeros: " + numNonZeros);
        return numNonZeros;
    }
    
    public CompressedMatrix(final long[][] matrix, final int numNonZeros) {
        this.numNonZeros = numNonZeros < 0 ? numNonZeros(matrix) : numNonZeros;
        
        values = new long[this.numNonZeros];
        colIndices = new int[this.numNonZeros];
        rowIndices = new int[matrix.length + 1];
        
        rowIndices[0] = 0;
        for (int i = 0, index = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0) {
                    values[index] = matrix[i][j];
                    colIndices[index] = j;
                    index++;
                }
            }
            rowIndices[i + 1] = index;
        }
    }
    
    public CompressedMatrix(final long[][] matrix) {
        this(matrix, -1);
    }
    
    public int getNumNonZeros() {
        return numNonZeros;
    }
    
    public long get(final int i, final int j) {
        final int rowStart = rowIndices[i];
        final int rowEnd = rowIndices[i + 1];
        for (int k = rowStart; k < rowEnd; k++) {
            final int colIndex = colIndices[k];
            if (colIndex == j) {
                return values[k];
            }
            if (colIndex > j) {
                return 0; // passed j, so must be a zero
            }
        }
        return 0; // trailing zero
    }
    
}
