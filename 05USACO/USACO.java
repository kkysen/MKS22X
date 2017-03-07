
/**
 * 
 * 
 * @author Khyber Sen
 */
public class USACO {
    
    public static String name() {
        return "Sen,Khyber";
    }
    
    public USACO() {}
    
    public int bronze(final String fileName) {
        return new MakeLake(fileName).lakeVolume();
    }
    
    public int silver(final String fileName) {
        return (int) new CowTravel(fileName).numWalks();
    }
    
}
