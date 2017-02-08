
/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class ChessBitBoard implements Cloneable {
    
    protected static long numClones = 0;
    protected static long numSets = 0;
    protected static long numGets = 0;
    protected static long numAdds = 0;
    
    public static String stats() {
        return "numClones = " + numClones
                + "\nnumSets   = " + numSets
                + "\nnumGets   = " + numGets
                + "\nnumAdds   = " + numAdds;
    }
    
    protected final int n;
    
    protected ChessBitBoard(final int n) {
        this.n = n;
    }
    
    @Override
    public abstract ChessBitBoard clone();
    
    public abstract void set(final int i, final int j);
    
    public abstract boolean get(final int i, final int j);
    
    public boolean isValidMove(final int i, final int j) {
        return !get(i, j);
    }
    
    protected abstract void addQueenColumn(int j);
    
    public ChessBitBoard addQueen(final Queen queen) {
        numAdds++;
        final ChessBitBoard newBoard = clone();
        newBoard.addQueenColumn(queen.j);
        for (int i = queen.i, j = queen.j; j >= 0 && i < n; j--, i++) {
            newBoard.set(i, j);
        }
        for (int i = queen.i, j = queen.j; j < n && i < n; j++, i++) {
            newBoard.set(i, j);
        }
        return newBoard;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(n * (n + 1));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(get(i, j) ? '\u25A0' : '\u25A1');
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    
    public static void main(final String[] args) {
        final int n = 4;
        final Queen root = Queen.root(n);
        final ChessBitBoard board1 = new LongChessBitBoard(n);
        final Queen queen1 = root.newChild(0, 0);
        final ChessBitBoard board2 = board1.addQueen(queen1);
        final Queen queen2 = queen1.newChild(1, 2);
        final ChessBitBoard board3 = board2.addQueen(queen2);
        System.out.println(board1);
        System.out.println(board2);
        System.out.println(board3);
    }
    
}
