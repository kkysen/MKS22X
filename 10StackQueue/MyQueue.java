import java.util.ArrayDeque;

/**
 * IntQueue
 * 
 * @author Khyber Sen
 */
public class MyQueue extends ArrayDeque<Integer> {
    
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
