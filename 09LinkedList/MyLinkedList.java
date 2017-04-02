import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import sun.misc.Unsafe;

/**
 * an IntegerLinkedList wrapper class for the generic {@link LinkedList}
 * 
 * @author Khyber Sen
 */
public class MyLinkedList extends LinkedList<Integer> {
    
    /**
     * @see LinkedList#LinkedList()
     */
    public MyLinkedList() {}
    
    /**
     * @see LinkedList#LinkedList(Collection)
     * 
     * @param c the Integer collection whose elements are to be placed in this
     *            list
     */
    public MyLinkedList(final Collection<? extends Integer> c) {
        super(c);
    }
    
    public static void main(final String[] args) throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        final MyLinkedList list = new MyLinkedList();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println(list);
        list.add(5, 100);
        
        System.out.println(list);
        for (final int i : list) {
            System.out.println(i);
        }
        System.out.println(list.contains(4));
        System.out.println(list.indexOf(100));
        System.out.println(list);
        list.remove(8);
        System.out.println(list);
        list.remove(new Integer(100));
        list.set(2, -1);
        System.out.println(list);
        
        final Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        final Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        System.out.println(unsafe.addressSize());
        
        System.out.println(new MyLinkedList(Arrays.asList(1, 2, 3)));
        System.out.println(new MyLinkedList());
    }
    
}
