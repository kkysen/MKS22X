/**
 * 
 * 
 * @author Khyber Sen
 */
public class AStarManhattanHeuristic extends BestFirstManhattanHeuristic {
    
    @Override
    public int cost(final int i, final int j, final int distanceToStart, final int endI,
            final int endJ) {
        return distanceToStart + super.cost(i, j, distanceToStart, endI, endJ);
    }
    
}
