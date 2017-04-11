import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * IntStack
 * 
 * @author Khyber Sen
 */
public class MyStack {
    
    private int[] a;
    private int size = 0;
    
    public MyStack() {
        this(10);
    }
    
    public MyStack(final int size) {
        a = new int[size];
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void push(final int i) {
        if (a.length == size) {
            a = Arrays.copyOf(a, size << 1);
        }
        a[size++] = i;
    }
    
    private final void checkEmpty() {
        if (size == 0) {
            //throw new EmptyStackException();
            throw new NoSuchElementException();
        }
    }
    
    public int pop() {
        checkEmpty();
        return a[--size];
    }
    
    public int peek() {
        checkEmpty();
        return a[size - 1];
    }
    
    public static void main(final String[] args) {
        final MyStack stack = new MyStack();
        stack.push(1);
        stack.push(10);
        System.out.println(stack.peek());
        System.out.println(stack.size());
        System.out.println(stack.pop());
        System.out.println(stack.size());
        System.out.println(stack.pop());
    }
    
}
