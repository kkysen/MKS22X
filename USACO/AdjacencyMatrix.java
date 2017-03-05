import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class AdjacencyMatrix {
    
    private static class AdjacencyMatrixPowers {
        
        private final List<SparseMatrix> adjacencyMatrixPowers = new ArrayList<>();
        
        private final long memoryOfEach;
        
        private int nextPower = 0;
        private int nextCompressed = 0;
        
        public AdjacencyMatrixPowers(final long[][] adjacencyMatrix) {
            adjacencyMatrixPowers.add(0, null); // undefined for 0th power
            nextPower++;
            nextCompressed++;
            addNoMemCheck(adjacencyMatrix);
            memoryOfEach = adjacencyMatrixPowers.get(1).approximateMemory();
        }
        
        public int nextPower() {
            return nextPower;
        }
        
        private void addNoMemCheck(final long[][] adjacencyMatrixPower) {
            //            int estimatedNumNonZeros = 10000 * (2 + nextPower * (nextPower + 1));
            //            System.out.println("power: " + nextPower);
            //            System.out.println("estimate: " + estimatedNumNonZeros);
            //            estimatedNumNonZeros = -1;
            adjacencyMatrixPowers.add(new SparseMatrix(adjacencyMatrixPower));
            nextPower++;
        }
        
        private void compress() {
            adjacencyMatrixPowers.get(nextCompressed).compress();
            nextCompressed++;
        }
        
        private static long freeMemory() {
            return Runtime.getRuntime().freeMemory();
        }
        
        public void add(final long[][] adjacencyMatrixPower) {
            addNoMemCheck(adjacencyMatrixPower);
            while (freeMemory() / 2 < memoryOfEach) {
                //System.out.println(freeMemory());
                if (nextCompressed + 1 == nextPower) {
                    break; // don't compress last one, b/c needed for next exponentiation
                }
                compress();
            }
        }
        
        public SparseMatrix get(final int power) {
            return adjacencyMatrixPowers.get(power);
        }
        
        public long[][] last() {
            return get(nextPower - 1).toArrayMatrix();
        }
        
    }
    
    private final AdjacencyMatrixPowers adjacencyMatrixPowers;
    
    private final int size;
    private final int[][] compressedAdjacencyMatrix;
    
    public AdjacencyMatrix(final Graph graph) {
        size = graph.size();
        final long[][] adjacencyMatrix = new long[size][size];
        graph.fillAdjacencyMatrix(adjacencyMatrix);
        compressedAdjacencyMatrix = compressedAdjacencyMatrix(adjacencyMatrix);
        adjacencyMatrixPowers = new AdjacencyMatrixPowers(adjacencyMatrix);
    }
    
    public int size() {
        return size;
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
    
    private static void printCompressedMatrix(final int[][] compressedMatrix) {
        for (final int[] row : compressedMatrix) {
            System.out.println(Arrays.toString(row));
        }
    }
    
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
    
    // calculate from starting power (inclusive) to power (inclusive)
    // starting power usually starts as 2
    // starting power is next power to calculate
    private void exponentiateAdjacencyMatrix(final int startingPower, final int power,
            long[][] multiplicand) {
        // raise adjacency matrix to Tth power
        // A[i][j] = # walks from vertex i to vertex j
        for (int i = startingPower; i <= power; i++) {
            multiplicand = multiplyAdjacencyMatrix(compressedAdjacencyMatrix, multiplicand);
            adjacencyMatrixPowers.add(multiplicand);
            System.gc();
        }
    }
    
    // calculates up to power, inclusive
    private void exponentiateAdjacencyMatrix(final int power) {
        final int startingPower = adjacencyMatrixPowers.nextPower();
        final long[][] multiplicand = adjacencyMatrixPowers.last();
        exponentiateAdjacencyMatrix(startingPower, power, multiplicand);
    }
    
    public SparseMatrix numWalksMatrix(final int length) {
        if (length >= adjacencyMatrixPowers.nextPower()) {
            exponentiateAdjacencyMatrix(length);
        }
        //        for (int i = 1; i < adjacencyMatrixPowers.size(); i++) {
        //            System.out.println("\npower " + i + ":\n");
        //            final long[][] matrix = adjacencyMatrixPowers.get(i);
        //            printMatrix(matrix);
        //        }
        //        for (int i = 0; i < 10; i++) {
        //            System.out.println(Arrays.toString(adjacencyMatrixPowers.get(length)[i]));
        //        }
        return adjacencyMatrixPowers.get(length);
    }
    
    public long numWalks(final int length, final int startVertex, final int endVertex) {
        return numWalksMatrix(length).get(startVertex, endVertex);
    }
    
}
