import java.io.IOException;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MakeLake {
    
    private final int SQUARE_AREA = 6 * 6 * 12 * 12; // inches (6' x 6')
    private final int STOMP_HEIGHT = 3;
    private final int STOMP_WIDTH = 3;
    
    private final int height;
    private final int width;
    private final int[][] elevations;
    private final int[][] instructions;
    private final int lakeElevation;
    
    private MakeLake(final Scanner scanner) {
        height = scanner.nextInt();
        width = scanner.nextInt();
        elevations = new int[height][width];
        lakeElevation = scanner.nextInt();
        instructions = new int[scanner.nextInt()][3];
        scanner.nextLine();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                elevations[i][j] = scanner.nextInt();
            }
            scanner.nextLine();
        }
        for (int i = 0; i < instructions.length; i++) {
            for (int j = 0; j < 3; j++) {
                instructions[i][j] = scanner.nextInt();
            }
            scanner.nextLine();
        }
        scanner.close();
    }
    
    public MakeLake(final Path path) throws IOException {
        this(new Scanner(path));
    }
    
    private static Scanner createScanner(final Path path) {
        try {
            return new Scanner(path);
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
    
    public MakeLake(final String fileName) {
        this(createScanner(Paths.get(fileName)));
    }
    
    private void stomp(final int row, final int col, final int depth) {
        int highest = 0;
        for (int i = 0; i < STOMP_HEIGHT; i++) {
            for (int j = 0; j < STOMP_WIDTH; j++) {
                final int elevation = elevations[row + i][col + j];
                if (elevation > highest) {
                    highest = elevation;
                }
            }
        }
        final int newElevation = highest - depth;
        for (int i = 0; i < STOMP_HEIGHT; i++) {
            for (int j = 0; j < STOMP_WIDTH; j++) {
                if (elevations[row + i][col + j] > newElevation) {
                    elevations[row + i][col + j] = newElevation;
                }
            }
        }
    }
    
    private void stompAll() {
        for (int k = 0; k < instructions.length; k++) {
            final int[] row = instructions[k];
            stomp(row[0] - 1, row[1] - 1, row[2]);
        }
    }
    
    public int lakeVolume() {
        stompAll();
        int volume = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                final int depth = lakeElevation - elevations[i][j];
                volume += depth < 0 ? 0 : depth;
            }
        }
        return volume * SQUARE_AREA;
    }
    
    private static String padLeft(final String s, final int length) {
        final char[] pad = new char[length - s.length()];
        Arrays.fill(pad, ' ');
        return s + new String(pad);
    }
    
    private static String padLeft(final long i, final int length) {
        return padLeft(String.valueOf(i), length);
    }
    
    public void printElevations() {
        final int size = elevations.length * elevations[0].length;
        long max = 0;
        for (final int[] row : elevations) {
            for (final int element : row) {
                if (element > max) {
                    max = element;
                }
            }
        }
        final int padLength = String.valueOf(max).length();
        final StringBuilder sb = new StringBuilder((padLength + 1) * size);
        for (final int[] row : elevations) {
            for (final int element : row) {
                sb.append(padLeft(element, padLength));
                sb.append(' ');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);
    }
    
    public static boolean test(final Path inPath, final Path outPath) throws IOException {
        final MakeLake lakeMaker = new MakeLake(inPath);
        final int answer = Integer.parseInt(new String(Files.readAllBytes(outPath)).trim());
        final int volume = lakeMaker.lakeVolume();
        System.out.println(volume + " should be " + answer);
        return volume == answer;
    }
    
    public static void main(final String[] args) throws IOException {
        final Path dir = Paths.get("05 USACO - Lake Making", "tests");
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
