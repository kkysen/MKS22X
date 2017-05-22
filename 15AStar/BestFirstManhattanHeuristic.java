/**
 * 
 * 
 * @author Khyber Sen
 */
public class BestFirstManhattanHeuristic implements MazeHeuristic {
    
    @Override
    public int cost(final int i, final int j, final int distanceToStart, final int endI,
            final int endJ) {
        return Utils.manhattanDistance(i, j, endI, endJ);
    }
    
}
