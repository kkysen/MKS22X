import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class AdjacencyMatrixMultiplicationTest {
    
    private static int[][] compressedAdjacencyMatrix(final long[][] adjacencyMatrix) {
        final int size = adjacencyMatrix.length;
        final int[][] compressedAdjacencyMatrix = new int[size][];
        for (int i = 0; i < size; i++) {
            final List<Integer> indicesList = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                final long bool = adjacencyMatrix[i][j];
                if (bool == 1) {
                    indicesList.add(j);
                }
            }
            final int[] indices = new int[indicesList.size()];
            for (int k = 0; k < indices.length; k++) {
                indices[k] = indicesList.get(k);
            }
            compressedAdjacencyMatrix[i] = indices;
        }
        return compressedAdjacencyMatrix;
    }
    
    private static long[][] multiplyMatrix(final int m, final int n, final long[][] a,
            final long[][] b) {
        final long[][] ab = new long[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int sum = 0;
                for (int k = 0; k < m; k++) {
                    sum += a[i][k] * b[k][j];
                }
                ab[i][j] = sum;
            }
        }
        return ab;
    }
    
    private static boolean matrixEquals(final long[][] a, final long[][] b) {
        for (int i = 0; i < a.length; i++) {
            if (!Arrays.equals(a[i], b[i])) {
                return false;
            }
        }
        return a.length == b.length;
    }
    
    private static long[][] multiplyAdjacencyMatrix(final int[][] compressedAdjacencyMatrix,
            final long[][] multiplicand) {
        final long[][] product = new long[multiplicand.length][multiplicand[0].length];
        for (int i = 0; i < compressedAdjacencyMatrix.length; i++) {
            final long[] productRow = product[i];
            for (int j = 0; j < compressedAdjacencyMatrix[i].length; j++) {
                final int index = compressedAdjacencyMatrix[i][j];
                final long[] multiplicandRow = multiplicand[index];
                for (int k = 0; k < multiplicandRow.length; k++) {
                    productRow[k] += multiplicandRow[k];
                }
            }
        }
        return product;
    }
    
    public static boolean multiplyAdjacencyMatrixTest(final int n, final long[][] adjacencyMatrix,
            final long[][] multiplicand) {
        final int[][] compressedAdjacencyMatrix = compressedAdjacencyMatrix(adjacencyMatrix);
        final long[][] product = new long[n][n];
        multiplyAdjacencyMatrix(compressedAdjacencyMatrix, multiplicand);
        System.out.println("done");
        //Graph.printMatrix(product);
        final long[][] correctProduct = multiplyMatrix(n, n, adjacencyMatrix, multiplicand);
        //System.out.println();
        //Graph.printMatrix(correctProduct);
        return matrixEquals(product, correctProduct);
    }
    
    private static long[][] exponentiateAdjacencyMatrix(final long[][] adjacencyMatrix,
            final int power) {
        final int[][] compressedAdjacencyMatrix = compressedAdjacencyMatrix(adjacencyMatrix);
        long[][] multiplicand = cloneMatrix(adjacencyMatrix);
        // raise adjacency matrix to Tth power
        // A[i][j] = # walks from vertex i to vertex j
        for (int i = 1; i < power; i++) {
            multiplicand = multiplyAdjacencyMatrix(compressedAdjacencyMatrix, multiplicand);
        }
        return multiplicand;
    }
    
    private static long[][] cloneMatrix(final long[][] matrix) {
        final long[][] clone = new long[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            clone[i] = matrix[i].clone();
        }
        return clone;
    }
    
    private static String padLeft(final String s, final int length) {
        final char[] pad = new char[length - s.length()];
        Arrays.fill(pad, ' ');
        return s + new String(pad);
    }
    
    private static String padLeft(final long i, final int length) {
        return padLeft(String.valueOf(i), length);
    }
    
    private static void printMatrix(final long[][] matrix) {
        final int size = matrix.length * matrix[0].length;
        long max = 0;
        for (final long[] row : matrix) {
            for (final long element : row) {
                if (element > max) {
                    max = element;
                }
            }
        }
        final int padLength = String.valueOf(max).length();
        final StringBuilder sb = new StringBuilder((padLength + 1) * size);
        for (final long[] row : matrix) {
            for (final long element : row) {
                sb.append(padLeft(element, padLength));
                sb.append(' ');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);
    }
    
    private static boolean exponentiateAdjacencyMatrixTest(final long[][] adjacencyMatrix,
            final int power) {
        final int n = adjacencyMatrix.length;
        long[][] correctProduct = adjacencyMatrix;
        for (int i = 1; i < power; i++) {
            correctProduct = multiplyMatrix(n, n, adjacencyMatrix, correctProduct);
        }
        final long[][] product = exponentiateAdjacencyMatrix(adjacencyMatrix, power);
        System.out.println("\nproduct:");
        printMatrix(product);
        System.out.println("\ncorrectProduct:");
        printMatrix(correctProduct);
        return matrixEquals(product, correctProduct);
    }
    
    public static void multiplyAdjacencyMatrixTest(final int n) {
        final long max = 1000000;
        final long[][] adjacencyMatrix = new long[n][n];
        final long[][] multiplicand = new long[n][n];
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjacencyMatrix[i][j] = (random.nextInt() & 1000) == 100 ? 1 : 0;
                multiplicand[i][j] = random.nextLong(max);
            }
        }
        if (!multiplyAdjacencyMatrixTest(n, adjacencyMatrix, multiplicand)) {
            throw new AssertionError();
        }
    }
    
    public static void main(final String[] args) {
        multiplyAdjacencyMatrixTest(100);
        final long[][] adjacencyMatrix = {
            {0, 1, 1, 0},
            {1, 0, 0, 1},
            {1, 0, 0, 1},
            {0, 1, 1, 0}
        };
        if (!exponentiateAdjacencyMatrixTest(adjacencyMatrix, 30)) {
            throw new AssertionError();
        }
    }
    
}
