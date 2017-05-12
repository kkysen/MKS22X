/**
 * 
 * 
 * @author Khyber Sen
 */
public class AStarManhattanHeuristic extends BestFirstManhattanHeuristic {
    
    @Override
    public int cost(final int distanceToStart, final int i, final int j, final int endI,
            final int endJ) {
        return distanceToStart + super.cost(distanceToStart, i, j, endI, endJ);
    }
    
}
