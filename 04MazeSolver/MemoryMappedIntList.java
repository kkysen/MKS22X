import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

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
        return 0;
    }
    
    public long size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
}
