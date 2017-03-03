import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MemoryMappedIntList {
    
    private static final int DEFAULT_BUFFER_SIZE = 1_000_000;
    
    private static final String FILE_NAME = MemoryMappedIntList.class.getSimpleName();
    
    private static int instanceCount = 0;
    
    private final int defaultBufferSize;
    
    private final long size;
    
    private final MappedByteBuffer fileBuffer;
    private final IntBuffer intBuffer;
    
    private int fromIndex;
    private int toIndex;
    private int[] localBuffer;
    
    public MemoryMappedIntList(final long size, final int defaultBufferSize) throws IOException {
        this.defaultBufferSize = defaultBufferSize;
        this.size = 0;
        final String fileName = FILE_NAME + "_" + ++instanceCount;
        final RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");
        final FileChannel fileChannel = randomAccessFile.getChannel();
        fileBuffer = fileChannel.map(MapMode.READ_WRITE, 0, size);
        randomAccessFile.close();
        intBuffer = fileBuffer.asIntBuffer();
        localBuffer = new int[defaultBufferSize];
        intBuffer.get(localBuffer);
    }
    
    public void loadRange(final int fromIndex, final int toIndex) {
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        final int length = toIndex - fromIndex;
        localBuffer = new int[length];
        intBuffer.get(localBuffer, fromIndex, length);
    }
    
    private void loadAround(final int index) {
        loadRange(index - defaultBufferSize / 2, index + defaultBufferSize / 2);
    }
    
    public int get(final int index) {
        
    }
    
    public long size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * @see java.util.List#size()
     */
    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /**
     * @see java.util.List#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.List#contains(java.lang.Object)
     */
    @Override
    public boolean contains(final Object o) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.List#iterator()
     */
    @Override
    public Iterator<Integer> iterator() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#toArray()
     */
    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#toArray(java.lang.Object[])
     */
    @Override
    public <T> T[] toArray(final T[] a) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#add(java.lang.Object)
     */
    @Override
    public boolean add(final Integer e) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.List#remove(java.lang.Object)
     */
    @Override
    public boolean remove(final Object o) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.List#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(final Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.List#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(final Collection<? extends Integer> c) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends Integer> c) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.List#removeAll(java.util.Collection)
     */
    @Override
    public boolean removeAll(final Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.List#retainAll(java.util.Collection)
     */
    @Override
    public boolean retainAll(final Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * @see java.util.List#clear()
     */
    @Override
    public void clear() {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * @see java.util.List#get(int)
     */
    @Override
    public Integer get(final int index) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#set(int, java.lang.Object)
     */
    @Override
    public Integer set(final int index, final Integer element) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#add(int, java.lang.Object)
     */
    @Override
    public void add(final int index, final Integer element) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * @see java.util.List#remove(int)
     */
    @Override
    public Integer remove(final int index) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#indexOf(java.lang.Object)
     */
    @Override
    public int indexOf(final Object o) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /**
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    @Override
    public int lastIndexOf(final Object o) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /**
     * @see java.util.List#listIterator()
     */
    @Override
    public ListIterator<Integer> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#listIterator(int)
     */
    @Override
    public ListIterator<Integer> listIterator(final int index) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @see java.util.List#subList(int, int)
     */
    @Override
    public List<Integer> subList(final int fromIndex, final int toIndex) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
