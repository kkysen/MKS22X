/**
 * 
 * 
 * @author Khyber Sen
 */
public final class IJPriorityQueue implements Frontier {
    
    /**
     * 
     * 
     * @author Khyber Sen
     */
    private class IJCmp implements Comparable<IJCmp> {
        
        private final IJ ij;
        public final int cost;
        
        public IJCmp(final IJ ij, final int cost) {
            this.ij = ij;
            this.cost = cost;
        }
        
        @Override
        public int compareTo(final IJCmp p) {
            return cost - p.cost;
        }
        
        @Override
        public String toString() {
            return "(" + ij.i + ", " + ij.j + ", $" + cost + ")";
        }
        
    }
    
    private final MazeHeuristic heuristic;
    
    private final PriorityQueue<IJCmp> queue;
    
    private final int endI;
    private final int endJ;
    
    public IJPriorityQueue(final MazeHeuristic heuristic, final int capacity, final int endI,
            final int endJ) {
        this.heuristic = heuristic;
        queue = new PriorityQueue<>(capacity);
        this.endI = endI;
        this.endJ = endJ;
    }
    
    public IJPriorityQueue(final MazeHeuristic heuristic, final int endI, final int endJ) {
        this(heuristic, 10, endI, endJ);
    }
    
    @Override
    public int size() {
        return queue.size();
    }
    
    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    @Override
    public void clear() {
        queue.clear();
    }
    
    @Override
    public void add(final IJ ij, final int distance) {
        queue.add(new IJCmp(ij, heuristic.cost(distance, ij.i, ij.j, endI, endJ)));
    }
    
    @Override
    public IJ remove() {
        return queue.remove().ij;
    }
    
    @Override
    public String toString() {
        return queue.toString();
    }
    
}
