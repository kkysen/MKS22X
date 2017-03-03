import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class Graph {
    
    protected List<int[][]> adjacencyMatrixPowers = new ArrayList<>();
    
    protected final int size;
    protected final int[][] adjacencyMatrix;
    private final int[][] compressedAdjacencyMatrix;
    
    protected Graph(final int size) {
        this.size = size;
        adjacencyMatrix = new int[size][size];
        compressedAdjacencyMatrix = new int[size][];
    }
    
    protected static int[][] cloneMatrix(final int[][] matrix) {
        final int[][] clone = new int[matrix.length][];
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
    
    private static String padLeft(final int i, final int length) {
        return padLeft(String.valueOf(i), length);
    }
    
    protected static void printMatrix(final int[][] matrix) {
        final int size = matrix.length * matrix[0].length;
        final int padLength = String.valueOf(Integer.MIN_VALUE).length();
        final StringBuilder sb = new StringBuilder((padLength + 1) * size);
        for (final int[] element : matrix) {
            for (final int element2 : element) {
                sb.append(padLeft(element2, padLength));
                sb.append(' ');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);
    }
    
    protected abstract void fillAdjacencyMatrix();
    
    private void fillCompressedAdjacencyMatrix() {
        for (int i = 0; i < size; i++) {
            final List<Integer> indicesList = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                final int bool = adjacencyMatrix[i][j];
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
    }
    
    private static void multiplyAdjacencyMatrix(final int[][] compressedAdjacencyMatrix,
            final int[][] multiplicand, final int[][] product) {
        for (int i = 0; i < compressedAdjacencyMatrix.length; i++) {
            final int[] productRow = product[i];
            for (int j = 0; j < compressedAdjacencyMatrix[i].length; j++) {
                final int index = compressedAdjacencyMatrix[i][j];
                final int[] multiplicandRow = multiplicand[index];
                for (int k = 0; k < multiplicandRow.length; k++) {
                    productRow[k] += multiplicandRow[k];
                }
            }
        }
    }
    
    private void exponentiateAdjacencyMatrix(final int startingPower, final int power,
            int[][] multiplicand) {
        int[][] product = new int[size][size];
        int[][] temp;
        // raise adjacency matrix to Tth power
        // A[i][j] = # walks from vertex i to vertex j
        for (int i = startingPower; i < power; i++) {
            multiplyAdjacencyMatrix(compressedAdjacencyMatrix, multiplicand, product);
            adjacencyMatrixPowers.set(i, cloneMatrix(product));
            temp = multiplicand;
            multiplicand = product;
            product = temp;
        }
    }
    
    private void exponentiateAdjacencyMatrix(final int power) {
        final int startingPower = adjacencyMatrixPowers.size();
        int[][] multiplicand;
        if (startingPower == 0) {
            fillAdjacencyMatrix();
            fillCompressedAdjacencyMatrix();
            printMatrix(adjacencyMatrix);
            printMatrix(compressedAdjacencyMatrix);
            multiplicand = cloneMatrix(adjacencyMatrix);
        } else {
            multiplicand = cloneMatrix(adjacencyMatrixPowers.get(startingPower - 1));
        }
        for (int i = startingPower; i < power; i++) {
            adjacencyMatrixPowers.add(null);
        }
        exponentiateAdjacencyMatrix(startingPower, power, multiplicand);
    }
    
    protected int[][] numWalksMatrix(final int length) {
        if (length >= adjacencyMatrixPowers.size()) {
            exponentiateAdjacencyMatrix(length);
        }
        for (final int[][] m : adjacencyMatrixPowers) {
            System.out.println("\npower:\n");
            printMatrix(m);
        }
        return adjacencyMatrixPowers.get(length - 1);
    }
    
    protected int numWalks(final int length, final int startVertex, final int endVertex) {
        return numWalksMatrix(length)[startVertex][endVertex];
    }
    
}
