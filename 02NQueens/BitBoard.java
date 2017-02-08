import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class BitBoard {
    
    protected final int height;
    protected final int width;
    
    protected final BitSet bits;
    
    public BitBoard(final int height, final int width) {
        this.height = height;
        this.width = width;
        bits = new BitSet(height * width);
    }
    
    public BitSet getRow(int i) {
        return bits.get(i * height, ++i * height);
    }
    
    public List<BitSet> rows() {
        final List<BitSet> rows = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            rows.add(getRow(i));
        }
        return rows;
    }
    
    public boolean get(final int i, final int j) {
        return bits.get(i * height + j);
    }
    
    public void set(final int i, final int j) {
        bits.set(i * height + j);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(height * (width + 1));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(get(i, j) ? '\u25A0' : '\u25A1');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    
}
