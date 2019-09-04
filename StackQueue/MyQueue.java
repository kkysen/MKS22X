import java.util.ArrayDeque;

/**
 * IntQueue
 * 
 * @author Khyber Sen
 */
public class MyQueue extends ArrayDeque<Integer> {
    
    private static final long serialVersionUID = 3086819714101747605L;
    
    public MyQueue() {
        super();
    }
    
    public MyQueue(final int size) {
        super(size);
    }
    
    public void enqueue(final int i) {
        add(i);
    }
    
    public int dequeue() {
        return remove();
    }
    
}
