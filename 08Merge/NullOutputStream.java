import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class NullOutputStream extends OutputStream {
    
    @Override
    public void write(final int b) throws IOException {}
    
}
